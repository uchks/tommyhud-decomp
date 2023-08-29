package codes.biscuit.tommyhud.gui;

import net.minecraft.util.*;
import codes.biscuit.tommyhud.*;
import java.awt.image.*;
import codes.biscuit.tommyhud.core.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.texture.*;
import java.io.*;
import org.lwjgl.input.*;
import java.awt.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.gui.*;
import codes.biscuit.tommyhud.misc.*;

public class ColorSelectionGui extends GuiScreen
{
    private static final ResourceLocation COLOR_PICKER;
    private TommyHUD main;
    private BufferedImage COLOR_PICKER_IMAGE;
    private Element element;
    private int imageX;
    private int imageY;
    private GuiTextField hexColorField;
    
    ColorSelectionGui(final Element element) {
        this.main = TommyHUD.getInstance();
        this.element = element;
        try {
            this.COLOR_PICKER_IMAGE = TextureUtil.func_177053_a(Minecraft.func_71410_x().func_110442_L().func_110536_a(ColorSelectionGui.COLOR_PICKER).func_110527_b());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void func_73866_w_() {
        (this.hexColorField = new GuiTextField(0, Minecraft.func_71410_x().field_71466_p, this.field_146294_l / 2 + 110 - 50, 220, 100, 15)).func_146203_f(7);
        this.hexColorField.func_146195_b(true);
        this.setTextBoxHex(this.main.getConfigManager().getConfigValues().getColor(this.element));
        Keyboard.enableRepeatEvents(true);
        super.func_73866_w_();
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        final int startColor = new Color(0, 0, 0, 128).getRGB();
        final int endColor = new Color(0, 0, 0, 192).getRGB();
        this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, startColor, endColor);
        MainGui.drawDefaultTitleText(255);
        final int defaultBlue = this.main.getUtils().getDefaultColor(255.0f);
        if (this.element.getGuiDefault() != null) {
            final int pickerWidth = this.COLOR_PICKER_IMAGE.getWidth();
            final int pickerHeight = this.COLOR_PICKER_IMAGE.getHeight();
            this.imageX = this.field_146294_l / 2 - 200;
            this.imageY = 90;
            if (this.main.getConfigManager().getConfigValues().getChromaFeatures().contains(this.element)) {
                GlStateManager.func_179131_c(0.5f, 0.5f, 0.5f, 0.7f);
                GlStateManager.func_179147_l();
            }
            else {
                GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            }
            this.field_146297_k.func_110434_K().func_110577_a(ColorSelectionGui.COLOR_PICKER);
            Gui.func_146110_a(this.imageX, this.imageY, 0.0f, 0.0f, pickerWidth, pickerHeight, (float)pickerWidth, (float)pickerHeight);
            MainGui.drawScaledString("Selected Color", 120, defaultBlue, 1.5, 75);
            func_73734_a(this.field_146294_l / 2 + 90, 140, this.field_146294_l / 2 + 130, 160, this.main.getConfigManager().getConfigValues().getColor(this.element).getRGB());
            if (!this.main.getConfigManager().getConfigValues().getChromaFeatures().contains(this.element)) {
                MainGui.drawScaledString("Set Hex Color", 200, defaultBlue, 1.5, 75);
                this.hexColorField.func_146194_f();
            }
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (!this.main.getConfigManager().getConfigValues().getChromaFeatures().contains(this.element)) {
            final int xPixel = mouseX - this.imageX;
            final int yPixel = mouseY - this.imageY;
            if (xPixel > 0 && xPixel < this.COLOR_PICKER_IMAGE.getWidth() && yPixel > 0 && yPixel < this.COLOR_PICKER_IMAGE.getHeight()) {
                final Color selectedColor = new Color(this.COLOR_PICKER_IMAGE.getRGB(xPixel, yPixel), true);
                if (selectedColor.getAlpha() == 255) {
                    this.main.getConfigManager().getConfigValues().setColor(this.element, selectedColor.getRGB());
                    this.setTextBoxHex(selectedColor);
                    this.main.getUtils().playSound("gui.button.press", 0.25, 1.0);
                }
            }
            this.hexColorField.func_146192_a(mouseX, mouseY, mouseButton);
        }
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }
    
    private void setTextBoxHex(final Color color) {
        this.hexColorField.func_146180_a(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        super.func_73869_a(typedChar, keyCode);
        if (this.hexColorField.func_146206_l()) {
            this.hexColorField.func_146201_a(typedChar, keyCode);
            String text = this.hexColorField.func_146179_b();
            if (text.startsWith("#")) {
                text = text.substring(1);
            }
            if (text.length() == 6) {
                int typedColor;
                try {
                    typedColor = Integer.parseInt(text, 16);
                }
                catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    return;
                }
                this.main.getConfigManager().getConfigValues().setColor(this.element, typedColor);
            }
        }
    }
    
    public void func_73876_c() {
        this.hexColorField.func_146178_a();
        super.func_73876_c();
    }
    
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
        this.main.getListener().setGuiToOpen(GUIType.EDITING);
    }
    
    static {
        COLOR_PICKER = new ResourceLocation("tommyhud", "colorpicker.png");
    }
}
