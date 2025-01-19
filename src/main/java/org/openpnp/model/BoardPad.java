package org.openpnp.model;

import org.openpnp.model.Abstract2DLocatable.Side;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class BoardPad extends AbstractModelObject {
    public enum Type {
        Paste, Ignore
    }

    @JacksonXmlProperty( isAttribute = true )
    private Type type = Type.Paste;

    @JacksonXmlProperty( isAttribute = true )
    protected Side side = Side.Top;

    @JacksonXmlProperty
    protected Location location = new Location(LengthUnit.Millimeters);

    @JacksonXmlProperty( isAttribute = true )
    protected String name;

    @JacksonXmlProperty
    protected Pad pad;

    public BoardPad() {

    }

    public BoardPad(BoardPad boardPad) {
        this.type = boardPad.type;
        this.side = boardPad.side;
        this.location = boardPad.location;
        this.name = boardPad.name;
        this.pad = boardPad.pad;
    }
    
    public BoardPad(Pad pad, Location location) {
        setPad(pad);
        setLocation(location);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        Location oldValue = this.location;
        this.location = location;
        firePropertyChange("location", oldValue, location);
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        Object oldValue = this.side;
        this.side = side;
        firePropertyChange("side", oldValue, side);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        Object oldValue = this.type;
        this.type = type;
        firePropertyChange("type", oldValue, type);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Object oldValue = this.name;
        this.name = name;
        firePropertyChange("name", oldValue, name);
    }

    public Pad getPad() {
        return pad;
    }

    public void setPad(Pad pad) {
        Object oldValue = pad;
        this.pad = pad;
        firePropertyChange("pad", oldValue, pad);
    }
}
