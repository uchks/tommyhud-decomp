package codes.biscuit.tommyhud.gui.buttons;

import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import codes.biscuit.tommyhud.*;
import codes.biscuit.tommyhud.core.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;

public class ButtonColorWheel extends GuiButton
{
    private static final ResourceLocation COLOR_WHEEL;
    private static final int SIZE = 10;
    private TommyHUD main;
    private Element element;
    public float x;
    public float y;
    
    public ButtonColorWheel(final float x, final float y, final Element element) {
        super(0, 0, 0, "");
        this.main = TommyHUD.getInstance();
        this.element = element;
        this.field_146120_f = 10;
        this.field_146121_g = 10;
        this.x = x;
        this.y = y;
    }
    
    public void func_146112_a(final Minecraft mc, final int mouseX, final int mouseY) {
        final float scale = this.main.getConfigManager().getConfigValues().getGuiScale(this.element);
        this.field_146123_n = (mouseX >= this.x * scale && mouseY >= this.y * scale && mouseX < this.x * scale + this.field_146120_f * scale && mouseY < this.y * scale + this.field_146121_g * scale);
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, this.field_146123_n ? 1.0f : 0.5f);
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a(scale, scale, 1.0f);
        GlStateManager.func_179147_l();
        mc.func_110434_K().func_110577_a(ButtonColorWheel.COLOR_WHEEL);
        this.main.getUtils().drawTexturedRectangle(this.x, this.y, 0.0f, 0.0f, 10.0f, 10.0f, 10.0f, 10.0f, true);
        GlStateManager.func_179121_F();
    }
    
    public static int getSize() {
        return 10;
    }
    
    public boolean func_146116_c(final Minecraft mc, final int mouseX, final int mouseY) {
        final float scale = this.main.getConfigManager().getConfigValues().getGuiScale(this.element);
        return mouseX >= this.x * scale && mouseY >= this.y * scale && mouseX < this.x * scale + this.field_146120_f * scale && mouseY < this.y * scale + this.field_146121_g * scale;
    }
    
    public Element getElement() {
        return this.element;
    }
    
    static {
        COLOR_WHEEL = new ResourceLocation("tommyhud", "colorwheel.png");
    }
}
