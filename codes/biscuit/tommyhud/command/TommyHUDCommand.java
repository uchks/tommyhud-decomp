package codes.biscuit.tommyhud.command;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.misc.GUIType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class TommyHUDCommand extends CommandBase {
    public String getCommandName() {
        return "tommyhud";
    }

    public List<String> getCommandAliases() {
        return Collections.singletonList("th");
    }

    public void processCommand(ICommandSender sender, String[] args) {
        TommyHUD.getInstance().getListener().setGuiToOpen(GUIType.MAIN);
    }

    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }
}