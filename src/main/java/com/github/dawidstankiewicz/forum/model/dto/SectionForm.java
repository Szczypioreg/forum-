package com.github.dawidstankiewicz.forum.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionForm {

    private Integer id;
    @Size(min = 3)
    private String name;
    private String description;
    private String imageFilename;

}
