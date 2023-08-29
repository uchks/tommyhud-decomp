package codes.biscuit.tommyhud.asm.hooks;

import net.minecraft.client.multiplayer.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import codes.biscuit.tommyhud.*;
import codes.biscuit.tommyhud.core.*;
import net.minecraft.entity.passive.*;
import codes.biscuit.tommyhud.asm.*;
import net.minecraft.client.renderer.entity.*;
import java.lang.reflect.*;
import net.minecraft.client.network.*;
import net.minecraft.world.*;
import net.minecraft.profiler.*;

public class RenderGlobalHook
{
    public static WorldClient DUMMY_WORLD;
    private static long lastError;
    
    public static boolean renderEntitySimple(final RenderManager renderManager, final Entity entity, final float partialTicks) {
        if (entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand) && !entity.func_98034_c((EntityPlayer)Minecraft.func_71410_x().field_71439_g) && TommyHUD.getInstance().getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.JERRY_MODE)) {
            try {
                final EntityVillager entityVillager = new EntityVillager((World)RenderGlobalHook.DUMMY_WORLD);
                final EntityLivingBase oldEntity = (EntityLivingBase)entity;
                entityVillager.field_70165_t = oldEntity.field_70165_t;
                entityVillager.field_70163_u = oldEntity.field_70163_u;
                entityVillager.field_70161_v = oldEntity.field_70161_v;
                entityVillager.field_70169_q = oldEntity.field_70169_q;
                entityVillager.field_70167_r = oldEntity.field_70167_r;
                entityVillager.field_70166_s = oldEntity.field_70166_s;
                entityVillager.field_70125_A = oldEntity.field_70125_A;
                entityVillager.field_70177_z = oldEntity.field_70177_z;
                entityVillager.field_70127_C = oldEntity.field_70127_C;
                entityVillager.field_70126_B = oldEntity.field_70126_B;
                entityVillager.field_70159_w = oldEntity.field_70159_w;
                entityVillager.field_70181_x = oldEntity.field_70181_x;
                entityVillager.field_70179_y = oldEntity.field_70179_y;
                entityVillager.field_70142_S = oldEntity.field_70142_S;
                entityVillager.field_70137_T = oldEntity.field_70137_T;
                entityVillager.field_70136_U = oldEntity.field_70136_U;
                entityVillager.field_70173_aa = oldEntity.field_70173_aa;
                entityVillager.field_70759_as = oldEntity.field_70759_as;
                entityVillager.field_70758_at = oldEntity.field_70758_at;
                entityVillager.field_70726_aT = oldEntity.field_70726_aT;
                entityVillager.field_70727_aS = oldEntity.field_70727_aS;
                entityVillager.field_70721_aZ = oldEntity.field_70721_aZ;
                entityVillager.field_70722_aY = oldEntity.field_70722_aY;
                entityVillager.field_70754_ba = oldEntity.field_70754_ba;
                entityVillager.field_70761_aq = oldEntity.field_70761_aq;
                entityVillager.field_70760_ar = oldEntity.field_70760_ar;
                entityVillager.field_70737_aN = oldEntity.field_70737_aN;
                entityVillager.field_70738_aO = oldEntity.field_70738_aO;
                entityVillager.field_70172_ad = oldEntity.field_70172_ad;
                entityVillager.field_70771_an = oldEntity.field_70771_an;
                entityVillager.field_70725_aQ = oldEntity.field_70725_aQ;
                entityVillager.field_70128_L = oldEntity.field_70128_L;
                renderManager.func_147937_a((Entity)entityVillager, partialTicks);
                try {
                    final double d0 = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partialTicks;
                    final double d2 = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partialTicks;
                    final double d3 = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partialTicks;
                    final Entity renderViewEntity = Minecraft.func_71410_x().func_175606_aa();
                    final double d4 = renderViewEntity.field_70142_S + (renderViewEntity.field_70165_t - renderViewEntity.field_70142_S) * partialTicks;
                    final double d5 = renderViewEntity.field_70137_T + (renderViewEntity.field_70163_u - renderViewEntity.field_70137_T) * partialTicks;
                    final double d6 = renderViewEntity.field_70136_U + (renderViewEntity.field_70161_v - renderViewEntity.field_70136_U) * partialTicks;
                    final double x = d0 - d4;
                    final double y = d2 - d5;
                    final double z = d3 - d6;
                    final Render<Entity> renderPlayer = (Render<Entity>)renderManager.func_78713_a(entity);
                    final Method renderLivingLabel = renderPlayer.getClass().getSuperclass().getSuperclass().getDeclaredMethod(TommyHUDTransformer.isDeobfuscated() ? "renderName" : "func_177067_a", Entity.class, Double.TYPE, Double.TYPE, Double.TYPE);
                    renderLivingLabel.setAccessible(true);
                    renderLivingLabel.invoke(renderPlayer, entity, x, y, z);
                }
                catch (Throwable ex) {
                    if (System.currentTimeMillis() - RenderGlobalHook.lastError > 5000L) {
                        RenderGlobalHook.lastError = System.currentTimeMillis();
                        ex.printStackTrace();
                    }
                }
            }
            catch (Throwable ex2) {
                if (System.currentTimeMillis() - RenderGlobalHook.lastError > 5000L) {
                    RenderGlobalHook.lastError = System.currentTimeMillis();
                    ex2.printStackTrace();
                }
            }
        }
        else {
            renderManager.func_147937_a(entity, partialTicks);
        }
        return true;
    }
    
    static {
        RenderGlobalHook.DUMMY_WORLD = new WorldClient((NetHandlerPlayClient)null, new WorldSettings(0L, WorldSettings.GameType.SURVIVAL, false, false, WorldType.field_77137_b), 0, (EnumDifficulty)null, (Profiler)null);
        RenderGlobalHook.lastError = -1L;
    }
}
