package org.openpnp.vision.pipeline.stages;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openpnp.vision.pipeline.CvPipeline;
import org.openpnp.vision.pipeline.CvStage;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Threshold extends CvStage {
    @JacksonXmlProperty( isAttribute = true )
    private int threshold = 100;

    @JacksonXmlProperty( isAttribute = true )
    private boolean auto = false;

    @JacksonXmlProperty( isAttribute = true )
    private boolean invert = false;

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    @Override
    public Result process(CvPipeline pipeline) throws Exception {
        Mat mat = pipeline.getWorkingImage();
        int type = invert ? Imgproc.THRESH_BINARY_INV : Imgproc.THRESH_BINARY;
        type |= auto ? Imgproc.THRESH_OTSU : 0;
        Imgproc.threshold(mat, mat, threshold, 255, type);
        return null;
    }
}
