package com.scfg.core.adapter.persistence.VCMA;

import com.scfg.core.adapter.persistence.VCMA.models.ZoneJpaEntity;
import com.scfg.core.adapter.persistence.VCMA.repository.ZoneRepository;
import com.scfg.core.application.port.out.ZonePort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.domain.managers.Zone;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PersistenceAdapter
@RequiredArgsConstructor
public class ZonePersistenceAdapter implements ZonePort {

    @Autowired
    private ZoneRepository zoneRepository;

    public ZoneRepository getZoneRepository() {
        return zoneRepository;
    }

    public void setZoneRepository(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public List<Zone> getAllZone() {
        try {
            Object zoneList = zoneRepository.findAll();
            return (List<Zone>) zoneList;
        } catch (Exception e){
            return null;
        }
    }

    public synchronized ZoneJpaEntity insertZone(ZoneJpaEntity mg){
        try {
            return zoneRepository.save(mg);
        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    //#region Mapper
    public ZoneJpaEntity mapToJpaEntity(Zone zone) {
        System.out.println("Acabo de entrar aqui con\n"+zone);
        return new ZoneJpaEntity(
                zone.getZONES_ID() == null ? null : zone.getZONES_ID(),
                zone.getDESCRIPTION(),
                zone.getUSER_CREATE(),
                zone.getUSER_UPDATE(),
                zone.getDATE_CREATE(),
                zone.getDATE_UPDATE(),
                zone.getSTATUS()
        );
    }

    private Zone mapToDomain(ZoneJpaEntity zoneJpaEntity){
        return new Zone(
                zoneJpaEntity.getZONES_ID() == null ? null : zoneJpaEntity.getZONES_ID(),
                zoneJpaEntity.getDESCRIPTION(),
                zoneJpaEntity.getUSER_CREATE(),
                zoneJpaEntity.getUSER_UPDATE(),
                zoneJpaEntity.getDATE_CREATE(),
                zoneJpaEntity.getDATE_UPDATE(),
                zoneJpaEntity.getSTATUS()
        );
    }
//#endregion
}
