package ru.netology.cloudservicediplom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NewFileNameDTO {

    @JsonProperty("filename")
    private String newName;
}
