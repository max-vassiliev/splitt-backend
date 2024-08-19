package com.example.splitt.bill.dto.validation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class StrictIntegerDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
        double value = jsonParser.getDoubleValue();

        if (value % 1 != 0) {
            throw context.weirdNumberException(value, Integer.class,
                    "Expected an integer value, but got a floating-point value.");
        }

        return (int) value;
    }
}
