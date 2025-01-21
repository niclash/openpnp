package org.openpnp.serialization;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.opencv.core.Mat;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.ThreadPoolExecutor;

public class XmlObjectMapperSerializer {
    XmlMapper mapper;

    public XmlObjectMapperSerializer() {
        JacksonXmlModule module = new JacksonXmlModule();
        module.addSerializer(Color.class, new StdColorSerializer());
        module.addDeserializer(Color.class, new StdColorDeserializer());

        module.setDeserializerModifier(PostDeserializeAnnotationDeserializer.modifier);
        module.setSerializerModifier(PreSerializeAnnotationSerializer.modifier);

        mapper = new XmlMapper(module);
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);

        mapper.addMixIn(Mat.class, MatExclusionMixin.class);
        mapper.addMixIn(ThreadPoolExecutor.class, ThreadPoolExecutorExclusionMixin.class);
        mapper.addMixIn(BufferedImage.class, BufferedImageExclusionMixin.class);
    }

    public <T> T read(Class<? extends T> type, String source) throws Exception {
        return mapper.readValue(source, type);
    }

    public <T> T read(Class<? extends T> type, File source) throws Exception {
        System.out.println(source.getAbsolutePath());
        return mapper.readValue(source, type);
    }

    public <T> T read(Class<? extends T> type, InputStream source) throws Exception {
        return mapper.readValue(source, type);
    }

    public <T> T read(Class<? extends T> type, Reader source) throws Exception {
        return mapper.readValue(source, type);
    }

    public void write(Object source, File out) throws Exception {
        mapper.writeValue(out, source);
    }

    public void write(Object source, OutputStream out) throws Exception {
        mapper.writeValue(out, source);
    }

    public void write(Object source, Writer out) throws Exception {
        mapper.writeValue(out, source);
    }

    public String writeAsString( Object source ) throws Exception {
        return mapper.writeValueAsString(source);
    }

    @JsonIgnoreType
    abstract static class BufferedImageExclusionMixin {
    }

    @JsonIgnoreType
    abstract static class ThreadPoolExecutorExclusionMixin {
    }

    @JsonIgnoreType
    abstract static class MatExclusionMixin {
    }

    public static class StdColorDeserializer extends JsonDeserializer<Color> {

        @Override
        public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            if (!(p instanceof FromXmlParser)) {
                throw new UnsupportedOperationException("StdColorDeserializer can only be used with XML serialization");
            }
            int red = 0, green = 0, blue = 0, alpha = 0;
            while (p.nextToken() == JsonToken.FIELD_NAME) {
                String attr = p.getValueAsString();
                p.nextToken();  // value field
                int colorValue = Integer.parseInt(p.getValueAsString());
                switch (attr) {
                    case "r":
                        red = colorValue;
                        break;
                    case "g":
                        green = colorValue;
                        break;
                    case "b":
                        blue = colorValue;
                        break;
                    case "a":
                        alpha = colorValue;
                        break;
                }
            }
            return new Color(red, green, blue, alpha);
        }
    }

    public static class StdColorSerializer extends JsonSerializer<Color> {

        @Override
        public void serialize(Color value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            if (!(gen instanceof ToXmlGenerator)) {
                throw new UnsupportedOperationException("StdColorSerializer can only be used with XML serialization");
            }
            ToXmlGenerator xmlGen = (ToXmlGenerator) gen;
            xmlGen.writeStartObject();
            xmlGen.writeStringField("r", String.valueOf(value.getRed()));
            xmlGen.writeStringField("g", String.valueOf(value.getGreen()));
            xmlGen.writeStringField("b", String.valueOf(value.getBlue()));
            xmlGen.writeStringField("a", String.valueOf(value.getAlpha()));
            xmlGen.writeEndObject();
        }
    }

    public static class ParentColorSerializer extends StdSerializer<Color> {

        public ParentColorSerializer() {
            super(Color.class);
        }

        @Override
        public void serialize(Color color, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if (!(gen instanceof ToXmlGenerator)) {
                throw new UnsupportedOperationException("ColorSerializer can only be used with XML serialization");
            }

            ToXmlGenerator xmlGen = (ToXmlGenerator) gen;
            xmlGen.setNextName(new QName(null, "r"));

            xmlGen.setNextIsAttribute(true);
            xmlGen.writeNumber(color.getRed());

            xmlGen.setNextIsAttribute(true);
            xmlGen.writeNumberField("g", color.getGreen());

            xmlGen.setNextIsAttribute(true);
            xmlGen.writeNumberField("b", color.getBlue());

            xmlGen.setNextIsAttribute(true);
            xmlGen.writeNumberField("a", color.getAlpha());
        }

    }

    public static class ParentColorDeserializer extends StdDeserializer<Color> {

        ParentColorDeserializer() {
            super(Color.class);
        }

        @Override
        public Color deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            XMLStreamReader staxReader = ((FromXmlParser) p).getStaxReader();
            int red = colorValueOf(staxReader, "r");
            int green = colorValueOf(staxReader, "g");
            int blue = colorValueOf(staxReader, "b");
            int alpha = colorValueOf(staxReader, "a");
            return new Color(red, green, blue, alpha);
        }

        private int colorValueOf(XMLStreamReader reader, String value) {
            String attributeValue = reader.getAttributeValue(null, value);
            if (attributeValue != null) {
                return Integer.parseInt(attributeValue);
            }
            return 0;
        }
    }
}
