package codes.biscuit.tommyhud.core;

import codes.biscuit.tommyhud.core.DrawType;
import codes.biscuit.tommyhud.util.ColorCode;
import java.util.Map;

public class GuiDefault {
    private DrawType drawType;
    private ColorCode color;

    private GuiDefault(DrawType drawType) {
        this(drawType, ColorCode.WHITE);
    }

    public GuiDefault(ColorCode color) {
        this(DrawType.TEXT, color);
    }

    public GuiDefault(DrawType drawType, ColorCode color) {
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