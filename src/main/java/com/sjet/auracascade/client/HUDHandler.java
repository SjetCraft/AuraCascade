/**
 * This class was based on the 'HUDHandler' class created by <Vazkii>.
 * Get the Source Code in github: https://github.com/Vazkii/Botania
 *
 */
package com.sjet.auracascade.client;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.AuraNodeTile;
import com.sjet.auracascade.common.tiles.BaseAuraPumpTile;
import com.sjet.auracascade.common.tiles.BaseAuraTile;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.IProfiler;
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
        Minecraft mc = Minecraft.getInstance();
        IProfiler profiler = mc.getProfiler();

        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            profiler.startSection("auracascade-hud");

            RayTraceResult pos = mc.objectMouseOver;

            if(pos != null) {
                BlockPos bpos = pos.getType() == RayTraceResult.Type.BLOCK ? ((BlockRayTraceResult) pos).getPos() : null;
                //BlockState state = bpos != null ? mc.world.getBlockState(bpos) : null;
                //Block block = state == null ? null : state.getBlock();
                TileEntity tile = bpos != null ? mc.world.getTileEntity(bpos) : null;

                if(tile instanceof BaseAuraTile) {
                    profiler.startSection("auraNode");
                    if(tile instanceof AuraNodeTile) {
                        ((AuraNodeTile) tile).renderHUD(mc);
                    } else if (tile instanceof BaseAuraPumpTile) {
                        ((BaseAuraPumpTile) tile).renderHUD(mc);
                    }
                }
            }
        }
    }
}
