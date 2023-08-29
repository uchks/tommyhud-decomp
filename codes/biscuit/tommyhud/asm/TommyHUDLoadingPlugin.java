package codes.biscuit.tommyhud.asm;

import net.minecraftforge.fml.relauncher.*;
import java.util.*;

@IFMLLoadingPlugin.MCVersion("1.8.9")
public class TommyHUDLoadingPlugin implements IFMLLoadingPlugin
{
    public String[] getASMTransformerClass() {
        return new String[] { TommyHUDTransformer.class.getName() };
    }
    
    public String getModContainerClass() {
        return null;
    }
    
    public String getSetupClass() {
        return null;
    }
    
    public void injectData(final Map<String, Object> data) {
    }
    
    public String getAccessTransformerClass() {
        return null;
    }
}
