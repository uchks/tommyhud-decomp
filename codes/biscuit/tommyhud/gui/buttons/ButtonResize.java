package codes.biscuit.tommyhud.gui.buttons;

import codes.biscuit.tommyhud.*;
import codes.biscuit.tommyhud.core.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import codes.biscuit.tommyhud.util.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;

public class ButtonResize extends GuiButton
{
    private static final int SIZE = 2;
    private TommyHUD main;
    private Element element;
    private Corner corner;
    public float x;
    public float y;
    private float cornerOffsetX;
    private float cornerOffsetY;
    
    public ButtonResize(final float x, final float y, final Element element, final Corner corner) {
        super(0, 0, 0, "");
        this.main = TommyHUD.getInstance();
        this.element = element;
        this.corner = corner;
        this.x = x;
        this.y = y;
    }
    
    public void func_146112_a(final Minecraft mc, final int mouseX, final int mouseY) {
        final float scale = this.main.getConfigManager().getConfigValues().getGuiScale(this.element);
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a(scale, scale, 1.0f);
        this.field_146123_n = (mouseX >= (this.x - 2.0f) * scale && mouseY >= (this.y - 2.0f) * scale && mouseX < (this.x + 2.0f) * scale && mouseY < (this.y + 2.0f) * scale);
        final int color = this.field_146123_n ? ColorCode.WHITE.getRGB() : ColorCode.WHITE.getColor(70).getRGB();
        this.main.getUtils().drawRect(this.x - 2.0f, this.y - 2.0f, this.x + 2.0f, this.y + 2.0f, color);
        GlStateManager.func_179121_F();
    }
    
    public boolean func_146116_c(final Minecraft mc, final int mouseX, final int mouseY) {
        final ScaledResolution sr = new ScaledResolution(mc);
        final float minecraftScale = (float)sr.func_78325_e();
        final float floatMouseX = Mouse.getX() / minecraftScale;
        final float floatMouseY = (mc.field_71440_d - Mouse.getY()) / minecraftScale;
        this.cornerOffsetX = floatMouseX;
        this.cornerOffsetY = floatMouseY;
        return this.field_146123_n;
    }
    
    public Corner getCorner() {
        return this.corner;
    }
    
    public float getCornerOffsetX() {
        return this.cornerOffsetX;
    }
    
    public float getCornerOffsetY() {
        return this.cornerOffsetY;
    }
    
    public Element getElement() {
        return this.element;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public enum Corner
    {
        TOP_LEFT, 
        TOP_RIGHT, 
        BOTTOM_RIGHT, 
        BOTTOM_LEFT;
    }
}
