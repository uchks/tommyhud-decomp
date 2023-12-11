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
                entityVillager.posX = oldEntity.posX;
                entityVillager.posY = oldEntity.posY;
                entityVillager.posZ = oldEntity.posZ;
                entityVillager.prevPosX = oldEntity.prevPosX;
                entityVillager.prevPosY = oldEntity.prevPosY;
                entityVillager.prevPosZ = oldEntity.prevPosZ;
                entityVillager.rotationPitch = oldEntity.rotationPitch;
                entityVillager.rotationYaw = oldEntity.rotationYaw;
                entityVillager.prevRotationPitch = oldEntity.prevRotationPitch;
                entityVillager.prevRotationYaw = oldEntity.prevRotationYaw;
                entityVillager.motionX = oldEntity.motionX;
                entityVillager.motionY = oldEntity.motionY;
                entityVillager.motionZ = oldEntity.motionZ;
                entityVillager.lastTickPosX = oldEntity.lastTickPosX;
                entityVillager.lastTickPosY = oldEntity.lastTickPosY;
                entityVillager.lastTickPosZ = oldEntity.lastTickPosZ;
                entityVillager.ticksExisted = oldEntity.ticksExisted;
                entityVillager.rotationYawHead = oldEntity.rotationYawHead;
                entityVillager.prevRotationYawHead = oldEntity.prevRotationYawHead;
                entityVillager.cameraPitch = oldEntity.cameraPitch;
                entityVillager.prevCameraPitch = oldEntity.prevCameraPitch;
                entityVillager.limbSwingAmount = oldEntity.limbSwingAmount;
                entityVillager.prevLimbSwingAmount = oldEntity.prevLimbSwingAmount;
                entityVillager.limbSwing = oldEntity.limbSwing;
                entityVillager.renderYawOffset = oldEntity.renderYawOffset;
                entityVillager.prevRenderYawOffset = oldEntity.prevRenderYawOffset;
                entityVillager.hurtTime = oldEntity.hurtTime;
                entityVillager.maxHurtTime = oldEntity.maxHurtTime;
                entityVillager.hurtResistantTime = oldEntity.hurtResistantTime;
                entityVillager.maxHurtResistantTime = oldEntity.maxHurtResistantTime;
                entityVillager.deathTime = oldEntity.deathTime;
                entityVillager.isDead = oldEntity.isDead;
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
                    Method renderLivingLabel = renderPlayer.getClass().getSuperclass().getSuperclass().getDeclaredMethod(TommyHUDTransformer.isDeobfuscated() ? "renderName" : "renderName", Entity.class, Double.TYPE, Double.TYPE, Double.TYPE);
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