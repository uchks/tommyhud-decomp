package codes.biscuit.tommyhud.gui.buttons;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.core.Element;
import codes.biscuit.tommyhud.gui.buttons.ButtonResize;
import codes.biscuit.tommyhud.util.ColorCode;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;

public class ButtonResize extends GuiButton {
    private static final int SIZE = 2;
    private TommyHUD main = TommyHUD.getInstance();
    private Element element;
    private ButtonResize.Corner corner;
    public float x;
    public float y;
    private float cornerOffsetX;
    private float cornerOffsetY;

    public ButtonResize(float x, float y, Element element, ButtonResize.Corner corner) {
        super(0, 0, 0, "");
        this.element = element;
        this.corner = corner;
        this.x = x;
        this.y = y;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        float scale = this.main.getConfigManager().getConfigValues().getGuiScale(this.element);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        this.hovered = (float)mouseX >= (this.x - 2.0F) * scale && (float)mouseY >= (this.y - 2.0F) * scale && (float)mouseX < (this.x + 2.0F) * scale && (float)mouseY < (this.y + 2.0F) * scale;
        int color = this.hovered ? ColorCode.WHITE.getRGB() : ColorCode.WHITE.getColor(70).getRGB();
        this.main.getUtils().drawRect((double)(this.x - 2.0F), (double)(this.y - 2.0F), (double)(this.x + 2.0F), (double)(this.y + 2.0F), color);
        GlStateManager.popMatrix();
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        float minecraftScale = (float)sr.getScaleFactor();
        float floatMouseX = (float)Mouse.getX() / minecraftScale;
        float floatMouseY = (float)(mc.displayHeight - Mouse.getY()) / minecraftScale;
        this.cornerOffsetX = floatMouseX;
        this.cornerOffsetY = floatMouseY;
        return this.hovered;
    }

    public ButtonResize.Corner getCorner() {
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

    public static enum Corner {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT;
    }
}