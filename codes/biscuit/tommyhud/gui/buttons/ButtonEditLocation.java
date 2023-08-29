package codes.biscuit.tommyhud.gui.buttons;

import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import codes.biscuit.tommyhud.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import codes.biscuit.tommyhud.misc.*;

public class ButtonEditLocation extends GuiButton
{
    private static final ResourceLocation MOVE;
    private static final int PADDING_TOP_BOTTOM = 30;
    private static final int ICON_SIZE = 80;
    private static final int STRING_SCALE = 3;
    private TommyHUD main;
    
    public ButtonEditLocation() {
        super(0, 0, 0, "Edit Locations");
        this.main = TommyHUD.getInstance();
        this.field_146121_g = 140;
        this.field_146120_f = this.field_146121_g + Minecraft.func_71410_x().field_71466_p.func_78256_a(this.field_146126_j) * 3 + 30;
    }
    
    public void func_146112_a(final Minecraft mc, final int mouseX, final int mouseY) {
        this.field_146128_h = mc.field_71443_c - this.field_146120_f - 30;
        this.field_146129_i = mc.field_71440_d - this.field_146121_g - 30;
        func_73734_a(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, this.main.getUtils().getDefaultColor(128.0f));
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        mc.func_110434_K().func_110577_a(ButtonEditLocation.MOVE);
        func_146110_a(this.field_146128_h + 30, this.field_146129_i + 30, 0.0f, 0.0f, 80, 80, 80.0f, 80.0f);
        final float scale = 3.0f;
        GlStateManager.func_179094_E();
        GlStateManager.func_179152_a(scale, scale, 1.0f);
        int stringX = this.field_146128_h + 30 + 80 + 30;
        int stringY = this.field_146129_i + this.field_146121_g / 2 - mc.field_71466_p.field_78288_b;
        stringX /= (int)scale;
        stringY /= (int)scale;
        mc.field_71466_p.func_78276_b(this.field_146126_j, stringX, stringY, -1);
        GlStateManager.func_179121_F();
    }
    
    public boolean func_146116_c(final Minecraft mc, final int mouseX, final int mouseY) {
        final boolean pressed = mouseX >= this.field_146128_h && mouseY >= this.field_146129_i && mouseX < this.field_146128_h + this.field_146120_f && mouseY < this.field_146129_i + this.field_146121_g;
        if (pressed) {
            this.main.getListener().setGuiToOpen(GUIType.EDITING);
        }
        return pressed;
    }
    
    static {
        MOVE = new ResourceLocation("tommyhud", "move.png");
    }
}
