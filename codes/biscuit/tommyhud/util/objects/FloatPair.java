package codes.biscuit.tommyhud.util.objects;

import codes.biscuit.tommyhud.util.objects.FloatPair;
import java.util.Map;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.mutable.MutableFloat;

public class FloatPair {
    private MutableFloat x;
    private MutableFloat y;

    public FloatPair(float x, float y) {
        this.x = new MutableFloat(x);
        this.y = new MutableFloat(y);
    }

    public float getX() {
        return this.x.getValue();
    }

    public float getY() {
        return this.y.getValue();
    }

    public void setY(float y) {
        this.y.setValue(y);
    }

    public void setX(float x) {
        this.x.setValue(x);
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else if (other == this) {
            return true;
        } else if (other.getClass() != this.getClass()) {
            return false;
        } else {
            FloatPair otherFloatPair = (FloatPair)other;
            return (new EqualsBuilder()).append(this.getX(), otherFloatPair.getX()).append(this.getY(), otherFloatPair.getY()).isEquals();
        }
    }

    public int hashCode() {
        return (new HashCodeBuilder(83, 11)).append(this.getX()).append(this.getY()).toHashCode();
    }

    public String toString() {
        return this.getX() + "|" + this.getY();
    }

    public FloatPair cloneCoords() {
        return new FloatPair(this.getX(), this.getY());
    }
}
 