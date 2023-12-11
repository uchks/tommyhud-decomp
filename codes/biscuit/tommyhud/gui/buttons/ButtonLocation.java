package codes.biscuit.tommyhud.gui.buttons;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.core.Element;
import codes.biscuit.tommyhud.util.ColorCode;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;

public class ButtonLocation extends GuiButton {
    private static Element lastHoveredElement = null;
    private TommyHUD main = TommyHUD.getInstance();
    private Element element;
    private float boxXOne;
    private float boxXTwo;
    private float boxYOne;
    private float boxYTwo;
    private float scale;

    public ButtonLocation(Element element) {
        super(-1, 0, 0, (String)null);
        this.element = element;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.main.getRenderer().drawElement(this.element, this);
        if (this.hovered) {
            lastHoveredElement = this.element;
        }

    }

    public void checkHoveredAndDrawBox(float boxXOne, float boxXTwo, float boxYOne, float boxYTwo, float scale) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        float minecraftScale = (float)sr.getScaleFactor();
        float floatMouseX = (float)Mouse.getX() / minecraftScale;
        float floatMouseY = (float)(Minecraft.getMinecraft().displayHeight - Mouse.getY()) / minecraftScale;
        this.hovered = floatMouseX >= boxXOne * scale && floatMouseY >= boxYOne * scale && floatMouseX < boxXTwo * scale && floatMouseY < boxYTwo * scale;
        int boxAlpha = 70;
        if (this.hovered) {
            boxAlpha = 120;
        }

        this.main.getUtils().drawRect((double)boxXOne, (double)boxYOne, (double)boxXTwo, (double)boxYTwo, ColorCode.GRAY.getColor(boxAlpha).getRGB());
        this.boxXOne = boxXOne;
        this.boxXTwo = boxXTwo;
        this.boxYOne = boxYOne;
        this.boxYTwo = boxYTwo;
        this.scale = scale;
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.enabled && this.visible && this.hovered;
    }

    public void playPressSound(SoundHandler soundHandlerIn) {
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
        return lastHoveredElement;
    }

    public float getScale() {
        return this.scale;
    }
}
 