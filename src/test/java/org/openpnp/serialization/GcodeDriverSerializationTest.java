package org.openpnp.serialization;

import org.junit.jupiter.api.Test;
import org.openpnp.machine.reference.driver.GcodeAsyncDriver;
import org.openpnp.machine.reference.driver.GcodeDriver;
import org.openpnp.spi.Driver;
import org.openpnp.util.XmlSerialize;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

public class GcodeDriverSerializationTest extends SerializationTest {

    @Test
    void testGcodeDriverSerialization() throws Exception {
        Driver d = new GcodeDriver();
        String xml = XmlSerialize.serialization().writeAsString(d);
        System.out.println(xml);
        GcodeDriver driver = XmlSerialize.serialization().read(GcodeDriver.class, xml);

        driver = XmlSerialize.serialization().read(GcodeAsyncDriver.class, XML);
    }

    private static String XML =
            "         <driver class=\"org.openpnp.machine.reference.driver.GcodeAsyncDriver\" id=\"DRV165852d288e966d4\" name=\"GcodeAsyncDriver\" motion-control-type=\"ModeratedConstantAcceleration\" communications=\"tcp\" connection-keep-alive=\"false\" units=\"Millimeters\" max-feed-rate=\"0\" backlash-offset-x=\"-1.0\" backlash-offset-y=\"-1.0\" backlash-offset-z=\"0.0\" backlash-offset-r=\"0.0\" non-squareness-factor=\"0.0\" backlash-feed-rate-factor=\"0.1\" timeout-milliseconds=\"5000\" connect-wait-time-milliseconds=\"3000\" visual-homing-enabled=\"true\" backslash-escaped-characters-enabled=\"false\" remove-comments=\"true\" compress-gcode=\"true\" logging-gcode=\"false\" supporting-pre-move=\"false\" using-letter-variables=\"true\" infinity-timeout-milliseconds=\"60000\" writer-polling-interval=\"100\" writer-queue-timeout=\"60000\" max-commands-queued=\"1000\" confirmation-flow-control=\"true\" reported-location-confirmation=\"true\" interpolation-max-steps=\"32\" interpolation-jerk-steps=\"4\" interpolation-time-step=\"0.001\" interpolation-min-step=\"16\">\n" +
            "            <serial line-ending-type=\"LF\" port-name=\"\" baud=\"115200\" flow-control=\"Off\" data-bits=\"Eight\" stop-bits=\"One\" parity=\"None\" set-dtr=\"false\" set-rts=\"false\" name=\"SerialPortCommunications\"/>\n" +
            "            <tcp line-ending-type=\"LF\" ip-address=\"GcodeServer\" port=\"23\" name=\"TcpCommunications\"/>\n" +
            "            <simulated line-ending-type=\"LF\"/>\n" +
            "            <homing-fiducial-location units=\"Millimeters\" x=\"0.0\" y=\"0.0\" z=\"0.0\" rotation=\"0.0\"/>\n" +
            "            <detected-firmware><![CDATA[FIRMWARE_NAME:GcodeServer, FIRMWARE_URL:http%3A//openpnp.org, X-SOURCE_CODE_URL:https%3A//github.com/openpnp/openpnp, FIRMWARE_VERSION:INTERNAL BUILD, X-FIRMWARE_BUILD_DATE:Oct 23 2020 00:00:00, X-AXES:0, X-PAXES:0]]></detected-firmware>\n" +
            "            <reported-axes><![CDATA[ok C: X:0.0000 Y:0.0000 Z:0.0000 C:0.0000]]></reported-axes>\n" +
            "            <command type=\"COMMAND_CONFIRM_REGEX\">\n" +
            "               <text><![CDATA[^ok.*]]></text>\n" +
            "            </command>\n" +
            "            <command type=\"POSITION_REPORT_REGEX\">\n" +
            "               <text><![CDATA[^.*X:(?<X>-?\\d+\\.\\d+) Y:(?<Y>-?\\d+\\.\\d+) Z:(?<Z>-?\\d+\\.\\d+) .*C:(?<C>-?\\d+\\.\\d+).*]]></text>\n" +
            "            </command>\n" +
            "            <command type=\"CONNECT_COMMAND\">\n" +
            "               <text><![CDATA[G21 ; Set millimeters mode ]]></text>\n" +
            "               <text><![CDATA[G90 ; Set absolute positioning mode]]></text>\n" +
            "            </command>\n" +
            "            <command type=\"HOME_COMMAND\">\n" +
            "               <text><![CDATA[G28 ; Home all axes]]></text>\n" +
            "            </command>\n" +
            "            <command type=\"SET_GLOBAL_OFFSETS_COMMAND\">\n" +
            "               <text><![CDATA[G92 {X:X%.4f} {Y:Y%.4f} {Z:Z%.4f} {C:C%.4f} ; reset coordinates]]></text>\n" +
            "            </command>\n" +
            "            <command type=\"GET_POSITION_COMMAND\">\n" +
            "               <text><![CDATA[M114 ; get position]]></text>\n" +
            "            </command>\n" +
            "            <command type=\"MOVE_TO_COMMAND\">\n" +
            "               <text><![CDATA[{Acceleration:M204 S%.2f} G1 {X:X%.4f} {Y:Y%.4f} {Z:Z%.4f} {C:C%.4f} {FeedRate:F%.2f} ; move to target]]></text>\n" +
            "            </command>\n" +
            "            <command type=\"MOVE_TO_COMPLETE_COMMAND\">\n" +
            "               <text><![CDATA[M400 ; Wait for moves to complete before returning]]></text>\n" +
            "            </command>\n" +
            "            <junction-deviation value=\"0.02\" units=\"Millimeters\"/>\n" +
            "         </driver>\n";
}

