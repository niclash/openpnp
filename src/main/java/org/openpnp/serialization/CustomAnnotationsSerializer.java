package org.openpnp.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class CustomAnnotationsSerializer extends StdSerializer<Object> {

    private final JsonSerializer<Object> delegate;

    public CustomAnnotationsSerializer(JsonSerializer<Object> delegate, BeanDescription beanDesc) {
        super(Object.class);
        this.delegate = delegate;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        callPreSerializeMethods(value);
        delegate.serialize(value, gen, provider);
    }

    private void callPreSerializeMethods(Object deserializedObject) {
        String clsName = deserializedObject.getClass().getName();
        if( clsName.startsWith("org.openpnp") ) {
            for (Method method : deserializedObject.getClass().getDeclaredMethods()) {
                try {
                    method.setAccessible(true);
                    Annotation annotation = method.getAnnotation(PreSerialize.class);
                    if (annotation != null) {
                        try {
                            method.invoke(deserializedObject);
                        } catch (Throwable e) {
                            throw new RuntimeException("Failed to call @PreSerialize annotated method in class "
                                + method.getDeclaringClass().getName(), e);
                        }
                    }
                } catch(Throwable e) {
                    System.err.println("Failed to call make " + method + " accessible");
                }
            }
        }
    }
}
