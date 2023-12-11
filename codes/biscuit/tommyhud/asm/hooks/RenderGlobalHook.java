package codes.biscuit.tommyhud.asm.hooks;

import codes.biscuit.tommyhud.TommyHUD;
import codes.biscuit.tommyhud.core.CrazyMode;
import java.lang.reflect.Method;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.WorldSettings.GameType;

public class RenderGlobalHook {
    public static WorldClient DUMMY_WORLD = new WorldClient((NetHandlerPlayClient)null, new WorldSettings(0L, GameType.SURVIVAL, false, false, WorldType.DEFAULT), 0, (EnumDifficulty)null, (Profiler)null);
    private static long lastError = -1L;

    public static boolean renderEntitySimple(RenderManager renderManager, Entity entity, float partialTicks) {
        if (entity instanceof EntityLivingBase && !(entity instanceof EntityArmorStand) && !entity.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) && TommyHUD.getInstance().getConfigManager().getConfigValues().isCrazyModeEnabled(CrazyMode.JERRY_MODE)) {
            try {
                EntityVillager entityVillager = new EntityVillager(DUMMY_WORLD);
                EntityLivingBase oldEntity = (EntityLivingBase)entity;
                entityVillager.posX = oldEntity.field_70165_t;
                entityVillager.posY = oldEntity.field_70163_u;
                entityVillager.posZ = oldEntity.field_70161_v;
                entityVillager.prevPosX = oldEntity.field_70169_q;
                entityVillager.prevPosY = oldEntity.field_70167_r;
                entityVillager.prevPosZ = oldEntity.field_70166_s;
                entityVillager.rotationPitch = oldEntity.field_70125_A;
                entityVillager.rotationYaw = oldEntity.field_70177_z;
                entityVillager.prevRotationPitch = oldEntity.field_70127_C;
                entityVillager.prevRotationYaw = oldEntity.field_70126_B;
                entityVillager.motionX = oldEntity.field_70159_w;
                entityVillager.motionY = oldEntity.field_70181_x;
                entityVillager.motionZ = oldEntity.field_70179_y;
                entityVillager.lastTickPosX = oldEntity.field_70142_S;
                entityVillager.lastTickPosY = oldEntity.field_70137_T;
                entityVillager.lastTickPosZ = oldEntity.field_70136_U;
                entityVillager.ticksExisted = oldEntity.field_70173_aa;
                entityVillager.field_70759_as = oldEntity.rotationYawHead;
                entityVillager.field_70758_at = oldEntity.prevRotationYawHead;
                entityVillager.field_70726_aT = oldEntity.cameraPitch;
                entityVillager.field_70727_aS = oldEntity.prevCameraPitch;
                entityVillager.field_70721_aZ = oldEntity.limbSwingAmount;
                entityVillager.field_70722_aY = oldEntity.prevLimbSwingAmount;
                entityVillager.field_70754_ba = oldEntity.limbSwing;
                entityVillager.field_70761_aq = oldEntity.renderYawOffset;
                entityVillager.field_70760_ar = oldEntity.prevRenderYawOffset;
                entityVillager.field_70737_aN = oldEntity.hurtTime;
                entityVillager.field_70738_aO = oldEntity.maxHurtTime;
                entityVillager.hurtResistantTime = oldEntity.field_70172_ad;
                entityVillager.field_70771_an = oldEntity.maxHurtResistantTime;
                entityVillager.field_70725_aQ = oldEntity.deathTime;
                entityVillager.isDead = oldEntity.field_70128_L;
                renderManager.renderEntitySimple(entityVillager, partialTicks);

                try {
                    double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks;
                    double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks;
                    double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks;
                    Entity renderViewEntity = Minecraft.getMinecraft().getRenderViewEntity();
                    double d3 = renderViewEntity.lastTickPosX + (renderViewEntity.posX - renderViewEntity.lastTickPosX) * (double)partialTicks;
                    double d4 = renderViewEntity.lastTickPosY + (renderViewEntity.posY - renderViewEntity.lastTickPosY) * (double)partialTicks;
                    double d5 = renderViewEntity.lastTickPosZ + (renderViewEntity.posZ - renderViewEntity.lastTickPosZ) * (double)partialTicks;
                    double x = d0 - d3;
                    double y = d1 - d4;
                    double z = d2 - d5;
                    Render<Entity> renderPlayer = renderManager.getEntityRenderObject(entity);
                    Method renderLivingLabel = renderPlayer.getClass().getSuperclass().getSuperclass().getDeclaredMethod(TommyHUDTransformer.isDeobfuscated() ? "renderName" : "func_177067_a", Entity.class, Double.TYPE, Double.TYPE, Double.TYPE);
                    renderLivingLabel.setAccessible(true);
                    renderLivingLabel.invoke(renderPlayer, entity, x, y, z);
                } catch (Throwable var26) {
                    if (System.currentTimeMillis() - lastError > 5000L) {
                        lastError = System.currentTimeMillis();
                        var26.printStackTrace();
                    }
                }
            } catch (Throwable var27) {
                if (System.currentTimeMillis() - lastError > 5000L) {
                    lastError = System.currentTimeMillis();
                    var27.printStackTrace();
                }
            }
        } else {
            renderManager.renderEntitySimple(entity, partialTicks);
        }

        return true;
    }
}