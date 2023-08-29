package codes.biscuit.tommyhud.util;

import java.awt.*;
import com.google.common.base.*;
import org.apache.commons.lang3.*;

public enum ColorCode
{
    BLACK('0', 0), 
    DARK_BLUE('1', 170), 
    DARK_GREEN('2', 43520), 
    DARK_AQUA('3', 43690), 
    DARK_RED('4', 11141120), 
    DARK_PURPLE('5', 11141290), 
    GOLD('6', 16755200), 
    GRAY('7', 11184810), 
    DARK_GRAY('8', 5592405), 
    BLUE('9', 5592575), 
    GREEN('a', 5635925), 
    AQUA('b', 5636095), 
    RED('c', 16733525), 
    LIGHT_PURPLE('d', 16733695), 
    YELLOW('e', 16777045), 
    WHITE('f', 16777215), 
    MAGIC('k', true, "obfuscated"), 
    BOLD('l', true), 
    STRIKETHROUGH('m', true), 
    UNDERLINE('n', true, "underlined"), 
    ITALIC('o', true), 
    RESET('r');
    
    public static final char COLOR_CHAR = '§';
    private char code;
    private boolean isFormat;
    private String jsonName;
    private String toString;
    private Color color;
    
    private ColorCode(final char code) {
        this(code, -1);
    }
    
    private ColorCode(final char code, final int rgb) {
        this(code, false, rgb);
    }
    
    private ColorCode(final char code, final boolean isFormat) {
        this(code, isFormat, -1);
    }
    
    private ColorCode(final char code, final boolean isFormat, final int rgb) {
        this(code, isFormat, null, rgb);
    }
    
    private ColorCode(final char code, final boolean isFormat, final String jsonName) {
        this(code, isFormat, jsonName, -1);
    }
    
    private ColorCode(final char code, final boolean isFormat, final String jsonName, final int rgb) {
        this.code = code;
        this.isFormat = isFormat;
        this.jsonName = jsonName;
        this.toString = new String(new char[] { '§', code });
        this.color = (this.isColor() ? new Color(rgb) : null);
    }
    
    public static ColorCode getByChar(final char code) {
        for (final ColorCode color : values()) {
            if (color.code == code) {
                return color;
            }
        }
        return null;
    }
    
    public char getCode() {
        return this.code;
    }
    
    public Color getColor() {
        Preconditions.checkArgument(this.isColor(), (Object)"Format has no color!");
        return this.color;
    }
    
    public Color getColor(final float alpha) {
        return this.getColor((int)alpha);
    }
    
    public Color getColor(final int alpha) {
        return new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), alpha);
    }
    
    public String getJsonName() {
        return StringUtils.isEmpty((CharSequence)this.jsonName) ? this.name().toLowerCase() : this.jsonName;
    }
    
    public int getRGB() {
        return this.getColor().getRGB();
    }
    
    public boolean isColor() {
        return !this.isFormat() && this != ColorCode.RESET;
    }
    
    public boolean isFormat() {
        return this.isFormat;
    }
    
    public ColorCode getNextFormat() {
        return this.getNextFormat(this.ordinal());
    }
    
    private ColorCode getNextFormat(final int ordinal) {
        final int nextColor = ordinal + 1;
        if (nextColor > values().length - 1) {
            return values()[0];
        }
        if (!values()[nextColor].isColor()) {
            return this.getNextFormat(nextColor);
        }
        return values()[nextColor];
    }
    
    @Override
    public String toString() {
        return this.toString;
    }
}
