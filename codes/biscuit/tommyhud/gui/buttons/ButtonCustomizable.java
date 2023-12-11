package codes.biscuit.tommyhud.gui.buttons;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.gui.MainGui;
import java.awt.Color;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class ButtonCustomizable extends GuiButton {
    private TommyHUD main = TommyHUD.getInstance();
    private long timeOpened = System.currentTimeMillis();
    private Runnable onClickRunnable;
    private Supplier<Integer> colorSupplier;
    private Supplier<String> textSupplier;

    public ButtonCustomizable(double x, double y, int width, int height, Runnable onClickRunnable, Supplier<Integer> colorSupplier, Supplier<String> textSupplier) {
        super(0, (int)x, (int)y, "");
        this.width = width;
        this.height = height;
        this.onClickRunnable = onClickRunnable;
        this.colorSupplier = colorSupplier;
        this.textSupplier = textSupplier;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        mouseX *= scaledResolution.getScaleFactor();
        mouseY *= scaledResolution.getScaleFactor();
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
        int boxColor = this.main.getUtils().getColorWithAlpha(this.colorSupplier.get(), boxAlpha);
        GlStateManager.enableBlend();
        if (alpha < 4) {
            alpha = 4;
        }

        int fontColor = (new Color(224, 224, 224, alpha)).getRGB();
        if (this.hovered) {
            fontColor = (new Color(255, 255, 160, alpha)).getRGB();
        }

        this.drawButtonBoxAndText(boxColor, 3.0F, fontColor);
    }

    private void drawButtonBoxAndText(int boxColor, float scale, int fontColor) {
        func_73734_a(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, boxColor);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        this.displayString = (String)this.textSupplier.get();
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
 