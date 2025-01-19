package org.openpnp.vision.pipeline.stages;

import org.openpnp.vision.pipeline.CvPipeline;
import org.openpnp.vision.pipeline.CvStage;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ImageRecall extends CvStage {
    @JacksonXmlProperty( isAttribute = true )
    private String imageStageName = null;

    public String getImageStageName() {
        return imageStageName;
    }

    public void setImageStageName(String imageStageName) {
        this.imageStageName = imageStageName;
    }

    @Override
    public Result process(CvPipeline pipeline) throws Exception {
        if (imageStageName == null || imageStageName.trim().isEmpty()) {
            return null;
        }
        Result result = pipeline.getExpectedResult(imageStageName);
        if (result.image == null) {
            return null;
        }
        return new Result(result.image.clone(), result.colorSpace);
    }
}
