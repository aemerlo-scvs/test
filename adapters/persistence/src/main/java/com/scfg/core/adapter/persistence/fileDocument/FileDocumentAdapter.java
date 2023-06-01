package com.scfg.core.adapter.persistence.fileDocument;

import com.scfg.core.application.port.out.FileDocumentPort;
import com.scfg.core.common.PersistenceAdapter;
import com.scfg.core.common.enums.PolicyStatusEnum;
import com.scfg.core.common.enums.TypesAttachmentsEnum;
import com.scfg.core.common.enums.PersistenceStatusEnum;
import com.scfg.core.common.util.ModelMapperConfig;
import com.scfg.core.domain.FileDocument;
import com.scfg.core.domain.dto.FileDocumentDTO;
import com.scfg.core.domain.dto.credicasas.FileDocumentByRequestDTO;
import com.scfg.core.domain.GeneralRequest;
import org.modelmapper.ModelMapper;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@PersistenceAdapter
@RequiredArgsConstructor
public class FileDocumentAdapter implements FileDocumentPort {
    private final FileDocumentRepository fileDocumentRepository;

    @Override
    public List<FileDocument> findAll() {
        return null;
    }

    @Override
    public FileDocument findById(Long fileId) {
        FileDocumentJpaEntity file = fileDocumentRepository.findById(fileId).orElse(null);
        return file != null ? mapToDomain(file) : null;
    }

    @Override
    public FileDocument SaveOrUpdate(FileDocument fileDocument) {
        FileDocumentJpaEntity fileDocumentJpaEntity = mapToEntity(fileDocument);
        fileDocumentJpaEntity = fileDocumentRepository.save(fileDocumentJpaEntity);
        FileDocument fileDocument1= mapToDomain(fileDocumentJpaEntity);
        return fileDocument1;
    }

    @Override
    public List<FileDocumentDTO> SaveOrUpdateAll(List<FileDocumentDTO> fileDocument) {
        List<FileDocumentJpaEntity> fileDocumentJpaEntities = mapToJpaEntityListCLF(fileDocument);
        fileDocumentJpaEntities = fileDocumentRepository.saveAll(fileDocumentJpaEntities);
        return mapToDomainListCLF(fileDocumentJpaEntities);
    }
    @Override
    public List<FileDocument> findAllByPolicyItemId(Long policyItemId) {
        List<FileDocumentJpaEntity> fileDocumentJpaEntities = fileDocumentRepository.getFileDocumentsByPolicyItemId(policyItemId, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());
        return fileDocumentJpaEntities.stream().map(o -> new ModelMapper().map(o, FileDocument.class)).collect(Collectors.toList());
    }

