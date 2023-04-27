package com.scfg.core.adapter.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scfg.core.adapter.persistence.user.UserJpaEntity;
import com.scfg.core.application.port.out.ChangeLogPort;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.RequestInfo;
import com.scfg.core.domain.common.ChangeLog;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

import static com.scfg.core.common.util.HelpersMethods.isNull;

@Component
@RequiredArgsConstructor
public class ChangeLogListener {

    private static ChangeLogPort changeLogPort;

    private static RequestInfo requestInfo;

    String jsonObjectAux = "";

    @Autowired
    public void setChangeLogPort(ChangeLogPort changeLogPort) {
        this.changeLogPort = changeLogPort;
    }

    @Autowired
    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    private String ip;

    {
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @PrePersist
    public void prePersist(final BaseJpaEntity entity) throws JsonProcessingException {

        entity.setStatus(PersistenceStatusEnum
                .CREATED_OR_UPDATED
                .getValue());

        ObjectMapper mapper = new ObjectMapper();
        if (entity != null) {
            jsonObjectAux = mapper.writeValueAsString(entity);
        }
    }

    @PreUpdate
    public void preUpdate(final BaseJpaEntity entity) throws JsonProcessingException {

        if (isNull(entity.getStatus())) {
            entity.setStatus(PersistenceStatusEnum
                    .CREATED_OR_UPDATED
                    .getValue());
        }

        ObjectMapper mapper = new ObjectMapper();
        if (entity != null) {
            jsonObjectAux = mapper.writeValueAsString(entity);
        }
    }

    @PostPersist
    public void postPersistChangeLog(final BaseJpaEntity entity) {

        String action = (entity.getStatus() == PersistenceStatusEnum.CREATED_OR_UPDATED.getValue()) ? "AGREGAR" : "MODIFICAR";
        UserAgent userAgent = UserAgent.parseUserAgentString(requestInfo.getUserAgent());
        String userId = requestInfo.getUserId();
        String table = entity.getClass().getSimpleName().replace("JpaEntity", "");

        if (userId == null) {
            userId = "0";
        }

        ChangeLog changeLog = ChangeLog.builder()
                .id(0L)
                .action(action)
                .table(table)
                .createdAt(LocalDateTime.now())
                .referenceId(entity.getId())
                .referenceObject(jsonObjectAux)
                .browser(userAgent.getBrowser().getName())
                .ip(ip)
                .userId(Long.parseLong(userId))
                .build();

         // changeLogPort.save(changeLog);
    }

    @PostUpdate
    public void postUpdateChangeLog(final BaseJpaEntity entity) {

        String action = (entity.getStatus() == PersistenceStatusEnum.DELETED.getValue()) ? "ELIMINAR" : "MODIFICAR";
        UserAgent userAgent = UserAgent.parseUserAgentString(requestInfo.getUserAgent());
        String userId = requestInfo.getUserId();
        String table = entity.getClass().getSimpleName().replace("JpaEntity", "");

        if (userId == null) {
            userId = "0";
            if (entity instanceof UserJpaEntity) { //El usuario está iniciando sesion
                userId = entity.getId() + "";
                action = "INICIO DE SESION";
            }
        }

        ChangeLog changeLog = ChangeLog.builder()
                .id(0L)
                .action(action)
                .table(table)
                .createdAt(LocalDateTime.now())
                .referenceId(entity.getId())
                .referenceObject(jsonObjectAux)
                .browser(userAgent.getBrowser().getName())
                .ip(ip)
                .userId(Long.parseLong(userId))
                .build();

         // changeLogPort.save(changeLog);
    }
}
