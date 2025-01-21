package org.openpnp.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class PreSerializeAnnotationSerializer extends StdSerializer<Object>
    implements ContextualSerializer, ResolvableSerializer {

    static BeanSerializerModifier modifier = new BeanSerializerModifier() {
        @SuppressWarnings("unchecked")
        @Override
        public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
            Method preSerializeMethod = null;
            for (AnnotatedMethod m : beanDesc.getClassInfo().memberMethods()) {
                if (m.getAnnotation(PreSerialize.class) != null) {
                    preSerializeMethod = m.getAnnotated();
                    break;
                }
            }

            if (preSerializeMethod != null) {
                serializer = new PreSerializeAnnotationSerializer(preSerializeMethod, (JsonSerializer<Object>) serializer);
            }
            return serializer;
        }
    };

    private final Method method;
    private final JsonSerializer<Object> delegate;

    public PreSerializeAnnotationSerializer(Method method, JsonSerializer<Object> delegate) {
        super(Object.class);
        this.method = method;
        this.method.setAccessible(true);
        this.delegate = delegate;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        callPreSerializeMethods(value);
        delegate.serialize(value, gen, provider);
    }

    @Override
    public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        callPreSerializeMethods(value);
        delegate.serializeWithType(value, gen, serializers, typeSer);
    }

    private void callPreSerializeMethods(Object deserializedObject) {
        try {
            method.invoke(deserializedObject);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to call @PreSerialize annotated method in class "
                + method.getDeclaringClass().getName(), e);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        // First let the delegate handle its own contextualization
        JsonSerializer<?> contextualDelegate = delegate;
        if (delegate instanceof ContextualSerializer) {
            contextualDelegate = ((ContextualSerializer) delegate).createContextual(prov, property);
        }
        if( property == null ) {
            return contextualDelegate;
        }
        // Now scan for @PreSerialize annotation on the current type
        JavaType type = property.getType();
        BeanDescription beanDesc = prov.getConfig().introspect(type);
        Method preSerializeMethod = null;

        for (AnnotatedMethod m : beanDesc.getClassInfo().memberMethods()) {
            if (m.getAnnotation(PreSerialize.class) != null) {
                preSerializeMethod = m.getAnnotated();
                break;
            }
        }

        // If we found a method and either the delegate changed or the method is different
        if (preSerializeMethod != null &&
            (contextualDelegate != delegate || !preSerializeMethod.equals(method))) {
            return new PreSerializeAnnotationSerializer(preSerializeMethod, (JsonSerializer<Object>) contextualDelegate);
        }

        return this;
    }

    @Override
    public void resolve(SerializerProvider provider) throws JsonMappingException {
        if ((delegate != null)
            && (delegate instanceof ResolvableSerializer)) {
            ((ResolvableSerializer) delegate).resolve(provider);
        }
    }
}

/*
public class PreSerializeAnnotationSerializer extends StdSerializer<Object>
    implements ContextualSerializer {

    static BeanSerializerModifier modifier = new BeanSerializerModifier() {
        @SuppressWarnings("unchecked")
        @Override
        public JsonSerializer<?> modifySerializer(SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer) {
            for (AnnotatedMethod m : beanDesc.getClassInfo().memberMethods()) {
                if (m.getAnnotation(PreSerialize.class) != null) {
                    Method method = m.getAnnotated();
                    serializer = new PreSerializeAnnotationSerializer(method, (JsonSerializer<Object>) serializer);
                }
            }
            return serializer;
        }
    };

    private final Method method;
    private final JsonSerializer<Object> delegate;

    public PreSerializeAnnotationSerializer(Method method, JsonSerializer<Object> delegate) {
        super(Object.class);
        this.method = method;
        this.method.setAccessible(true);
        this.delegate = delegate;
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        callPreSerializeMethods(value);
        delegate.serialize(value, gen, provider);
    }

    private void callPreSerializeMethods(Object deserializedObject) {
        try {
            method.invoke(deserializedObject);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to call @PreSerialize annotated method in class "
                + method.getDeclaringClass().getName(), e);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        JavaType type = property.getType();
        JsonSerializer<Object> serializer = prov.findTypedValueSerializer(type, false, property);
        return serializer;
//        List<Class<?>> typeHierarchy = new ArrayList<>();
//        Class<?> current = type.getRawClass();
//        while (current != Object.class) {
//            typeHierarchy.add(current);
//            current = current.getSuperclass();
//        }
//        typeHierarchy.addAll(Arrays.asList(type.getRawClass().getInterfaces()));
//        Optional<Method> optional = typeHierarchy.stream()
//            .flatMap(it -> stream(it.getDeclaredMethods()))
//            .filter(m -> m.getAnnotation(PreSerialize.class) != null)
//            .findAny();
//        if (optional.isPresent()) {
//            return new PreSerializeAnnotationSerializer(optional.get(), delegate);
//        } else {
//            return delegate;
//        }
    }
}
*/