package codes.biscuit.tommyhud.gui;

import codes.biscuit.tommyhud.core.*;
import codes.biscuit.tommyhud.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import java.awt.*;
import codes.biscuit.tommyhud.gui.buttons.*;
import net.minecraft.client.*;
import java.io.*;
import java.util.*;

public class MainGui extends GuiScreen
{
    private static Set<CrazyMode> revealedCrazyModes;
    private static boolean fadingIn;
    private TommyHUD main;
    private long timeOpened;
    private int number;
    private int y;
    
    public MainGui() {
        this.main = TommyHUD.getInstance();
        this.timeOpened = System.currentTimeMillis();
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a(1.0f / scaledResolution.func_78325_e(), 1.0f / scaledResolution.func_78325_e(), 1.0f);
        final long timeSinceOpen = System.currentTimeMillis() - this.timeOpened;
        float alphaMultiplier = 0.5f;
        if (MainGui.fadingIn) {
            final int fadeMilis = 500;
            if (timeSinceOpen <= fadeMilis) {
                alphaMultiplier = timeSinceOpen / (float)(fadeMilis * 2);
            }
        }
        int alpha = (int)(255.0f * alphaMultiplier);
        final int startColor = new Color(0, 0, 0, (int)(alpha * 0.5)).getRGB();
        final int endColor = new Color(0, 0, 0, alpha).getRGB();
        this.func_73733_a(0, 0, this.field_146297_k.field_71443_c, this.field_146297_k.field_71440_d, startColor, endColor);
        GlStateManager.func_179147_l();
        if (alpha < 4) {
            alpha = 4;
        }
        drawDefaultTitleText(alpha * 2);
        drawScaledString("Crazy Modes", 180, TommyHUD.getInstance().getUtils().getDefaultColor((float)(alpha * 2)), 4.0, 0);
        super.func_73863_a(mouseX, mouseY, partialTicks);
        GlStateManager.func_179121_F();
    }
    
    public void func_73866_w_() {
        this.field_146292_n.add(new ButtonEditLocation());
        this.number = 1;
        this.y = 240;
        this.addButton(CrazyMode.LARGE_GUI_AND_CROSSHAIR, "Large Scale Elements & Crosshair");
        this.addButton(CrazyMode.ROCKING_BOAT_MODE, "Rocking Boat Mode");
        this.addButton(CrazyMode.HELICOPTER_CROSSHAIR, "Helicopter Crosshair...");
        this.addButton(CrazyMode.CRAZY_CHROMA_MODE, "Chroma Chroma Chroma!");
        this.addButton(CrazyMode.EARTHQUAKE_MODE, "Earthquake Mode o.O");
        this.addButton(CrazyMode.JERRY_MODE, "Everything Is Now a Jerry");
        this.addButton(CrazyMode.UPSIDE_DOWN_MODE, "Upside Down HUD Mode");
        this.addButton(CrazyMode.GLITCHY_MODE, "Glitchy Text Mode");
        this.addButton(CrazyMode.TINY_GUI, "Tiny GUI Mode");
        this.addButton(CrazyMode.CHROMA_SCREEN_MODE, "Chroma Screen :O");
    }
    
    public void addButton(final CrazyMode crazyMode, final String name) {
        final float middleX = this.field_146297_k.field_71443_c / 2.0f;
        final int width = 600;
        final int height = 50;
        final int currentNumber = this.number;
        final int i;
        this.field_146292_n.add(new ButtonCustomizable(middleX - width / 2.0f, this.y, width, height, () -> {
            if (this.main.getConfigManager().getConfigValues().getEnabledCrazyModes().contains(crazyMode)) {
                this.main.getConfigManager().getConfigValues().getEnabledCrazyModes().remove(crazyMode);
            }
            else {
                this.main.getConfigManager().getConfigValues().getEnabledCrazyModes().add(crazyMode);
                MainGui.revealedCrazyModes.add(crazyMode);
            }
            return;
        }, () -> {
            if (this.main.getConfigManager().getConfigValues().getEnabledCrazyModes().contains(crazyMode)) {
                return Integer.valueOf(-10551404);
            }
            else {
                return Integer.valueOf(this.main.getUtils().getDefaultColor(255.0f));
            }
        }, () -> {
            if (MainGui.revealedCrazyModes.contains(crazyMode)) {
                return name;
            }
            else {
                return "Mystery Mode " + i;
            }
        }));
        this.y += 80;
        ++this.number;
    }
    
    public static void setFadingIn(final boolean fadingIn) {
        MainGui.fadingIn = fadingIn;
    }
    
    public static boolean isFadingIn() {
        return MainGui.fadingIn;
    }
    
    public static void drawDefaultTitleText(final int alpha) {
        final int defaultBlue = TommyHUD.getInstance().getUtils().getDefaultColor((float)alpha);
        drawScaledString("TommyHUD", 50, defaultBlue, 9.0, 0);
    }
    
    static void drawScaledString(final String text, final int y, final int color, final double scale, final int xOffset) {
        drawScaledString(text, y, color, scale, xOffset, true);
    }
    
    public static void drawScaledString(final String text, final int y, final int color, final double scale, final int xOffset, final boolean centered) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179139_a(scale, scale, 1.0);
        if (centered) {
            TommyHUD.getInstance().getUtils().drawCenteredString(text, (float)(Math.round(Minecraft.func_71410_x().field_71443_c / 2.0f / scale) + xOffset), (float)Math.round((float)y / scale), color);
        }
        else {
            Minecraft.func_71410_x().field_71466_p.func_175065_a(text, (float)(Math.round(Minecraft.func_71410_x().field_71443_c / 2.0f / scale) + xOffset), (float)Math.round((float)y / scale), color, true);
        }
        GlStateManager.func_179121_F();
    }
    
    protected void func_73864_a(int mouseX, int mouseY, final int mouseButton) throws IOException {
        final ScaledResolution scaledResolution = new ScaledResolution(this.field_146297_k);
        mouseX *= scaledResolution.func_78325_e();
        mouseY *= scaledResolution.func_78325_e();
        super.func_73864_a(mouseX, mouseY, mouseButton);
    }
    
    static {
        MainGui.revealedCrazyModes = EnumSet.noneOf(CrazyMode.class);
        MainGui.fadingIn = true;
    }
}
