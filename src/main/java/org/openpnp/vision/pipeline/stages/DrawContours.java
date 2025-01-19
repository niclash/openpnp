package org.openpnp.vision.pipeline.stages;

import java.awt.Color;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;
import org.openpnp.vision.FluentCv;
import org.openpnp.vision.pipeline.CvPipeline;
import org.openpnp.vision.pipeline.CvStage;
import org.openpnp.vision.pipeline.Property;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class DrawContours extends CvStage {
    @JacksonXmlProperty
    private Color color = null;

    @JacksonXmlProperty( isAttribute = true )
    private String contoursStageName = null;
    
    @JacksonXmlProperty( isAttribute = true )
    private int thickness = 1;
    
    @Property(description="The index of the contour in the list to draw. Any negative value will draw all contours.")
    @JacksonXmlProperty( isAttribute = true )
    private int index = -1;
    
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getContoursStageName() {
        return contoursStageName;
    }

    public void setContoursStageName(String contoursStageName) {
        this.contoursStageName = contoursStageName;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public Result process(CvPipeline pipeline) throws Exception {
        if (contoursStageName == null || contoursStageName.trim().isEmpty()) {
            throw new Exception("contoursStageName is required.");
        }
        Result result = pipeline.getExpectedResult(contoursStageName);
        if (result.model == null) {
            return null;
        }
        List<MatOfPoint> contours = result.getExpectedListModel(MatOfPoint.class, null);
        Mat mat = pipeline.getWorkingImage();
        if (index < 0) {
            for (int i = 0; i < contours.size(); i++) {
                Imgproc.drawContours(mat, contours, i, FluentCv.colorToScalar(color == null ? FluentCv.indexedColor(i) : color), thickness);
            }
        }
        else {
            Imgproc.drawContours(mat, contours, index, FluentCv.colorToScalar(color == null ? FluentCv.indexedColor(index) : color), thickness);
        }
        return null;
    }
}
