package codes.biscuit.tommyhud.gui.elements;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.gui.elements.CheckBox;
import codes.biscuit.tommyhud.util.ColorCode;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class CheckBox {
    private static final ResourceLocation CHECK_BOX = new ResourceLocation("tommyhud", "checkbox.png");
    private TommyHUD main = TommyHUD.getInstance();
    private static final int ICON_SIZE = 16;
    private final float scale;
    private final Minecraft mc;
    private final int x;
    private final int y;
    private final String text;
    private final int textWidth;
    private final int size;
    private boolean value;
    private CheckBox.OnToggleListener onToggleListener;

    public CheckBox(Minecraft mc, int x, int y, int size, String text, boolean value) {
        this(mc, x, y, size, text);
        this.value = value;
    }

    CheckBox(Minecraft mc, int x, int y, int size, String text) {
        this.mc = mc;
        this.x = x;
        this.y = y;
        this.scale = (float)size / 16.0F;
        this.text = text;
        this.textWidth = mc.fontRendererObj.getStringWidth(text);
        this.size = size;
    }

    public void draw() {
        int scaledX = Math.round((float)this.x / this.scale);
        int scaledY = Math.round((float)this.y / this.scale);
        GlStateManager.pushMatrix();
        GlStateManager.scale(this.scale, this.scale, 1.0F);
        int color = this.value ? ColorCode.WHITE.getRGB() : ColorCode.GRAY.getRGB();
        this.main.getUtils().drawTextWithStyle(this.text, (float)(scaledX + Math.round((float)this.size * 1.5F / this.scale)), (float)(scaledY + this.size / 2), color);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().getTextureManager().bindTexture(CHECK_BOX);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.value) {
            this.mc.ingameGUI.func_73729_b(scaledX, scaledY, 49, 34, 16, 16);
        } else {
            this.mc.ingameGUI.func_73729_b(scaledX, scaledY, 33, 34, 16, 16);
        }

        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && mouseX > this.x && mouseX < this.x + this.size + this.textWidth && mouseY > this.y && mouseY < this.y + this.size) {
            this.value = !this.value;
            this.main.getUtils().playSound("gui.button.press", 0.25D, 1.0D);
            if (this.onToggleListener != null) {
                this.onToggleListener.onToggle(this.value);
            }
        }

    }

    public void setValue(boolean value) {
        this.value = value;
    }

    boolean getValue() {
        return this.value;
    }

    public void setOnToggleListener(CheckBox.OnToggleListener listener) {
        this.onToggleListener = listener;
    }

    @FunctionalInterface
    public interface OnToggleListener {
        void onToggle(boolean var1);
    }
}