package org.openpnp.serialization;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class JacksonFailureTest {

    @Test
    void testSerialization() throws Exception {

        JacksonXmlModule module = new JacksonXmlModule();
        XmlMapper mapper = new XmlMapper(module);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);

        PackagesConfigurationHolder holder = new PackagesConfigurationHolder();
        Package pkg1 = new Package();
        pkg1.id = "pkg1";
        Package pkg2 = new Package();
        pkg2.id = "pkg2";
        Footprint footprint1 = new Footprint();
        Footprint footprint2 = new Footprint();
        Pad pad1 = new Pad();
        Pad pad2 = new Pad();
        pad1.name = "abc";
        pad1.x = 10;
        pad1.y = 20;
        pad2.name = "def";
        pad2.x = 30;
        pad2.y = 40;
        footprint1.pads.add(pad1);
        footprint2.pads.add(pad2);
        pkg1.footprint = footprint1;
        pkg2.footprint = footprint2;
        holder.packages.add(pkg1);
        holder.packages.add(pkg2);
        System.out.println(holder);
        System.out.println(mapper.writeValueAsString(holder));
    }

    @Test
    void testSingle() throws Exception {
        JacksonXmlModule module = new JacksonXmlModule();
        XmlMapper mapper = new XmlMapper(module);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);

        Package pkg = mapper.readValue(XML1, Package.class);

    }

    @Test
    void testNestedArray() throws Exception {
        JacksonXmlModule module = new JacksonXmlModule();
        XmlMapper mapper = new XmlMapper(module);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE);

        PackagesConfigurationHolder holder = mapper.readValue(XML2, PackagesConfigurationHolder.class);

    }

    @JacksonXmlRootElement(localName = "openpnp-packages")
    public static class PackagesConfigurationHolder {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "package")
        private List<Package> packages = new ArrayList<>();

        @Override
        public String toString() {
            return "PackagesConfigurationHolder{" +
                "Package=" + packages +
                '}';
        }
    }

    public static class Package {
        @JacksonXmlProperty(isAttribute = true)
        private String id;

        @JacksonXmlProperty(localName = "footprint")
        private Footprint footprint;

        @Override
        public String toString() {
            return "Package{" +
                "id='" + id + '\'' +
                ", footprint=" + footprint +
                '}';
        }
    }

    public static abstract class AbstractModelObject {
    }

    public static class Footprint extends AbstractModelObject {

        @JacksonXmlProperty(isAttribute = true, localName = "units")
        private LengthUnit units = LengthUnit.Millimeters;

        @JacksonXmlProperty(localName = "pad")
        private List<Pad> pads = new ArrayList<>();

        @Override
        public String toString() {
            return "Footprint{" +
                "units=" + units +
                ", pads=" + pads +
                '}';
        }
    }

    public static class Pad {
        @JacksonXmlProperty(isAttribute = true)
        private String name;

        @JacksonXmlProperty(isAttribute = true)
        private double x;

        @Override
        public String toString() {
            return "Pad{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
        }

        @JacksonXmlProperty(isAttribute = true)
        private double y;
    }

    public enum LengthUnit {
        Meters,
        Centimeters,
        Millimeters
    }

    private static final String XML1 =
        "    <package id=\"0805\">\n" +
            "        <footprint units=\"Millimeters\">\n" +
            "            <pad name=\"1\" x=\"-0.95\" y=\"0.0\"/>\n" +
            "            <pad name=\"2\" x=\"0.95\" y=\"0.0\"/>\n" +
            "        </footprint>\n" +
            "    </package>\n";

    private static final String XML2 = "<openpnp-packages>\n" +
        "    <package id=\"SW-TACT-5.25MM\">\n" +
        "    </package>\n" +
        "    <package id=\"JEDEC-MS-013-AB\">\n" +
        "    </package>\n" +
        "    <package id=\"0805\">\n" +
        "        <footprint units=\"Millimeters\">\n" +
        "            <pad name=\"1\" x=\"-0.95\" y=\"0.0\"/>\n" +
        "            <pad name=\"2\" x=\"0.95\" y=\"0.0\"/>\n" +
        "        </footprint>\n" +
        "    </package>\n" +
        "</openpnp-packages>\n";
}
