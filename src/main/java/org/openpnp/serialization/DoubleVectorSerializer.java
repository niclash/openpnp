package org.openpnp.serialization;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;

import java.util.Arrays;
import java.util.stream.Collectors;

public class DoubleVectorSerializer
implements Converter<double[], String> {

    @Override
    public String convert(double[] value) {
        return Arrays.stream(value).mapToObj(Double::toString).collect(Collectors.joining(", "));
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(double[].class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(String.class);
    }
}
