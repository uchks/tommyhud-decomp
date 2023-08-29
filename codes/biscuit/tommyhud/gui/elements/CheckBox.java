package codes.biscuit.tommyhud.gui.elements;

import net.minecraft.util.*;
import codes.biscuit.tommyhud.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import codes.biscuit.tommyhud.util.*;

public class CheckBox
{
    private static final ResourceLocation CHECK_BOX;
    private TommyHUD main;
    private static final int ICON_SIZE = 16;
    private final float scale;
    private final Minecraft mc;
    private final int x;
    private final int y;
    private final String text;
    private final int textWidth;
    private final int size;
    private boolean value;
    private OnToggleListener onToggleListener;
    
    public CheckBox(final Minecraft mc, final int x, final int y, final int size, final String text, final boolean value) {
        this(mc, x, y, size, text);
        this.value = value;
    }
    
    CheckBox(final Minecraft mc, final int x, final int y, final int size, final String text) {
        this.main = TommyHUD.getInstance();
        this.mc = mc;
        this.x = x;
        this.y = y;
        this.scale = size / 16.0f;
        this.text = text;
        this.textWidth = mc.field_71466_p.func_78256_a(text);
        this.size = size;
    }
    
    public void draw() {
        final int scaledX = Math.round(this.x / this.scale);
        final int scaledY = Math.round(this.y / this.scale);
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a(this.scale, this.scale, 1.0f);
        final int color = this.value ? ColorCode.WHITE.getRGB() : ColorCode.GRAY.getRGB();
        this.main.getUtils().drawTextWithStyle(this.text, (float)(scaledX + Math.round(this.size * 1.5f / this.scale)), (float)(scaledY + this.size / 2), color);
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        Minecraft.func_71410_x().func_110434_K().func_110577_a(CheckBox.CHECK_BOX);
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.value) {
            this.mc.field_71456_v.func_73729_b(scaledX, scaledY, 49, 34, 16, 16);
        }
        else {
            this.mc.field_71456_v.func_73729_b(scaledX, scaledY, 33, 34, 16, 16);
        }
        GlStateManager.func_179126_j();
        GlStateManager.func_179121_F();
    }
    
    public void onMouseClick(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0 && mouseX > this.x && mouseX < this.x + this.size + this.textWidth && mouseY > this.y && mouseY < this.y + this.size) {
            this.value = !this.value;
            this.main.getUtils().playSound("gui.button.press", 0.25, 1.0);
            if (this.onToggleListener != null) {
                this.onToggleListener.onToggle(this.value);
            }
        }
    }
    
    public void setValue(final boolean value) {
        this.value = value;
    }
    
    boolean getValue() {
        return this.value;
    }
    
    public void setOnToggleListener(final OnToggleListener listener) {
        this.onToggleListener = listener;
    }
    
    static {
        CHECK_BOX = new ResourceLocation("tommyhud", "checkbox.png");
    }
    
    @FunctionalInterface
    public interface OnToggleListener
    {
        void onToggle(final boolean p0);
    }
}
