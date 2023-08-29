package codes.biscuit.tommyhud.command;

import java.util.*;
import net.minecraft.command.*;
import codes.biscuit.tommyhud.*;
import codes.biscuit.tommyhud.misc.*;

public class TommyHUDCommand extends CommandBase
{
    public String func_71517_b() {
        return "tommyhud";
    }
    
    public List<String> func_71514_a() {
        return Collections.singletonList("th");
    }
    
    public void func_71515_b(final ICommandSender sender, final String[] args) {
        TommyHUD.getInstance().getListener().setGuiToOpen(GUIType.MAIN);
    }
    
    public String func_71518_a(final ICommandSender sender) {
        return null;
    }
    
    public int func_82362_a() {
        return 0;
    }
}
