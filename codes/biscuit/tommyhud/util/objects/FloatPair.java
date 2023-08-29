package codes.biscuit.tommyhud.util.objects;

import org.apache.commons.lang3.mutable.*;
import org.apache.commons.lang3.builder.*;

public class FloatPair
{
    private MutableFloat x;
    private MutableFloat y;
    
    public FloatPair(final float x, final float y) {
        this.x = new MutableFloat(x);
        this.y = new MutableFloat(y);
    }
    
    public float getX() {
        return this.x.getValue();
    }
    
    public float getY() {
        return this.y.getValue();
    }
    
    public void setY(final float y) {
        this.y.setValue(y);
    }
    
    public void setX(final float x) {
        this.x.setValue(x);
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        final FloatPair otherFloatPair = (FloatPair)other;
        return new EqualsBuilder().append(this.getX(), otherFloatPair.getX()).append(this.getY(), otherFloatPair.getY()).isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(83, 11).append(this.getX()).append(this.getY()).toHashCode();
    }
    
    @Override
    public String toString() {
        return this.getX() + "|" + this.getY();
    }
    
    public FloatPair cloneCoords() {
        return new FloatPair(this.getX(), this.getY());
    }
}
