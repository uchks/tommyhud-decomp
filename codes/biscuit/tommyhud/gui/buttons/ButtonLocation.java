package codes.biscuit.tommyhud.gui.buttons;

import codes.biscuit.tommyhud.core.*;
import codes.biscuit.tommyhud.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import codes.biscuit.tommyhud.util.*;
import net.minecraft.client.audio.*;

public class ButtonLocation extends GuiButton
{
    private static Element lastHoveredElement;
    private TommyHUD main;
    private Element element;
    private float boxXOne;
    private float boxXTwo;
    private float boxYOne;
    private float boxYTwo;
    private float scale;
    
    public ButtonLocation(final Element element) {
        super(-1, 0, 0, (String)null);
        this.main = TommyHUD.getInstance();
        this.element = element;
    }
    
    public void func_146112_a(final Minecraft mc, final int mouseX, final int mouseY) {
        this.main.getRenderer().drawElement(this.element, this);
        if (this.field_146123_n) {
            ButtonLocation.lastHoveredElement = this.element;
        }
    }
    
    public void checkHoveredAndDrawBox(final float boxXOne, final float boxXTwo, final float boxYOne, final float boxYTwo, final float scale) {
        final ScaledResolution sr = new ScaledResolution(Minecraft.func_71410_x());
        final float minecraftScale = (float)sr.func_78325_e();
        final float floatMouseX = Mouse.getX() / minecraftScale;
        final float floatMouseY = (Minecraft.func_71410_x().field_71440_d - Mouse.getY()) / minecraftScale;
        this.field_146123_n = (floatMouseX >= boxXOne * scale && floatMouseY >= boxYOne * scale && floatMouseX < boxXTwo * scale && floatMouseY < boxYTwo * scale);
        int boxAlpha = 70;
        if (this.field_146123_n) {
            boxAlpha = 120;
        }
        this.main.getUtils().drawRect(boxXOne, boxYOne, boxXTwo, boxYTwo, ColorCode.GRAY.getColor(boxAlpha).getRGB());
        this.boxXOne = boxXOne;
        this.boxXTwo = boxXTwo;
        this.boxYOne = boxYOne;
        this.boxYTwo = boxYTwo;
        this.scale = scale;
    }
    
    public boolean func_146116_c(final Minecraft mc, final int mouseX, final int mouseY) {
        return this.field_146124_l && this.field_146125_m && this.field_146123_n;
    }
    
    public void func_146113_a(final SoundHandler soundHandlerIn) {
    }
    
    public Element getElement() {
        return this.element;
    }
    
    public float getBoxXOne() {
        return this.boxXOne;
    }
    
    public float getBoxXTwo() {
        return this.boxXTwo;
    }
    
    public float getBoxYOne() {
        return this.boxYOne;
    }
    
    public float getBoxYTwo() {
        return this.boxYTwo;
    }
    
    public static Element getLastHoveredElement() {
        return ButtonLocation.lastHoveredElement;
    }
    
    public float getScale() {
        return this.scale;
    }
    
    static {
        ButtonLocation.lastHoveredElement = null;
    }
}
