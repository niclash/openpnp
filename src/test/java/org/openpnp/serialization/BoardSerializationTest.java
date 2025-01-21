package org.openpnp.serialization;

import org.junit.jupiter.api.Test;
import org.openpnp.model.Board;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class BoardSerializationTest extends SerializationTest {

    @Test
    void testBoard1() throws Exception {
        File f = new File("samples/pnp-test/pnp-test.board.xml");
        Board board = serializer.read(Board.class, f);
        assertThat(board.getDimensions().getX(), equalTo(37.0));
        assertThat(board.getDimensions().getY(), equalTo(28.5));
        assertThat(board.getDimensions().getZ(), equalTo(0.0));
        assertThat(board.getDimensions().getRotation(), equalTo(0.0));
        assertThat(board.getPlacements().size(), equalTo(30));
        assertThat(board.getPlacements().get(0).getId(), equalTo("R1"));
        assertThat(board.getSolderPastePads().size(), equalTo(48));
        System.out.println(board.getDimensions());
    }
}
