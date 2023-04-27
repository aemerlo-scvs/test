package com.scfg.core.adapter.persistence.client;

import com.scfg.core.adapter.persistence.classifier.ClassifierJpaEntity;
import com.scfg.core.application.port.out.ClientPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.ActionRequestEnum;
import com.scfg.core.common.enums.ClassifierEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.exception.NotDataFoundException;
import com.scfg.core.common.util.PersistenceResponse;
import com.scfg.core.domain.common.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.scfg.core.common.util.HelpersConstants.*;
import static com.scfg.core.common.util.HelpersMethods.*;

@PersistenceAdapter
@RequiredArgsConstructor
public class ClientPersistenceAdapter implements ClientPort {

    private final ClientRepository clientRepository;

    @Override
    public List<Client> getAllClients() {
        Object clientList = clientRepository.findAll();
        return (List<Client>) clientList;
    }

    @Override
    public Client getClientById(long clientId) {
        ClientJpaEntity client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new NotDataFoundException("Client: " + clientId + " Not found")
                );
        return mapToDomain(client, false);
    }

    @Override
    public Client getClientByName(String clientName) {
        // corregir esta seccion
        StringTokenizer clientTokenizer = new StringTokenizer(clientName);

        ClientJpaEntity client = clientRepository.findByNamesAndLastNameAndMothersLastName(
                clientTokenizer.nextToken(), // lastname
                clientTokenizer.nextToken(), // mother lastname
                clientTokenizer.nextToken()) // names
                .orElseThrow(() ->
                        new NotDataFoundException("Client Name: " + clientName + " Not found")
                );

        return mapToDomain(client, false);
    }

    @Override
    public Client getClientByCI(String clientCI) {
        Page<ClientJpaEntity> pages = clientRepository.findByDocumentNumber(clientCI, PageRequest.of(0, 1));
        Optional<ClientJpaEntity> clientJpaEntityOptional = pages.stream().findFirst();
        ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.isPresent()
                ? clientJpaEntityOptional.get()
                : null;
        if (isNull(clientJpaEntity)) {
            clientJpaEntity = new ClientJpaEntity();
        }
        //ClientJpaEntity client = clientRepository.findByDocumentNumber(clientCI, PageRequest.of(0, 1));
        return mapToDomain(clientJpaEntity, false);
                /*.orElseThrow(() ->
                        new ClientNotFoundException("Client: " + clientCI + " Not found")
                );*/
        //return client.isPresent() ? mapToDomain(client.get()) : null;
    }

    @Override
    public PersistenceResponse save(Client client, boolean returnEntity) {
        ClientJpaEntity clientJpaEntity = mapToJpaEntity(client);
        try {
            clientJpaEntity = clientRepository.save(clientJpaEntity);
        } catch (Exception ex) {
            throw ex;
        }
        return new PersistenceResponse(
                Client.class.getSimpleName(),
                ActionRequestEnum.CREATE,
                returnEntity ? clientJpaEntity : null
        );
    }

    @Override
    public PersistenceResponse update(Client client) {
        ClientJpaEntity clientJpaEntity = mapToJpaEntity(client);
        clientJpaEntity = clientRepository.save(clientJpaEntity);
        return new PersistenceResponse(
                Client.class.getSimpleName(),
                ActionRequestEnum.UPDATE,
                clientJpaEntity
        );
    }

    @Override
    public PersistenceResponse delete(Client client) {
        ClientJpaEntity clientJpaEntity = mapToJpaEntity(client);
        // status for deleted
        clientJpaEntity.setStatus(PersistenceStatusEnum
                .DELETED
                .getValue());
        clientRepository.save(clientJpaEntity);

        return new PersistenceResponse(
                Client.class.getSimpleName(),
                ActionRequestEnum.DELETE,
                null
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public Map<String, Object> findOrUpsert(Client clientDomain) throws NoSuchFieldException, IllegalAccessException {
        Map<String, Object> result = new HashMap<>();
        Page<ClientJpaEntity> pages = clientRepository.findByDocumentNumber(clientDomain.getDocumentNumber(), PageRequest.of(0, 1));
        Optional<ClientJpaEntity> clientJpaEntityOptional = pages.stream().findFirst();
        ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.isPresent()
                ? clientJpaEntityOptional.get()
                : null;
        if (isNull(clientJpaEntity)) {
            clientJpaEntity = new ClientJpaEntity();
            result.put(KEY_ACTION_ENTITY, CREATE_ACTION);
        } else {
            result.put(KEY_ACTION_ENTITY, UPDATE_ACTION);
        }
        mergeValues(clientJpaEntity, clientDomain);
        clientJpaEntity = clientRepository.saveAndFlush(clientJpaEntity);

        result.put(KEY_CONTENT_ENTITY, mapToDomain(clientJpaEntity, false));
        return result;

        //mapToJpaEntityForUpsert(null, client);
        /*ClientJpaEntity clientJpaEntity = clientRepository.findByDocumentNumber(client.getDocumentNumber())
                .orElseGet(() -> {
                    try {
                        ClientJpaEntity x = clientRepository.saveAndFlush(mapToJpaEntity(client));
                        return x;
                    } catch (Exception e) {
                        String er = e.getMessage();
                    }
                    return null;
                });*/


    }

    /**
     * Update accumulted amount
     * @param policyTypeReferenceId
     * @param clientDocumentNumber
     * @param accumulatedAmount
     */
    @Transactional
    @Override
    public void updateAccumulatedAmount(long policyTypeReferenceId, String clientDocumentNumber, Double accumulatedAmount) {
        if (policyTypeReferenceId == ClassifierEnum.RegulatedDH_PolicyType
                .getReferenceCode()){
            clientRepository.updateAccumulatedAmountDhl(clientDocumentNumber, accumulatedAmount);
        } else {
            clientRepository.updateAccumulatedAmountDhn(clientDocumentNumber, accumulatedAmount);

        }
    }


    /***
     * Persist model in database
     * @param client
     * @return
     */
    public static ClientJpaEntity mapToJpaEntity(Client client) {
        ClassifierJpaEntity documentType = ClassifierJpaEntity.builder()
                .id(client.getDocumentTypeId())
                .build();

        ClassifierJpaEntity currency = ClassifierJpaEntity.builder()
                .id(client.getCurrencyId())
                .build();

        ClientJpaEntity clientJpaEntity = ClientJpaEntity.builder()
                .id(client.getId())
                .names(client.getNames())
                .lastName(client.getLastName())
                .mothersLastName(client.getMothersLastName())
                .marriedLastName(client.getMarriedLastName())
                .duplicateCopy(client.getDuplicateCopy())
                .extension(client.getExtension())
                .birthDate(client.getBirthDate())
                .nationality(client.getNationality())
                .documentNumber(client.getDocumentNumber())
                .gender(client.getGender())
                .accumulatedAmountDhl(client.getAccumulatedAmountDhl())
                .accumulatedAmountDhn(client.getAccumulatedAmountDhn())
                // for relationship
                .documentType(documentType)
                .currency(currency)
                .build();

        return clientJpaEntity;

    }

    /***
     * Export domain for consume APIs
     * @param clientJpaEntity
     * @return
     */
    public static Client mapToDomain(
            ClientJpaEntity clientJpaEntity,
            boolean withRelations) {

        return Client.builder()
                .id(clientJpaEntity.getId())
                .names(clientJpaEntity.getNames())
                .lastName(clientJpaEntity.getLastName())
                .mothersLastName(clientJpaEntity.getMothersLastName())
                .marriedLastName(clientJpaEntity.getMarriedLastName())
                .duplicateCopy(clientJpaEntity.getDuplicateCopy())
                .extension(clientJpaEntity.getExtension())
                .birthDate(clientJpaEntity.getBirthDate())
                .nationality(clientJpaEntity.getNationality())
                .documentNumber(clientJpaEntity.getDocumentNumber())
                .gender(clientJpaEntity.getGender())
                .accumulatedAmountDhl(clientJpaEntity.getAccumulatedAmountDhl())
                .accumulatedAmountDhn(clientJpaEntity.getAccumulatedAmountDhn())
                // relationship
                .documentTypeId(withRelations
                        ? clientJpaEntity.getDocumentType().getId()
                        : null)
                .currencyId(withRelations
                        ? clientJpaEntity.getCurrency().getId()
                        : null)
                // base entity
                .createdAt(clientJpaEntity.getCreatedAt())
                .lastModifiedAt(clientJpaEntity.getLastModifiedAt())
                .build();
    }
}
