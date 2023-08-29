package codes.biscuit.tommyhud.gui.buttons;

import net.minecraft.client.gui.*;
import codes.biscuit.tommyhud.*;
import net.minecraft.util.*;
import net.minecraft.client.*;

public class ButtonIconText extends GuiButton
{
    private static final int PADDING_TOP_BOTTOM = 10;
    private TommyHUD main;
    private ResourceLocation resourceLocation;
    private int iconSize;
    private int textScale;
    
    public ButtonIconText(final int x, final int y, final int height, final String text, final int textScale, final ResourceLocation resourceLocation) {
        super(0, x, y, text);
        this.main = TommyHUD.getInstance();
        this.resourceLocation = resourceLocation;
        this.textScale = textScale;
        this.iconSize = height;
        this.field_146121_g = height * 10 * 2;
        this.field_146120_f = this.field_146121_g + Minecraft.func_71410_x().field_71466_p.func_78256_a(text) + 10;
    }
    
    public void func_146112_a(final Minecraft mc, final int mouseX, final int mouseY) {
        func_73734_a(this.field_146128_h, this.field_146129_i, this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g, this.main.getUtils().getDefaultColor(128.0f));
        mc.func_110434_K().func_110577_a(this.resourceLocation);
        this.func_73729_b(this.field_146128_h + 10, this.field_146129_i + 10, 0, 0, this.iconSize, this.iconSize);
        mc.field_71466_p.func_78276_b(this.field_146126_j, this.field_146128_h + 10 + this.iconSize + 10, 10, -16777216);
    }
}
