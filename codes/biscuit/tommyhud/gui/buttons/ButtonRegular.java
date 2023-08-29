package codes.biscuit.tommyhud.gui.buttons;

import net.minecraft.client.gui.*;
import codes.biscuit.tommyhud.*;
import net.minecraft.client.*;
import codes.biscuit.tommyhud.gui.*;
import net.minecraft.client.renderer.*;
import java.awt.*;

public class ButtonRegular extends GuiButton
{
    private TommyHUD main;
    private long timeOpened;
    private Runnable onClickRunnable;
    
    public ButtonRegular(final double x, final double y, final int width, final int height, final String buttonText, final Runnable onClickRunnable) {
        super(0, (int)x, (int)y, buttonText);
        this.main = TommyHUD.getInstance();
        this.timeOpened = System.currentTimeMillis();
        this.field_146120_f = width;
        this.field_146121_g = height;
        this.onClickRunnable = onClickRunnable;
    }
    
    public void func_146112_a(final Minecraft mc, final int mouseX, final int mouseY) {
        float alphaMultiplier = 1.0f;
        int alpha;
        if (MainGui.isFadingIn()) {
            final long timeSinceOpen = System.currentTimeMillis() - this.timeOpened;
            final int fadeMilis = 500;
            if (timeSinceOpen <= fadeMilis) {
                alphaMultiplier = timeSinceOpen / (float)fadeMilis;
            }
            alpha = (int)(255.0f * alphaMultiplier);
        }
        else {
            alpha = 255;
        }
        this.field_146123_n = (mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g);
        int boxAlpha = 100;
        if (this.field_146123_n) {
            boxAlpha = 170;
        }
        boxAlpha *= (int)alphaMultiplier;
        final int boxColor = this.main.getUtils().getDefaultColor((float)boxAlpha);
        GlStateManager.func_179147_l();
        if (alpha < 4) {
            alpha = 4;
        }
        int fontColor = new Color(224, 224, 224, alpha).getRGB();
        if (this.field_146123_n) {
            fontColor = new Color(255, 255, 160, alpha).getRGB();
        }
        this.drawButtonBoxAndText(boxColor, 1.0f, fontColor);
    }
    
    private void drawButtonBoxAndText(final int boxColor, final float scale, final int fontColor) {
        func_73734_a(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, boxColor);
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a(scale, scale, 1.0f);
        this.main.getUtils().drawCenteredString(this.field_146126_j, (this.field_146128_h + this.field_146120_f / 2) / scale, (this.field_146129_i + (this.field_146121_g - 8.0f * scale) / 2.0f) / scale, fontColor);
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }
    
    public boolean func_146116_c(final Minecraft mc, final int mouseX, final int mouseY) {
        final boolean pressed = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
        if (pressed) {
            this.onClickRunnable.run();
        }
        return pressed;
    }
}
