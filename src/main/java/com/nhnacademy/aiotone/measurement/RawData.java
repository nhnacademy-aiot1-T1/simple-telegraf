package com.nhnacademy.aiotone.measurement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RawData {
    private Long time;

    @JsonProperty("g")
    private String gateway;

    @JsonProperty("m")
    private String motor;

    @JsonProperty("c")
    private Integer channel;

    private Double[] values;
}
