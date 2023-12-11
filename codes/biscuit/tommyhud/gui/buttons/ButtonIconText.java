package codes.biscuit.tommyhud.gui.buttons;

import codes.biscuit.tommyhud.TommyHUD;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class ButtonIconText extends GuiButton {
    private static final int PADDING_TOP_BOTTOM = 10;
    private TommyHUD main = TommyHUD.getInstance();
    private ResourceLocation resourceLocation;
    private int iconSize;
    private int textScale;

    public ButtonIconText(int x, int y, int height, String text, int textScale, ResourceLocation resourceLocation) {
        super(0, x, y, text);
        this.resourceLocation = resourceLocation;
        this.textScale = textScale;
        this.iconSize = height;
        this.height = height * 10 * 2;
        this.width = this.height + Minecraft.getMinecraft().fontRendererObj.getStringWidth(text) + 10;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        func_73734_a(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.main.getUtils().getDefaultColor(128.0F));
        mc.getTextureManager().bindTexture(this.resourceLocation);
        this.func_73729_b(this.xPosition + 10, this.yPosition + 10, 0, 0, this.iconSize, this.iconSize);
        mc.fontRendererObj.drawString(this.displayString, this.xPosition + 10 + this.iconSize + 10, 10, -16777216);
    }
}