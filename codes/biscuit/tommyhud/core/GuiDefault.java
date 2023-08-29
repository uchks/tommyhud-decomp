package codes.biscuit.tommyhud.core;

import codes.biscuit.tommyhud.util.*;

public class GuiDefault
{
    private DrawType drawType;
    private ColorCode color;
    
    private GuiDefault(final DrawType drawType) {
        this(drawType, ColorCode.WHITE);
    }
    
    public GuiDefault(final ColorCode color) {
        this(DrawType.TEXT, color);
    }
    
    public GuiDefault(final DrawType drawType, final ColorCode color) {
        this.drawType = drawType;
        this.color = color;
    }
    
    public DrawType getDrawType() {
        return this.drawType;
    }
    
    public ColorCode getColor() {
        return this.color;
    }
}
