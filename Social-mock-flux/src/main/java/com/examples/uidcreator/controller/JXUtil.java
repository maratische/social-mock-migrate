package com.examples.uidcreator.controller;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class JXUtil {

    /**
     * Mappers are thread safe so we can have a single oen for
     * all json activity
     */
    private static final ObjectMapper commonMapper = commonMapperSetup();
//    private static final ObjectMapper betterMapper = betterMapperSetup();
//    private static final ObjectMapper isoDateFormatMapper = isoDateFormatMapperSetup();

//    private static final ObjectMapper noIndentMapper = createNoIndentMapper();

    /**
     * Deserialize a json string to its object representation
     */
    public static <T> T fromJson(String json, Class clazz) {
        try {
            return (T) commonMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz, ObjectMapper customObjectMapper) {
        try {
            return customObjectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz) {
        return fromJsonArray(json, clazz, commonMapper);
    }

    public static <T> List<T> fromJsonArray(String json, Class<T> clazz, ObjectMapper mapper) {
        if (!StringUtils.hasValue(json)) {
            return Collections.emptyList();
        }

        try {
            return (List<T>) mapper.readValue(
                    json,
                    mapper.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static TypeFactory getTypeFactory() {
        return commonMapper.getTypeFactory();
    }

    public static <T> T fromJson(String json, JavaType type) {
        try {
            return commonMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deserialize a json string to a HashMap of maps/arrays
     */
    public static Map fromJsonToMap(String json) {

        return fromJsonToMap(json, commonMapper);

    }

    /**
     * Deserialize a json string to a HashMap of maps/arrays
     */
    public static Map fromJsonToMap(String json, ObjectMapper mapper) {

        if (StringUtils.isEmpty(json))
            return new HashMap();

        try {
            return mapper.readValue(json, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Deserialize a json input stream to a HashMap of maps/arrays
     */
    public static Map fromJsonToMap(InputStream in) {

        return fromJsonToMap(in, commonMapper);

    }

    /**
     * Deserialize a json input stream to a HashMap of maps/arrays
     */
    public static Map fromJsonToMap(InputStream in, ObjectMapper mapper) {

        try {
            return mapper.readValue(in, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Serialize an object to its JSON representation
     */
    public static String toJson(Object o) {
        return toJson(o, commonMapper);
    }

    /**
     * Serialize an object to its JSON representation with sanitizing all input data
     */
//    public static String toSafeJson(Object o) {
//        return toSafeJson(o, commonMapper);
//    }

    /**
     * Serialize an object to its JSON representation with sanitizing all input data
     */
//    public static String toSafeJson(Object o, ObjectMapper mapper) {
//        return JsonSanitizer.sanitize(toJson(o, mapper));
//    }

//    public static String createVariable(String name, Object o) {
//        return "var " + name + " = " + toSafeJson(o) + ";";
//    }

    /**
     * Serialize an object to its JSON representation
     */
    public static String toJson(Object o, ObjectMapper mapper) {
        if (o == null) return null;

        if (mapper == null)
            mapper = commonMapper;

        try {
            return mapper.writeValueAsString(o);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Write out stream with a custom mapper
     *
     * @param out
     * @return
     */
    public static void toJson(Object o, OutputStream out, ObjectMapper mapper) {
        try {
            mapper.writeValue(out, o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param out
     * @return
     */
    public static void toJson(Object o, OutputStream out) {
        try {
            commonMapper.writeValue(out, o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts an object to its map representation using Jackson
     */
    public static Map toJsonMap(Object o) {
        try {
            return commonMapper.convertValue(o, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts an object to its map representation using Jackson
     */
    public static Map toJsonMap(Object o, ObjectMapper mapper) {
        if (mapper == null)
            mapper = commonMapper;

        try {
            return mapper.convertValue(o, Map.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Deserialzies a map to it's object representation
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJsonMap(Map map, Class clazz) {
        return (T) commonMapper.convertValue(map, clazz);
    }

    public static <T> T fromJsonMap(Map map, Class<T> clazz, ObjectMapper customMapper) {
        return customMapper.convertValue(map, clazz);
    }

    /**
     * This mapper should be private and never exposed outside
     * of this class.  ObjectMappers are thread-safe
     * so we want to keep a single instance and protect
     * modification of the mapper configuration
     */
    private static ObjectMapper commonMapperSetup() {
        ObjectMapper mapper = new ObjectMapper();
        //        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // to prevent exception when encountering unknown property:
        //        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // to allow coercion of JSON empty String ("") to null Object value:
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        //        mapper.enable(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS);
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // to allow (non-standard) unquoted field names in JSON:
        //        mapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        // to allow use of apostrophes (single quotes), non standard
        //        mapper.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        return mapper;
    }

    public static ObjectMapper commonMapper() {
        return commonMapper;
    }

//    public static ObjectMapper betterMapper() {
//        return betterMapper;
//    }

//    public static ObjectMapper isoDateFormatMapper() {
//        return isoDateFormatMapper;
//    }

    private static ObjectMapper betterMapperSetup() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

//    private static ObjectMapper isoDateFormatMapperSetup() {
//        ObjectMapper mapper = betterMapper.copy();
//        mapper.setDateFormat(new SimpleDateFormat(ISO_WITH_TIMEZONE_FORMAT_NO_MILLIS));
//        return mapper;
//    }

    /**
     * This is a temporary mapper that extends the common mapper
     * If all goes well, we will roll the escape non ascii into the common mapper
     * and remove this mapper.
     */
    public static ObjectMapper escapeNonAsciiMapper() {
        ObjectMapper mapper = commonMapper.copy();
        mapper.getFactory().enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);

        return mapper;
    }

    /**
     * This workaround gives us the ideal mapper
     * where dates are serialized as seconds since 1970
     */
    public static ObjectMapper secondsMapper() {
        ObjectMapper mapper = betterMapperSetup();
        mapper.getFactory().enable(JsonGenerator.Feature.ESCAPE_NON_ASCII);

        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new JsonSerializer<Date>() {
            @Override
            public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeNumber(value.getTime() / 1000L);
            }
        });
        module.addDeserializer(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                return new Date(jp.getNumberValue().longValue() * 1000L);
            }
        });
        mapper.registerModule(module);

        return mapper;
    }

    /**
     * These special mappers decorate the original common mapper
     * Enables the pretty print feature
     */
    public static ObjectMapper indentMapper() {
        ObjectMapper mapper = commonMapper.copy();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    /**
     * These special mappers decorate the original common mapper.
     * Disables the pretty print feature
     */
//    public static ObjectMapper noIdentMapper() {
//        return noIndentMapper;
//    }
    private static ObjectMapper createNoIndentMapper() {
        ObjectMapper mapper = commonMapper.copy();
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    /**
     * These special mappers decorate the original common mapper
     * Changes GMT time zone to DEFAULT_DATETIMEZONE (EST)
     */
//    public static ObjectMapper timeZoneMapper() {
//        ObjectMapper mapper = commonMapper.copy();
//
//        DateFormat dateFormat = new StdDateFormat(DEFAULT_DATETIMEZONE.toTimeZone(), Locale.getDefault());
//        mapper.setDateFormat(dateFormat);
//
//        return mapper;
//    }

//    public static boolean isValidJSON(final String json) {
//
//        try {
//            commonMapper.readTree(json);
//            return true;
//        } catch (Exception e) {
//            Log.get().trace("Json is not valid", e);
//        }
//
//        return false;
//    }

}
