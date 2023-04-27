package com.scfg.core.security;

import com.scfg.core.application.port.in.AuthUserUseCase;
import com.scfg.core.application.port.out.AuthUserPort;
import com.scfg.core.application.port.out.ClassifierPort;
import com.scfg.core.application.port.out.RolePort;
import com.scfg.core.application.service.MenuService;
import com.scfg.core.common.credentials.ActiveDirectoryConfig;
import com.scfg.core.common.enums.AdConfigEnum;
import com.scfg.core.common.enums.BusinessGroupEnum;
import com.scfg.core.common.enums.ClassifierTypeEnum;
import com.scfg.core.common.enums.CompanyInitialEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.exception.OperationException;
import com.scfg.core.common.util.DateUtils;
import com.scfg.core.domain.common.*;
import com.scfg.core.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthUserDetailsService implements AuthUserUseCase, UserDetailsService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private final ActiveDirectoryConfig adConfig;

    private final AuthUserPort authUserPort;
    private final MenuService menuService;
    private final ClassifierPort classifierPort;
    private final RolePort rolePort ;
    private static final String BEARER = "Bearer ";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = authUserPort.findByEmail(username);
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(("ROLE_ADMIN"));
        return new org.springframework.security.core.userdetails.User(authUser.getEmail(), authUser.getPassword(), Arrays.asList(grantedAuthority));
    }

    @Override
    public AuthUser auth(AuthenticationRequest data) {
         AuthUser authUser = authUserPort.findByEmail(data.getUsername());
         String token = generateToken(data, authUser);
         authUser.setToken(token);
        return authUserPort.update(authUser);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {OperationException.class,
            AuthenticationServiceException.class, Exception.class})
    public AuthUser automaticLdapAuth(BfsAuthenticationRequest data) {
        AuthUser authUser = null;
        String stateFound = "";
        String roleFound = "";
        long lastLogon = 0L;

        ZoneId zid = ZoneId.of("America/La_Paz");
        LocalDateTime today = LocalDateTime.now(zid);
        int maxTimeWithoutLogin = 12;

        try {
            authUser = authUserPort.findByEmail(data.getEmail().toLowerCase().trim());
        } catch (NotDataFoundException notDataFoundEx) {
            log.error(notDataFoundEx.getMessage());
        }

        Attributes adAttrs = connectWithActiveDirectory(data.getEmail().toLowerCase().trim(), data.getCompany());
        try {
            if (data.getCompany().equals(CompanyInitialEnum.BFS.getValue())) {
                stateFound = adAttrs.get("st").get().toString();
                roleFound = adAttrs.get("title").get().toString();
            }

            lastLogon = (Long.parseLong(adAttrs.get("lastlogon").get().toString()) / 10000L) - +11644473600000L;

            Date auxDate = new Date(lastLogon);

            LocalDateTime lastLogonDate = DateUtils.asDateToLocalDateTime(auxDate);

            long t = ChronoUnit.HOURS.between(today, lastLogonDate);

            if (t > maxTimeWithoutLogin) {
                throw new OperationException("El usuario no ha iniciado sesión en su maquina local desde hace: " + t + " horas");
            }
        } catch (NamingException e) {
            log.error("No se pudo obtener el atributo del directorio activo: [{}]", e.getMessage());
        }

        if (authUser == null) {
            authUser = new AuthUser(adAttrs, data);
            if (data.getCompany().equals(CompanyInitialEnum.BFS.getValue())) {
                this.assignRegionalToBfsUser(authUser, stateFound);
                this.assignRoleToBfsUser(authUser, roleFound);
            }
        } else {
            if (data.getCompany().equals(CompanyInitialEnum.BFS.getValue())) {
                if (authUser.getCompanyIdc() == null) {
                    authUser.setCompanyIdc((long) BusinessGroupEnum.BFS.getValue());
                }
                authUser.setBfsUserCode(data.getCode());
                authUser.setBfsAgencyCode(data.getOfficeCode());
            }
        }

        String token = BEARER + jwtTokenProvider.createToken(data.getEmail(), authUser);
        authUser.setToken(token);
        return authUserPort.update(authUser);
    }

    @Override
    public String generateToken(AuthenticationRequest data, AuthUser authUser) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return BEARER + jwtTokenProvider.createToken(data.getUsername(), authUser);
        } catch (AuthenticationException e) {
            log.warn("Usuario o contraseña incorrectos: {}", data.getUsername());
            throw new BadCredentialsException("Usuario o contraseña incorrectos.");
        }catch (Exception e){
            log.error("Error de autenticación: [{}]", e.getMessage());
            throw e;
        }
    }

    @Override
    public AuthUser verifyToken(String token) {
        AuthUser authUser = authUserPort.findByToken(token);
        List<Menu> menuList =  menuService.getByRoleId(authUser.getRoleId());
        authUser.setMenuList(menuList);
        return authUser;
    }

    //#region Auxiliary methods

    private Attributes connectWithActiveDirectory(String userEmail, String company) {

        Attributes attrs = null;
        String adSecurityAuthentication = "simple";

        String test = adConfig.getScvsSearchBase() + "," + formatDomain(adConfig.getScvsServer());

        Map<String, String> adCredentials = new HashMap<>();
        adCredentials.put(CompanyInitialEnum.SCVS.getValue() + AdConfigEnum.AD_DOMAIN.getValue(),
                adConfig.getScvsServer());
        adCredentials.put(CompanyInitialEnum.SCVS.getValue() + AdConfigEnum.AD_PORT.getValue(), adConfig.getPort());
        adCredentials.put(CompanyInitialEnum.SCVS.getValue() + AdConfigEnum.AD_SEARCH_BASE.getValue(),
                adConfig.getScvsSearchBase() + "," + formatDomain(adConfig.getScvsServer()));

        adCredentials.put(CompanyInitialEnum.BFS.getValue() + AdConfigEnum.AD_DOMAIN.getValue(),
                adConfig.getBfsServer());
        adCredentials.put(CompanyInitialEnum.BFS.getValue() + AdConfigEnum.AD_PORT.getValue(), adConfig.getPort());
        adCredentials.put(CompanyInitialEnum.BFS.getValue() + AdConfigEnum.AD_SEARCH_BASE.getValue(),
                adConfig.getBfsSearchBase() + "," + formatDomain(adConfig.getBfsServer()));

        String adDomain = adCredentials.get(CompanyInitialEnum.SCVS.getValue() + AdConfigEnum.AD_DOMAIN.getValue());
        String adPort = adCredentials.get(CompanyInitialEnum.SCVS.getValue() + AdConfigEnum.AD_PORT.getValue());
        String searchBase = adCredentials.get(CompanyInitialEnum.SCVS.getValue() + AdConfigEnum.AD_SEARCH_BASE.getValue());

        if (company.equals(CompanyInitialEnum.BFS.getValue())) {
            adDomain = adCredentials.get(CompanyInitialEnum.BFS.getValue() + AdConfigEnum.AD_DOMAIN.getValue());
            adPort = adCredentials.get(CompanyInitialEnum.BFS.getValue() + AdConfigEnum.AD_PORT.getValue());
            searchBase = adCredentials.get(CompanyInitialEnum.BFS.getValue() + AdConfigEnum.AD_SEARCH_BASE.getValue());
        }

        Hashtable<String, String> environment = new Hashtable<>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, "ldap://" + adDomain + ":" + adPort);
        environment.put(Context.SECURITY_AUTHENTICATION, adSecurityAuthentication);
        environment.put(Context.SECURITY_PRINCIPAL, adConfig.getUsername());
        environment.put(Context.SECURITY_CREDENTIALS, adConfig.getPassword());

        LdapContext ctxGC;
        try {
            ctxGC = new InitialLdapContext(environment, null);
            String[] returnedAtts = {
                    "sAMAccountName",
                    "givenName",
                    "sn",
                    "cn",
                    "name",
                    "mail",
                    "memberOf",
                    "l",
                    "st",
                    "title",
                    "department",
                    "description",
                    "company",
                    "physicalDeliveryOfficeName",
                    "lastLogon",
                    "lastLogonTimestamp",
                    "lockoutTime"
            };
            SearchControls searchControls = new SearchControls();
            searchControls.setTimeLimit(600000);
            searchControls.setReturningAttributes(returnedAtts);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            int posI = userEmail.indexOf("@");
            String username = userEmail.substring(0, posI);
            String searchFilter = "(&(objectCategory=person)(objectClass=user)(sAMAccountName=" + username + "))";

            NamingEnumeration answer = ctxGC.search(searchBase, searchFilter, searchControls);

            if (!answer.hasMoreElements()) {
                throw new UsernameNotFoundException("Usuario no encontrado");
            } else {
                while (answer.hasMoreElements()) {
                    SearchResult sr = (SearchResult) answer.next();
                    attrs = sr.getAttributes();
                }
            }
        } catch (NamingException e) {
            log.error("Error de conexión con el directorio activo: [{}]", e.getMessage());
            throw new AuthenticationServiceException("Error de conexión con el directorio activo", e);
        } catch (Exception e) {
            log.error("Error de conexión con el directorio activo: [{}]", e.getMessage());
            throw new RuntimeException(e);
        }
        return attrs;
    }

    private String formatDomain(String domain) {
        StringBuilder buf = new StringBuilder();
        String[] array = domain.split(Pattern.quote("."));
        int length = array.length;
        for (int i = 1; i < length; i++) {
            String token = array[i];
            if (token.length() == 0) continue;
            if (buf.length() > 0) buf.append(",");
            buf.append("DC=").append(token);
        }
        return buf.toString();
    }

    private void assignRegionalToBfsUser(AuthUser authUser, String stateFound) {
        List<Classifier> regionalList = classifierPort.getAllClassifiersByClassifierTypeReferenceId(
                ClassifierTypeEnum.Regional.getReferenceId()
        );
        Classifier regional = regionalList.stream()
                .filter(e -> e.getDescription().toUpperCase().trim().equals(stateFound.toUpperCase().trim()))
                .findFirst()
                .orElse(null);

        if (regional != null) {
            authUser.setRegionalIdc(regional.getReferenceId());
        }
    }

    private void assignRoleToBfsUser(AuthUser authUser, String roleFound) {
        boolean hasRole = false;
        String defaultRole = "Rol por Defecto";
        String role1 = "Gestor";
        String role2 = "Jefe operativo";

        try {
            if (roleFound.toUpperCase().trim().contains(role1.toUpperCase())) {
                Role role = rolePort.findByName(CompanyInitialEnum.BFS.getValue().toUpperCase() + " - " + role1);
                authUser.setRoleId(role.getId());
                hasRole = true;
            }

            if (roleFound.toUpperCase().trim().contains(role2.toUpperCase())) {
                Role role = rolePort.findByName(CompanyInitialEnum.BFS.getValue().toUpperCase() + " - " + role2);
                authUser.setRoleId(role.getId());
                hasRole = true;
            }
        } catch (NotDataFoundException e) {
            log.error("Error al asignar el rol: [{}]", e.getMessage());
        }

        if (!hasRole) {
            Role role = rolePort.findByName(CompanyInitialEnum.BFS.getValue().toUpperCase() + " - " + defaultRole);
            authUser.setRoleId(role.getId());
        }
    }

    //#endregion

}
