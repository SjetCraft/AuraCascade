package com.sjet.auracascade.setup;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.blocks.AuraNode;
import com.sjet.auracascade.common.tiles.AuraNodeTile;
import com.sjet.auracascade.common.items.AuraCrystalWhiteItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonSetup {

    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> e) {
        e.getRegistry().register(new AuraNode());
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(new BlockItem(AuraNode.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node"));

        e.getRegistry().register(new AuraCrystalWhiteItem());
    }

    @SubscribeEvent
    public void onRegisterTiles(RegistryEvent.Register<TileEntityType<?>> e) {
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodeTile(), AuraNode.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node"));
    }

}

