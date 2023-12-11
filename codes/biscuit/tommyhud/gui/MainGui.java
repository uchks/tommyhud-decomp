package codes.biscuit.tommyhud.gui;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.core.CrazyMode;
import codes.biscuit.tommyhud.gui.MainGui;
import codes.biscuit.tommyhud.gui.buttons.ButtonCustomizable;
import codes.biscuit.tommyhud.gui.buttons.ButtonEditLocation;
import java.awt.Color;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class MainGui extends GuiScreen {
    private static Set<CrazyMode> revealedCrazyModes = EnumSet.noneOf(CrazyMode.class);
    private static boolean fadingIn = true;
    private TommyHUD main = TommyHUD.getInstance();
    private long timeOpened = System.currentTimeMillis();
    private int number;
    private int y;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0F / (float)scaledResolution.getScaleFactor(), 1.0F / (float)scaledResolution.getScaleFactor(), 1.0F);
        long timeSinceOpen = System.currentTimeMillis() - this.timeOpened;
        float alphaMultiplier = 0.5F;
        if (fadingIn) {
            int fadeMilis = 500;
            if (timeSinceOpen <= (long)fadeMilis) {
                alphaMultiplier = (float)timeSinceOpen / (float)(fadeMilis * 2);
            }
        }

        int alpha = (int)(255.0F * alphaMultiplier);
        int startColor = (new Color(0, 0, 0, (int)((double)alpha * 0.5D))).getRGB();
        int endColor = (new Color(0, 0, 0, alpha)).getRGB();
        this.func_73733_a(0, 0, this.mc.displayWidth, this.mc.displayHeight, startColor, endColor);
        GlStateManager.enableBlend();
        if (alpha < 4) {
            alpha = 4;
        }

        drawDefaultTitleText(alpha * 2);
        drawScaledString("Crazy Modes", 180, TommyHUD.getInstance().getUtils().getDefaultColor((float)(alpha * 2)), 4.0D, 0);
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
    }

    public void initGui() {
        this.buttonList.add(new ButtonEditLocation());
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

    public void addButton(CrazyMode crazyMode, String name) {
        float middleX = (float)this.mc.displayWidth / 2.0F;
        int width = 600;
        int height = 50;
        int currentNumber = this.number;
        this.buttonList.add(new ButtonCustomizable((double)(middleX - (float)width / 2.0F), (double)this.y, width, height, () -> {
            if (this.main.getConfigManager().getConfigValues().getEnabledCrazyModes().contains(crazyMode)) {
                this.main.getConfigManager().getConfigValues().getEnabledCrazyModes().remove(crazyMode);
            } else {
                this.main.getConfigManager().getConfigValues().getEnabledCrazyModes().add(crazyMode);
                revealedCrazyModes.add(crazyMode);
            }

        }, () -> this.main.getConfigManager().getConfigValues().getEnabledCrazyModes().contains(crazyMode) ? -10551404 : this.main.getUtils().getDefaultColor(255.0F), () -> revealedCrazyModes.contains(crazyMode) ? name : "Mystery Mode " + currentNumber));
        this.y += 80;
        ++this.number;
    }

    public static void setFadingIn(boolean fadingIn) {
        MainGui.fadingIn = fadingIn;
    }

    public static boolean isFadingIn() {
        return fadingIn;
    }

    public static void drawDefaultTitleText(int alpha) {
        int defaultBlue = TommyHUD.getInstance().getUtils().getDefaultColor((float)alpha);
        drawScaledString("TommyHUD", 50, defaultBlue, 9.0D, 0);
    }

    static void drawScaledString(String text, int y, int color, double scale, int xOffset) {
        drawScaledString(text, y, color, scale, xOffset, true);
    }

    public static void drawScaledString(String text, int y, int color, double scale, int xOffset, boolean centered) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0D);
        if (centered) {
            TommyHUD.getInstance().getUtils().drawCenteredString(text, (float)(Math.round((double)((float)Minecraft.getMinecraft().displayWidth / 2.0F) / scale) + (long)xOffset), (float)Math.round((double)((float)y) / scale), color);
        } else {
            Minecraft.getMinecraft().fontRendererObj.drawString(text, (float)(Math.round((double)((float)Minecraft.getMinecraft().displayWidth / 2.0F) / scale) + (long)xOffset), (float)Math.round((double)((float)y) / scale), color, true);
        }

        GlStateManager.popMatrix();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);
        mouseX *= scaledResolution.getScaleFactor();
        mouseY *= scaledResolution.getScaleFactor();
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
 