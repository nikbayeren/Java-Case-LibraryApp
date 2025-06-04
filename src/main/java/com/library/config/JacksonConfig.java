package com.library.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Year;

@Configuration
public class JacksonConfig {
    
    @Bean
    public SimpleModule yearModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Year.class, new CustomYearSerializer());
        module.addDeserializer(Year.class, new CustomYearDeserializer());
        return module;
    }
    
    public static class CustomYearSerializer extends JsonSerializer<Year> {
        @Override
        public void serialize(Year year, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (year != null) {
                gen.writeNumber(year.getValue());
            }
        }
    }
    
    public static class CustomYearDeserializer extends JsonDeserializer<Year> {
        @Override
        public Year deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return Year.of(p.getValueAsInt());
        }
    }
} 