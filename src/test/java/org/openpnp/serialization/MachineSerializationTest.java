package org.openpnp.serialization;

import org.junit.jupiter.api.Test;
import org.openpnp.machine.reference.ReferenceActuator;
import org.openpnp.machine.reference.ReferenceMachine;
import org.openpnp.machine.reference.camera.SimulatedUpCamera;
import org.openpnp.machine.reference.feeder.ReferencePushPullFeeder;
import org.openpnp.model.Board;
import org.openpnp.model.Configuration;
import org.openpnp.model.Configuration.MachineConfigurationHolder;
import org.openpnp.spi.Actuator;
import org.openpnp.spi.Camera;
import org.openpnp.spi.Feeder;
import org.openpnp.spi.Machine;
import org.openpnp.util.XmlSerialize;

import java.io.File;

public class MachineSerializationTest extends SerializationTest {

    @Test
    void testMinimalMachine() throws Exception {
        Machine underTest = new ReferenceMachine();
        Feeder feeder = new ReferencePushPullFeeder();
        underTest.addFeeder(feeder);
        Actuator actuator = new ReferenceActuator();
        underTest.addActuator(actuator);
        Camera camera  = new SimulatedUpCamera();
        underTest.addCamera(camera);
        String xml = XmlSerialize.serialization().writeAsString(underTest);
        System.out.println(xml);
    }

    @Test
    void testDefaultMachineXml() throws Exception {
        File f = new File("src/main/resources/config/machine.xml");
        MachineConfigurationHolder machine = serializer.read(MachineConfigurationHolder.class, f);
        System.out.println(machine);

        String xml = XmlSerialize.serialization().writeAsString(machine);
        System.out.println(xml);
    }
}
