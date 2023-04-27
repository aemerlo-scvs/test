package com.scfg.core.domain.common;

import com.scfg.core.common.enums.BusinessGroupEnum;
import com.scfg.core.common.enums.CompanyInitialEnum;
import com.scfg.core.common.exception.OperationException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.Collection;
import java.util.List;

@SuperBuilder
public class AuthUser extends User implements UserDetails {

    @Getter @Setter
    private List<Menu> menuList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //#region Constructor

    public AuthUser(Attributes adAttrs, BfsAuthenticationRequest bfsData) {
        try {
            this.setBfsUserCode(bfsData.getCode());
            this.setBfsAgencyCode(bfsData.getOfficeCode());
            this.setEmail(adAttrs.get("mail").get().toString());
            this.setName(adAttrs.get("givenname").get().toString());
            this.setSurName(adAttrs.get("sn").get().toString());
            this.setUserName(adAttrs.get("sAMAccountName").get().toString());
            this.setPassword("");
            this.setGenderIdc(0L);

            if (bfsData.getCompany().equals(CompanyInitialEnum.SCVS.getValue())) {
                this.setCompanyIdc((long) BusinessGroupEnum.SCVS.getValue());
            }
            if (bfsData.getCompany().equals(CompanyInitialEnum.BFS.getValue())) {
                this.setCompanyIdc((long) BusinessGroupEnum.BFS.getValue());
            }

        } catch (NamingException e) {
            throw new OperationException("No se pudo obtener el atributo del directorio activo");
        }
    }

    //#endregion

}
