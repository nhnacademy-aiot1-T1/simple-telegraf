package com.nhnacademy.aiotone.measurement;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.influxdb.annotations.Column;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.time.Instant;

@Getter
public abstract class BaseMeasurement {

    protected BaseMeasurement() {
    }

    @Column(tag = true)
    private String site;

    @Column(tag = true)
    private String branch;

    @Column(tag = true)
    private String place;

    @Column(tag = true)
    private String device;

    @Column(tag = true)
    private String element;

    @Setter
    @Column(timestamp = true)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant time;

    public abstract Object getValue();

    static class InstantDeserializer extends StdDeserializer<Instant> {
        public InstantDeserializer() {
            super(Instant.class);
        }

        @Override
        public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return Instant.ofEpochMilli(jsonParser.getValueAsLong());
        }
    }
}
