package codes.biscuit.tommyhud.util.objects;

import org.apache.commons.lang3.mutable.*;
import org.apache.commons.lang3.builder.*;

public class IntPair
{
    private MutableInt x;
    private MutableInt y;
    
    public IntPair(final int x, final int y) {
        this.x = new MutableInt(x);
        this.y = new MutableInt(y);
    }
    
    public int getX() {
        return this.x.getValue();
    }
    
    public int getY() {
        return this.y.getValue();
    }
    
    public void setY(final int y) {
        this.y.setValue(y);
    }
    
    public void setX(final int x) {
        this.x.setValue(x);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        final IntPair chunkCoords = (IntPair)obj;
        return new EqualsBuilder().append(this.getX(), chunkCoords.getX()).append(this.getY(), chunkCoords.getY()).isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(83, 11).append(this.getX()).append(this.getY()).toHashCode();
    }
    
    @Override
    public String toString() {
        return this.getX() + "|" + this.getY();
    }
    
    public IntPair cloneCoords() {
        return new IntPair(this.getX(), this.getY());
    }
}
