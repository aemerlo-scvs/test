package com.scfg.core.domain.dto;

//import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
public class FileDocumentDTO implements Serializable {
    private static final long serialVersionUID = 5441616027440750296L;
    private Long id;
    private Integer typeId;
    @NotEmpty(message = "name cannot be empty")
    private String name;
    @NotEmpty(message = "content cannot be empty")
    private String content;
    private String mime;
    private String path;
    private Integer cite;

    public FileDocumentDTO(Long id, Integer typeId, @NotEmpty(message = "name cannot be empty") String name, byte[] content, String mime, String path, Integer cite) {
        this.id = id;
        this.typeId = typeId;
        this.name = name;
        this.content = Base64.getEncoder().encodeToString(content);
        this.mime = mime;
        this.path = path;
    }
}
