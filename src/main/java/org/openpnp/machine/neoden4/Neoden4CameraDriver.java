package org.openpnp.machine.neoden4;

import com.sun.jna.Library;

public interface Neoden4CameraDriver extends Library {

	boolean img_capture(int which_camera);

	int img_init();

	boolean img_led(int camera, short mode);

	int img_read(int which_camera, byte[] pFrameBuffer, int BytesToRead, int timeoutMs);

	int img_readAsy(int which_camera, byte[] pFrameBuffer, int BytesToRead, int timeoutMs);

	int img_reset(int which_camera);

	boolean img_set_exp(int which_camera, short exposure);

	boolean img_set_gain(int which_camera, short gain);

	boolean img_set_lt(int which_camera, short a2, short a3);

	boolean img_set_wh(int which_camera, short w, short h);
}
