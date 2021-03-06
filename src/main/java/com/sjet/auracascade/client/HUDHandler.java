/**
 * This class was based on the 'HUDHandler' class created by <Vazkii>.
 * Get the Source Code in github: https://github.com/Vazkii/Botania
 *
 */
package com.sjet.auracascade.client;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.api.IBaseAuraConsumerTile;
import com.sjet.auracascade.common.api.IBaseAuraNodeTile;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AuraCascade.MODID)
public final class HUDHandler {

    private HUDHandler() {}

    @SubscribeEvent
    public static void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {

            RayTraceResult pos = minecraft.objectMouseOver;

            if(pos != null) {
                BlockPos bpos = pos.getType() == RayTraceResult.Type.BLOCK ? ((BlockRayTraceResult) pos).getPos() : null;
                TileEntity tile = bpos != null ? minecraft.world.getTileEntity(bpos) : null;

                //if the moused-over TileEntity implements IBaseAuraNodeTile
                if(tile instanceof IBaseAuraNodeTile) {
                    ((IBaseAuraNodeTile) tile).renderHUD(minecraft);
                } else if (tile instanceof IBaseAuraConsumerTile) {
                    ((IBaseAuraConsumerTile) tile).renderHUD(minecraft);
                }
            }
        }
    }
}
