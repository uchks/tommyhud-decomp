package codes.biscuit.tommyhud.gui.buttons;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.misc.GUIType;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ButtonEditLocation extends GuiButton {
    private static final ResourceLocation MOVE = new ResourceLocation("tommyhud", "move.png");
    private static final int PADDING_TOP_BOTTOM = 30;
    private static final int ICON_SIZE = 80;
    private static final int STRING_SCALE = 3;
    private TommyHUD main = TommyHUD.getInstance();

    public ButtonEditLocation() {
        super(0, 0, 0, "Edit Locations");
        this.height = 140;
        this.width = this.height + Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.displayString) * 3 + 30;
    }

    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        this.xPosition = mc.displayWidth - this.width - 30;
        this.yPosition = mc.displayHeight - this.height - 30;
        func_73734_a(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, this.main.getUtils().getDefaultColor(128.0F));
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(MOVE);
        func_146110_a(this.xPosition + 30, this.yPosition + 30, 0.0F, 0.0F, 80, 80, 80.0F, 80.0F);
        float scale = 3.0F;
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        int stringX = this.xPosition + 30 + 80 + 30;
        int stringY = this.yPosition + this.height / 2 - mc.fontRendererObj.FONT_HEIGHT;
        stringX = (int)((float)stringX / scale);
        stringY = (int)((float)stringY / scale);
        mc.fontRendererObj.drawString(this.displayString, stringX, stringY, -1);
        GlStateManager.popMatrix();
    }

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        boolean pressed = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        if (pressed) {
            this.main.getListener().setGuiToOpen(GUIType.EDITING);
        }

        return pressed;
    }
}