package org.openpnp.serialization;

import org.junit.jupiter.api.BeforeEach;
import org.openpnp.model.Configuration;
import org.openpnp.util.XmlSerialize;

import java.io.File;

public abstract class SerializationTest {

    protected XmlObjectMapperSerializer serializer;

    @BeforeEach
    void initConfig()
        throws Exception {
        File configDir = File.createTempFile("test", "");
        configDir.mkdir();
        Configuration.initialize(configDir);
        serializer = XmlSerialize.serialization();
    }
}
