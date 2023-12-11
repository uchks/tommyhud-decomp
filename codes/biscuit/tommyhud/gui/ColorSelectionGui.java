package codes.biscuit.tommyhud.gui;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.core.Element;
import codes.biscuit.tommyhud.gui.MainGui;
import codes.biscuit.tommyhud.misc.GUIType;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

public class ColorSelectionGui extends GuiScreen {
    private static final ResourceLocation COLOR_PICKER = new ResourceLocation("tommyhud", "colorpicker.png");
    private TommyHUD main = TommyHUD.getInstance();
    private BufferedImage COLOR_PICKER_IMAGE;
    private Element element;
    private int imageX;
    private int imageY;
    private GuiTextField hexColorField;

    ColorSelectionGui(Element element) {
        this.element = element;

        try {
            this.COLOR_PICKER_IMAGE = TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(COLOR_PICKER).getInputStream());
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void initGui() {
        this.hexColorField = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, this.width / 2 + 110 - 50, 220, 100, 15);
        this.hexColorField.setMaxStringLength(7);
        this.hexColorField.setFocused(true);
        this.setTextBoxHex(this.main.getConfigManager().getConfigValues().getColor(this.element));
        Keyboard.enableRepeatEvents(true);
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int startColor = (new Color(0, 0, 0, 128)).getRGB();
        int endColor = (new Color(0, 0, 0, 192)).getRGB();
        this.func_73733_a(0, 0, this.width, this.height, startColor, endColor);
        MainGui.drawDefaultTitleText(255);
        int defaultBlue = this.main.getUtils().getDefaultColor(255.0F);
        if (this.element.getGuiDefault() != null) {
            int pickerWidth = this.COLOR_PICKER_IMAGE.getWidth();
            int pickerHeight = this.COLOR_PICKER_IMAGE.getHeight();
            this.imageX = this.width / 2 - 200;
            this.imageY = 90;
            if (this.main.getConfigManager().getConfigValues().getChromaFeatures().contains(this.element)) {
                GlStateManager.color(0.5F, 0.5F, 0.5F, 0.7F);
                GlStateManager.enableBlend();
            } else {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }

            this.mc.getTextureManager().bindTexture(COLOR_PICKER);
            Gui.drawModalRectWithCustomSizedTexture(this.imageX, this.imageY, 0.0F, 0.0F, pickerWidth, pickerHeight, (float)pickerWidth, (float)pickerHeight);
            MainGui.drawScaledString("Selected Color", 120, defaultBlue, 1.5D, 75);
            func_73734_a(this.width / 2 + 90, 140, this.width / 2 + 130, 160, this.main.getConfigManager().getConfigValues().getColor(this.element).getRGB());
            if (!this.main.getConfigManager().getConfigValues().getChromaFeatures().contains(this.element)) {
                MainGui.drawScaledString("Set Hex Color", 200, defaultBlue, 1.5D, 75);
                this.hexColorField.drawTextBox();
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (!this.main.getConfigManager().getConfigValues().getChromaFeatures().contains(this.element)) {
            int xPixel = mouseX - this.imageX;
            int yPixel = mouseY - this.imageY;
            if (xPixel > 0 && xPixel < this.COLOR_PICKER_IMAGE.getWidth() && yPixel > 0 && yPixel < this.COLOR_PICKER_IMAGE.getHeight()) {
                Color selectedColor = new Color(this.COLOR_PICKER_IMAGE.getRGB(xPixel, yPixel), true);
                if (selectedColor.getAlpha() == 255) {
                    this.main.getConfigManager().getConfigValues().setColor(this.element, selectedColor.getRGB());
                    this.setTextBoxHex(selectedColor);
                    this.main.getUtils().playSound("gui.button.press", 0.25D, 1.0D);
                }
            }

            this.hexColorField.mouseClicked(mouseX, mouseY, mouseButton);
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void setTextBoxHex(Color color) {
        this.hexColorField.setText(String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (this.hexColorField.isFocused()) {
            this.hexColorField.textboxKeyTyped(typedChar, keyCode);
            String text = this.hexColorField.getText();
            if (text.startsWith("#")) {
                text = text.substring(1);
            }

            if (text.length() == 6) {
                int typedColor;
                try {
                    typedColor = Integer.parseInt(text, 16);
                } catch (NumberFormatException var6) {
                    var6.printStackTrace();
                    return;
                }

                this.main.getConfigManager().getConfigValues().setColor(this.element, typedColor);
            }
        }

    }

    public void updateScreen() {
        this.hexColorField.updateCursorCounter();
        super.updateScreen();
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.main.getListener().setGuiToOpen(GUIType.EDITING);
    }
}
 