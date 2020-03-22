package com.sjet.auracascade.setup;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.blocks.*;
import com.sjet.auracascade.common.blocks.pump.*;
import com.sjet.auracascade.common.items.*;
import com.sjet.auracascade.common.tiles.*;
import com.sjet.auracascade.common.tiles.pump.*;
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
        e.getRegistry().register(new AuraNodeCapacitor());
        e.getRegistry().register(new AuraNodeConserve());
        e.getRegistry().register(new AuraNodeManipulatorOrange());
        e.getRegistry().register(new AuraNodeManipulatorBlack());
        e.getRegistry().register(new AuraNodePumpBurning());
        e.getRegistry().register(new AuraNodePumpRedstone());
        e.getRegistry().register(new AuraNodePumpLight());
        e.getRegistry().register(new AuraNodePumpProjectile());
        e.getRegistry().register(new AuraNodePumpFall());
        e.getRegistry().register(new AuraNodePumpCreative());
    }

    @SubscribeEvent
    public void onRegisterItems(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(new BlockItem(AuraNode.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node"));
        e.getRegistry().register(new BlockItem(AuraNodeCapacitor.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node_capacitor"));
        e.getRegistry().register(new BlockItem(AuraNodeConserve.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node_conserve"));
        e.getRegistry().register(new BlockItem(AuraNodeManipulatorOrange.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node_manipulator_orange"));
        e.getRegistry().register(new BlockItem(AuraNodeManipulatorBlack.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node_manipulator_black"));
        e.getRegistry().register(new BlockItem(AuraNodePumpBurning.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node_pump_burning"));
        e.getRegistry().register(new BlockItem(AuraNodePumpRedstone.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node_pump_redstone"));
        e.getRegistry().register(new BlockItem(AuraNodePumpLight.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node_pump_light"));
        e.getRegistry().register(new BlockItem(AuraNodePumpProjectile.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node_pump_projectile"));
        e.getRegistry().register(new BlockItem(AuraNodePumpFall.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node_pump_fall"));
        e.getRegistry().register(new BlockItem(AuraNodePumpCreative.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_node_pump_creative"));

        e.getRegistry().register(new AuraCrystalWhiteItem());
        e.getRegistry().register(new AuraCrystalYellowItem());
        e.getRegistry().register(new AuraCrystalOrangeItem());
        e.getRegistry().register(new AuraCrystalRedItem());
        e.getRegistry().register(new AuraCrystalGreenItem());
        e.getRegistry().register(new AuraCrystalBlueItem());
        e.getRegistry().register(new AuraCrystalVioletItem());
        e.getRegistry().register(new AuraCrystalBlackItem());
    }

    @SubscribeEvent
    public void onRegisterTiles(RegistryEvent.Register<TileEntityType<?>> e) {
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodeTile(), AuraNode.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodeCapacitorTile(), AuraNodeCapacitor.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node_capacitor"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodeConserveTile(), AuraNodeConserve.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node_conserve"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodeManipulatorOrangeTile(), AuraNodeManipulatorOrange.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node_manipulator_orange"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodeManipulatorBlackTile(), AuraNodeManipulatorBlack.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node_manipulator_black"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodePumpBurningTile(), AuraNodePumpBurning.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node_pump_burning"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodePumpRedstoneTile(), AuraNodePumpRedstone.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node_pump_redstone"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodePumpLightTile(), AuraNodePumpLight.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node_pump_light"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodePumpProjectileTile(), AuraNodePumpProjectile.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node_pump_projectile"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodePumpFallTile(), AuraNodePumpFall.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node_pump_fall"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraNodePumpCreativeTile(), AuraNodePumpCreative.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_node_pump_creative"));
    }


}

