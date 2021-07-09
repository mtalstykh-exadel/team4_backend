package com.team4.testingsystem.serializers;

import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.team4.testingsystem.entities.User;

import java.io.IOException;

public class UserSerializer extends JsonSerializer<User> {
    @Override
    public void serialize(User user, JsonGenerator jsonGen,
                          SerializerProvider serProv) throws IOException{
        jsonGen.writeStartObject();
        jsonGen.writeNumberField("userId", user.getId());
        jsonGen.writeStringField("username", user.getName());
        jsonGen.writeEndObject();
    }
}