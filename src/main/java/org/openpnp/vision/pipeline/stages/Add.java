package org.openpnp.vision.pipeline.stages;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.openpnp.vision.pipeline.CvPipeline;
import org.openpnp.vision.pipeline.CvStage;
import org.openpnp.vision.pipeline.Stage;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@Stage(description="Adds two images together, scale either, or subtract second.")
public class Add extends CvStage {

    @JacksonXmlProperty( isAttribute = true )
    private String firstStageName = null;

    @JacksonXmlProperty( isAttribute = true )
    private String secondStageName = null;
    
    @JacksonXmlProperty( isAttribute = true )
    private double firstScalar = 1.0;

    @JacksonXmlProperty( isAttribute = true )
    private double secondScalar = 1.0;

    public String getFirstStageName() {
        return firstStageName;
    }

    public void setFirstStageName(String firstStageName) {
        this.firstStageName = firstStageName;
    }

    public String getSecondStageName() {
        return secondStageName;
    }

    public void setSecondStageName(String secondStageName) {
        this.secondStageName = secondStageName;
    }

    public double getFirstScalar() {
        return firstScalar;
    }

    public void setFirstScalar(double v) {
        this.firstScalar = v;
    }

    public double getSecondScalar() {
        return secondScalar;
    }

    public void setSecondScalar(double v) {
        this.secondScalar = v;
    }

    @Override
    public Result process(CvPipeline pipeline) throws Exception {
        if (firstStageName == null || firstStageName.trim().isEmpty()) {
            return null;
        }
        if (secondStageName == null || secondStageName.trim().isEmpty()) {
            return null;
        }
        // TODO STOPSHIP memory?
        Mat first = pipeline.getExpectedResult(firstStageName).image;
        Mat second = pipeline.getExpectedResult(secondStageName).image;

				if(this.firstScalar < 0){
					throw new Exception("firstScalar < 0!");
				}

        Mat f = first.clone();
				if(this.firstScalar != 1.0){
					Core.multiply(first, new Scalar(this.firstScalar), f);
				}

        Mat s = second.clone();
				if(this.secondScalar != 1.0){
					Core.multiply(second, new Scalar(Math.abs(this.secondScalar)), s);
				}
        
        Mat out = new Mat();
				if(this.secondScalar > 0){
	        Core.add(f, s, out);
				}
				else{
	        Core.subtract(f, s, out);
				}
				f.release();
				s.release();

        return new Result(out);
    }
}
