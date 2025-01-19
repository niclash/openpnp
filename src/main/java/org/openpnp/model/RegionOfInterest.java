package org.openpnp.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RegionOfInterest {
    @JacksonXmlProperty
    protected Location upperLeftCorner;

    @JacksonXmlProperty
    protected Location upperRightCorner;

    @JacksonXmlProperty
    protected Location lowerLeftCorner;

    @JacksonXmlProperty( isAttribute = true )
    protected boolean rectify;

    @JacksonXmlProperty
    protected Location offsets;
    
    public RegionOfInterest()
    {}
    
    public RegionOfInterest(Location upperLeftCorner, Location upperRightCorner,
            Location lowerLeftCorner, boolean rectify) {
        this.upperLeftCorner = upperLeftCorner;
        this.upperRightCorner = upperRightCorner;
        this.lowerLeftCorner = lowerLeftCorner;
        this.rectify = rectify;
        this.offsets = null;
    }
    
    public RegionOfInterest(Location upperLeftCorner, Location upperRightCorner,
            Location lowerLeftCorner, boolean rectify, Location offsets) {
        this.upperLeftCorner = upperLeftCorner;
        this.upperRightCorner = upperRightCorner;
        this.lowerLeftCorner = lowerLeftCorner;
        this.rectify = rectify;
        this.offsets = offsets;
    }
    
    public Location getUpperLeftCorner() {
        return upperLeftCorner;
    }
    public Location getUpperRightCorner() {
        return upperRightCorner;
    }
    public Location getLowerLeftCorner() {
        return lowerLeftCorner;
    }
    public boolean isRectify() {
        return rectify;
    }
    public Location getOffsets() {
        return offsets;
    }
    
    public RegionOfInterest rotateXy(double angle) {
        return new RegionOfInterest(
                upperLeftCorner.rotateXy(angle),
                upperRightCorner.rotateXy(angle),
                lowerLeftCorner.rotateXy(angle),
                rectify,
                this.offsets==null ? null : offsets.rotateXy(angle));
    }
}
