package com.sjet.auracascade.setup;

import com.sjet.auracascade.common.tiles.pump.AuraNodePumpFallTile;
import com.sjet.auracascade.common.tiles.pump.AuraNodePumpProjectileTile;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.util.math.*;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    @SubscribeEvent
    public static void onLivingFallEvent(LivingFallEvent event) {
        BlockPos entityPosition = new BlockPos(event.getEntity());

        for (BlockPos searchPump : Common.inRange(entityPosition, 3)) {
            if (event.getEntity().world.getTileEntity(searchPump) instanceof AuraNodePumpFallTile) {
                ((AuraNodePumpFallTile) event.getEntity().world.getTileEntity(searchPump)).onFall(event);
                break; //stop looking if a pump is found
            }
        }
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        //don't run this on the client or if the target impact is not a block
        if (event.getEntity().world.isRemote || event.getRayTraceResult().getType() != RayTraceResult.Type.BLOCK) {
            return;
        }
        //have to use the BlockRayTraceResult as the entity position will be different for an arrow,
        // eg. the position of the arrow will be +-1 off on x, y, or z
        BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) event.getRayTraceResult();
        BlockPos position = blockRayTraceResult.getPos();

        if (event.getEntity().world.getTileEntity(position) instanceof AuraNodePumpProjectileTile) {
            ((AuraNodePumpProjectileTile) event.getEntity().world.getTileEntity(position)).onEntityCollidedWithBlock(event);
        }
    }
}
