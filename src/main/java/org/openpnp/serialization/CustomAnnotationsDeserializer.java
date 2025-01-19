package org.openpnp.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DelegatingDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Class implementing the functionality of the {@link PostDeserialize} annotation.
 */
public class CustomAnnotationsDeserializer extends DelegatingDeserializer {

    private final BeanDescription beanDescription;

    public CustomAnnotationsDeserializer(JsonDeserializer<Object> delegate, BeanDescription beanDescription) {
        super(delegate);
        this.beanDescription = beanDescription;
    }

    @Override
    protected JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> newDelegatee) {
        return new CustomAnnotationsDeserializer((JsonDeserializer<Object>) newDelegatee, beanDescription);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Object deserializedObject = super.deserialize(p, ctxt);
        callPostDeserializeMethods(deserializedObject);
        return deserializedObject;
    }

    private void callPostDeserializeMethods(Object deserializedObject) {
        String clsName = deserializedObject.getClass().getName();
        if (clsName.startsWith("org.openpnp")) {
            for (Method method : deserializedObject.getClass().getDeclaredMethods()) {
                method.setAccessible(true);
                Annotation annotation = method.getAnnotation(PostDeserialize.class);
                if (annotation != null) {
                    try {
                        method.invoke(deserializedObject);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to call @PostDeserialize annotated method in class "
                            + beanDescription.getClassInfo().getName(), e);
                    }
                }
            }
        }
    }
}