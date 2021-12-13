package com.sjet.auracascade.client.renderer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.tiles.node.AuraNodePedestalTile;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AuraCascade.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RendererRegistry {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ClientRegistry.bindTileEntityRenderer(AuraNodePedestalTile.TYPE_NODE, AuraNodePedestalRenderer::new);
    }
}
