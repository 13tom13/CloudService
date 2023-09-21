package ru.netology.cloudservicediplom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class Token {
    @JsonProperty("auth-token")
    String token;
}
