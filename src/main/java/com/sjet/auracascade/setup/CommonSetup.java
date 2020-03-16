package com.sjet.auracascade.setup;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.blocks.AuraNode;
import com.sjet.auracascade.common.blocks.AuraNodePumpCreative;
import com.sjet.auracascade.common.tiles.AuraNodePumpCreativeTile;
import com.sjet.auracascade.common.tiles.AuraNodeTile;
import com.sjet.auracascade.common.items.AuraCrystalWhiteItem;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class CommonSetup {

    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> e) {
        e.getRegistry().register(new AuraNode());
        e.getRegistry().register(new AuraNodePumpCreative());
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(new BlockItem(AuraNode.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node"));
        e.getRegistry().register(new BlockItem(AuraNodePumpCreative.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node_pump_creative"));

        e.getRegistry().register(new AuraCrystalWhiteItem());
    }

    @SubscribeEvent
    public void onRegisterTiles(RegistryEvent.Register<TileEntityType<?>> e) {
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodeTile(), AuraNode.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodePumpCreativeTile(), AuraNodePumpCreative.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node_pump_creative"));
    }

    public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> reg, IForgeRegistryEntry<T> thing, ResourceLocation name) {
        reg.register(thing.setRegistryName(name));
    }

    public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> reg, IForgeRegistryEntry<T> thing, String name) {
        register(reg, thing, new ResourceLocation(AuraCascade.MODID, name));
    }

}

