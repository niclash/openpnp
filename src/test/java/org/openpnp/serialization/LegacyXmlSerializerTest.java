package org.openpnp.serialization;

import org.junit.jupiter.api.Test;
import org.openpnp.gui.components.reticle.*;
import org.openpnp.model.Footprint;
import org.openpnp.model.LengthUnit;
import org.openpnp.util.XmlSerialize;

import java.awt.*;

public class LegacyXmlSerializerTest {

    @Test
    public void testSerialize() {
        // niclas; Wanted to know what the XmlSerialize.serialize() output looks like.
        CrosshairReticle underTest1 = new CrosshairReticle();
        underTest1.setColor(Color.yellow);
        String xml = XmlSerialize.serialize(underTest1);
        System.out.println(xml);
        FiducialReticle underTest2 = new FiducialReticle();
        underTest2.setSize(2.5);
        underTest2.setUnits(LengthUnit.Millimeters);
        underTest2.setShape(FiducialReticle.Shape.Square);
        xml = XmlSerialize.serialize(underTest2);
        System.out.println(xml);
        RulerReticle underTest3 = new RulerReticle();
        underTest3.setUnits(LengthUnit.Millimeters);
        underTest3.setUnitsPerTick(2.3);
        xml = XmlSerialize.serialize(underTest3);
        System.out.println(xml);
        GridReticle underTest4 = new GridReticle();
        underTest4.setUnits(LengthUnit.Millimeters);
        underTest4.setUnitsPerTick(1.3);
        xml = XmlSerialize.serialize(underTest4);
        System.out.println(xml);
        FootprintReticle underTest5 = new FootprintReticle(createFootprint());
        xml = XmlSerialize.serialize(underTest5);
        System.out.println(xml);
        System.out.println("Done Serializing");
    }

    private static Footprint createFootprint() {
        Footprint footprint = new Footprint();
        Footprint.Pad pad1 = new Footprint.Pad();
        Footprint.Pad pad2 = new Footprint.Pad();
        pad1.setX(-12.3);
        pad1.setY(0);
        pad1.setX(12.3);
        pad1.setY(0);
        footprint.addPad(pad1);
        footprint.addPad(pad2);
        return footprint;
    }
}
