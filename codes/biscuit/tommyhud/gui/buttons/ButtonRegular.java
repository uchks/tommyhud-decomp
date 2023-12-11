package codes.biscuit.tommyhud.gui.buttons;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.gui.MainGui;
import java.awt.Color;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class ButtonRegular extends GuiButton {
    private TommyHUD main = TommyHUD.getInstance();
    private long timeOpened = System.currentTimeMillis();
    private Runnable onClickRunnable;

    public ButtonRegular(double x, double y, int width, int height, String buttonText, Runnable onClickRunnable) {
        super(0, (int)x, (int)y, buttonText);
        this.width = width;
        this.height = height;
        this.onClickRunnable = onClickRunnable;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        float alphaMultiplier = 1.0F;
        int alpha;
        if (MainGui.isFadingIn()) {
            long timeSinceOpen = System.currentTimeMillis() - this.timeOpened;
            int fadeMilis = 500;
            if (timeSinceOpen <= (long)fadeMilis) {
                alphaMultiplier = (float)timeSinceOpen / (float)fadeMilis;
            }

            alpha = (int)(255.0F * alphaMultiplier);
        } else {
            alpha = 255;
        }

        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        int boxAlpha = 100;
        if (this.hovered) {
            boxAlpha = 170;
        }

        boxAlpha = (int)((float)boxAlpha * alphaMultiplier);
        int boxColor = this.main.getUtils().getDefaultColor((float)boxAlpha);
        GlStateManager.enableBlend();
        if (alpha < 4) {
            alpha = 4;
        }

        int fontColor = (new Color(224, 224, 224, alpha)).getRGB();
        if (this.hovered) {
            fontColor = (new Color(255, 255, 160, alpha)).getRGB();
        }

        this.drawButtonBoxAndText(boxColor, 1.0F, fontColor);
    }

    private void drawButtonBoxAndText(int boxColor, float scale, int fontColor) {
        func_73734_a(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, boxColor);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        this.main.getUtils().drawCenteredString(this.displayString, (float)(this.xPosition + this.width / 2) / scale, ((float)this.yPosition + ((float)this.height - 8.0F * scale) / 2.0F) / scale, fontColor);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        boolean pressed = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        if (pressed) {
            this.onClickRunnable.run();
        }

        return pressed;
    }
}
 