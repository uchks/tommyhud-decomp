package codes.biscuit.tommyhud.gui.buttons;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.core.Element;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ButtonColorWheel extends GuiButton {
    private static final ResourceLocation COLOR_WHEEL = new ResourceLocation("tommyhud", "colorwheel.png");
    private static final int SIZE = 10;
    private TommyHUD main = TommyHUD.getInstance();
    private Element element;
    public float x;
    public float y;

    public ButtonColorWheel(float x, float y, Element element) {
        super(0, 0, 0, "");
        this.element = element;
        this.width = 10;
        this.height = 10;
        this.x = x;
        this.y = y;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        float scale = this.main.getConfigManager().getConfigValues().getGuiScale(this.element);
        this.hovered = (float)mouseX >= this.x * scale && (float)mouseY >= this.y * scale && (float)mouseX < this.x * scale + (float)this.width * scale && (float)mouseY < this.y * scale + (float)this.height * scale;
        GlStateManager.color(1.0F, 1.0F, 1.0F, this.hovered ? 1.0F : 0.5F);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(COLOR_WHEEL);
        this.main.getUtils().drawTexturedRectangle(this.x, this.y, 0.0F, 0.0F, 10.0F, 10.0F, 10.0F, 10.0F, true);
        GlStateManager.popMatrix();
    }

    public static int getSize() {
        return 10;
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        float scale = this.main.getConfigManager().getConfigValues().getGuiScale(this.element);
        return (float)mouseX >= this.x * scale && (float)mouseY >= this.y * scale && (float)mouseX < this.x * scale + (float)this.width * scale && (float)mouseY < this.y * scale + (float)this.height * scale;
    }

    public Element getElement() {
        return this.element;
    }
}
 