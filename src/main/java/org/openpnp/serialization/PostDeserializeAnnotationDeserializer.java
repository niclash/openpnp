package org.openpnp.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Class implementing the functionality of the {@link PostDeserialize} annotation.
 */
public class PostDeserializeAnnotationDeserializer extends StdDeserializer<Object>
    implements ContextualDeserializer, ResolvableDeserializer
{

    static BeanDeserializerModifier modifier = new BeanDeserializerModifier() {
        @SuppressWarnings("unchecked")
        @Override
        public JsonDeserializer<?> modifyDeserializer(DeserializationConfig config, BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
            for (AnnotatedMethod m : beanDesc.getClassInfo().memberMethods()) {
                if (m.getAnnotation(PostDeserialize.class) != null) {
                    Method method = m.getAnnotated();
                    return new PostDeserializeAnnotationDeserializer(method, (BeanDeserializer) deserializer);
                }
            }
            return deserializer;
        }
    };

    private final Method method;
    private final BeanDeserializer delegate;

    public PostDeserializeAnnotationDeserializer(Method method, BeanDeserializer delegate) {
        super(Object.class);
        this.method = method;
        this.delegate = delegate;
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Object deserializedObject = delegate.deserialize(p, ctxt);
        callPostDeserializeMethods(deserializedObject);
        return deserializedObject;
    }

    private void callPostDeserializeMethods(Object deserializedObject) {
        try {
            method.invoke(deserializedObject);
        } catch (Exception e) {
            throw new RuntimeException("Failed to call @PostDeserialize annotated method in class "
                + deserializedObject.getClass().getName(), e);
        }
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
        return delegate.createContextual(ctxt, property);
    }

    @Override
    public void resolve(DeserializationContext ctxt) throws JsonMappingException {
        delegate.resolve(ctxt);
    }
}