    @Override
    public List<FileDocumentByRequestDTO> findAllDocumentsByRequestId(Long requestId) {
        try {
            List<FileDocumentByRequestDTO> fileDocumentByRequestDTOList = fileDocumentRepository.findAllDocumentsByRequestId(requestId);
            return fileDocumentByRequestDTOList;
        } catch (Exception e) {
            System.out.println("Error al obtener los documentos: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<FileDocumentByRequestDTO> findAllSignedDocumentsByRequestId(Long requestId) {
        try {
            List<FileDocumentByRequestDTO> fileDocumentByRequestDTOList = fileDocumentRepository.findAllSignedDocumentsByRequestId(requestId);
            return fileDocumentByRequestDTOList;
        } catch (Exception e) {
            System.out.println("Error al obtener los documentos: " + e.getMessage());
            return null;
        }
    }

    @Override
    public FileDocument findLastByPolicyItemIdAndDocumentTypeIdc(Long policyItemId, Integer documentTypeIdc) {
        List<FileDocumentJpaEntity> list = fileDocumentRepository.findAllDocumentsByPolicyItemIdAndDocumentTypeIdc(
                policyItemId, documentTypeIdc, PersistenceStatusEnum.CREATED_OR_UPDATED.getValue());

        if (list.isEmpty()) {
            return null;
        }

        return new ModelMapperConfig().getStrictModelMapper().map(list.get(0), FileDocument.class);
    }

    @Override
    public long deleteFileDocument(Long fileDocumentId) {
        FileDocumentJpaEntity fileDocumentJpaEntity = fileDocumentRepository.findById(fileDocumentId).orElse(null);
        fileDocumentJpaEntity.setStatus(PersistenceStatusEnum.DELETED.getValue());
        return fileDocumentRepository.save(fileDocumentJpaEntity).getId();
    }

    @Override
    public List<FileDocumentByRequestDTO> getCertificateCoverageDocumentByPolicyItemId(Long policyItemId) {
        try {
            return fileDocumentRepository.getCertificateCoverageDocumentByPolicyItemId(
                    policyItemId, TypesAttachmentsEnum.COVERAGECERTIFICATE.getValue(), PolicyStatusEnum.ACTIVE.getValue());
        } catch (Exception e) {
            System.out.println("Error al obtener los documentos: " + e.getMessage());
            return null;
        }
    }

    private FileDocument mapToDomain(FileDocumentJpaEntity fileDocumentJpaEntity) {
        FileDocument fileDocument=FileDocument.builder()
                .id(fileDocumentJpaEntity.getId())
                .description(fileDocumentJpaEntity.getDescription())
                .typeDocument(fileDocumentJpaEntity.getTypeDocument())
                .directoryLocation(fileDocumentJpaEntity.getDirectoryLocation())
                .mime(fileDocumentJpaEntity.getMimeType())
                .content(fileDocumentJpaEntity.getContent())
                .createdAt(fileDocumentJpaEntity.getCreatedAt())
                .lastModifiedAt(fileDocumentJpaEntity.getLastModifiedAt())
                .cite(fileDocumentJpaEntity.getCite())
                .build();
        return fileDocument;
    }

    private FileDocumentJpaEntity mapToEntity(FileDocument fileDocument) {
        FileDocumentJpaEntity jpaEntity=FileDocumentJpaEntity.builder()
                .id(fileDocument.getId())
                .description(fileDocument.getDescription())
                .typeDocument(fileDocument.getTypeDocument())
                .directoryLocation(fileDocument.getDirectoryLocation())
                .mimeType(fileDocument.getMime())
                .content(fileDocument.getContent())
                .createdAt(fileDocument.getCreatedAt())
                .lastModifiedAt(fileDocument.getLastModifiedAt())
                .cite(fileDocument.getCite())
                .build();
        return jpaEntity;
    }

    private List<FileDocumentJpaEntity> mapToJpaEntityListCLF(List<FileDocumentDTO> lis) {
        List<FileDocumentJpaEntity> jpaEntityList = new ArrayList<>();
        lis.forEach(list -> {
            jpaEntityList.add(mapToJpaEntityCLF(list));
        });
        return jpaEntityList;
    }

    private FileDocumentJpaEntity mapToJpaEntityCLF(FileDocumentDTO o) {
        FileDocumentJpaEntity fileDocumentJpaEntity = FileDocumentJpaEntity.builder()
                .id(o.getId())
                .description(o.getName())
                .typeDocument(o.getTypeId())
                .mimeType(o.getMime())
                .content(o.getContent())
                .cite(o.getCite())
                .build();
        return fileDocumentJpaEntity;
    }

    private List<FileDocumentDTO> mapToDomainListCLF(List<FileDocumentJpaEntity> lis) {
        List<FileDocumentDTO> dtoList = new ArrayList<>();
        lis.forEach(list -> {
            dtoList.add(mapToDomainCLF(list));
        });
        return dtoList;
    }

    private FileDocumentDTO mapToDomainCLF(FileDocumentJpaEntity o) {
        FileDocumentDTO fileDocumentJpaEntity = FileDocumentDTO.builder()
                .id(o.getId())
                .name(o.getDescription())
                .mime(o.getMimeType())
                .content(o.getContent())
                .typeId(o.getTypeDocument())
                .cite(o.getCite())
                .build();
        return fileDocumentJpaEntity;
    }
}
