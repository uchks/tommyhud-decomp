package codes.biscuit.tommyhud.asm.hooks;

import java.util.Map;
import net.minecraft.entity.Entity;

public class MinecraftHook {
    private static long lastError = -1L;

    public static Entity getRenderViewEntity(Entity renderViewEntity) {
        return renderViewEntity;
    }
}