package codes.biscuit.tommyhud.asm.hooks;

import net.minecraft.entity.*;

public class MinecraftHook
{
    private static long lastError;
    
    public static Entity getRenderViewEntity(final Entity renderViewEntity) {
        return renderViewEntity;
    }
    
    static {
        MinecraftHook.lastError = -1L;
    }
}
