package org.openpnp.serialization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openpnp.model.Configuration;
import org.openpnp.model.Configuration.PackagesConfigurationHolder;
import org.openpnp.model.Footprint;
import org.openpnp.model.LengthUnit;
import org.openpnp.model.Package;
import org.openpnp.util.XmlSerialize;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class PackageSerializationTest extends SerializationTest {

    @Test
    void testMinimalPackage() throws Exception {
        Package pkg = new Package("test-id");
        pkg.setId("123");
        pkg.setDescription("This is a test");
        pkg.setTapeSpecification("This is a tape");
        Footprint footprint = new Footprint();
        pkg.setFootprint(footprint);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.write(pkg, baos);
        String xml = baos.toString();
        System.out.println(xml);

        Package read = serializer.read(Package.class, xml);
        System.out.println(read);
        assertThat(read.getId(), equalTo(pkg.getId()));
        assertThat(read.getFootprint().getUnits(), equalTo(pkg.getFootprint().getUnits()));
    }

    @Test
    void testTypicalPackage() throws Exception {

        Package pkg = XmlSerialize.serialization().read(Package.class, XML1);
        assertThat(pkg.getId(), equalTo("0805"));
        assertThat(pkg.getFootprint().getUnits(), equalTo(LengthUnit.Millimeters));
    }

    @Test
    void testPackages() throws Exception {

        PackagesConfigurationHolder pkg = XmlSerialize.serialization().read(PackagesConfigurationHolder.class, XML2);
    }

    @Test
    void testDefaultPackageXml() throws Exception {
        File f = new File("src/main/resources/config/packages.xml");
        XmlObjectMapperSerializer serializer = XmlSerialize.serialization();
        PackagesConfigurationHolder holder =
            serializer.read(PackagesConfigurationHolder.class, f);
    }

    private static final String XML1 = "    <package id=\"0805\" description=\"0805\">\n" +
        "        <footprint units=\"Millimeters\">\n" +
        "            <pad name=\"1\" x=\"-0.95\" y=\"0.0\" width=\"1.3\" height=\"1.5\" rotation=\"90.0\" roundness=\"0.0\"/>\n" +
        "            <pad name=\"2\" x=\"0.95\" y=\"0.0\" width=\"1.3\" height=\"1.5\" rotation=\"90.0\" roundness=\"0.0\"/>\n" +
        "        </footprint>\n" +
        "        <compatible-nozzle-tip-ids class=\"java.util.ArrayList\">\n" +
        "            <string>NT1</string>\n" +
        "            <string>NT2</string>\n" +
        "        </compatible-nozzle-tip-ids>\n" +
        "    </package>\n";

    private static final String XML2 = "<openpnp-packages>\n" +
        "    <package id=\"SW-TACT-5.25MM\">\n" +
        "    </package>\n" +
        "    <package id=\"JEDEC-MS-013-AB\" description=\"18 SOIC 0.300in / 7.5mm Wide\">\n" +
        "    </package>\n" +
        "    <package id=\"0805\" description=\"0805\">\n" +
        "        <footprint units=\"Millimeters\">\n" +
        "            <pad name=\"1\" x=\"-0.95\" y=\"0.0\" width=\"1.3\" height=\"1.5\" rotation=\"90.0\" roundness=\"0.0\"/>\n" +
        "            <pad name=\"2\" x=\"0.95\" y=\"0.0\" width=\"1.3\" height=\"1.5\" rotation=\"90.0\" roundness=\"0.0\"/>\n" +
        "        </footprint>\n" +
        "        <compatible-nozzle-tip-ids class=\"java.util.ArrayList\">\n" +
        "            <string>NT1</string>\n" +
        "            <string>NT2</string>\n" +
        "        </compatible-nozzle-tip-ids>\n" +
        "    </package>\n" +
        "</openpnp-packages>\n";
}
