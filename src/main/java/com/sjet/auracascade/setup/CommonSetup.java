package com.sjet.auracascade.setup;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.blocks.consumer.*;
import com.sjet.auracascade.common.blocks.node.*;
import com.sjet.auracascade.common.blocks.pump.*;
import com.sjet.auracascade.common.items.crystals.*;
import com.sjet.auracascade.common.items.ingots.*;
import com.sjet.auracascade.common.items.gems.*;
import com.sjet.auracascade.common.tiles.consumer.*;
import com.sjet.auracascade.common.tiles.node.*;
import com.sjet.auracascade.common.tiles.pump.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommonSetup {

    @SubscribeEvent
    public void onRegisterBlocks(RegistryEvent.Register<Block> e) {
        //Nodes
        e.getRegistry().register(new AuraNode());
        e.getRegistry().register(new AuraNodeCapacitor());
        e.getRegistry().register(new AuraNodeConserve());
        e.getRegistry().register(new AuraNodeManipulatorOrange());
        e.getRegistry().register(new AuraNodeManipulatorBlack());
        //Pumps
        e.getRegistry().register(new AuraNodePumpBurning());
        e.getRegistry().register(new AuraNodePumpRedstone());
        e.getRegistry().register(new AuraNodePumpLight());
        e.getRegistry().register(new AuraNodePumpProjectile());
        e.getRegistry().register(new AuraNodePumpFall());
        e.getRegistry().register(new AuraNodePumpCreative());
        //Consumers
        e.getRegistry().register(new AuraColorer());
        e.getRegistry().register(new AuraGrower());
        e.getRegistry().register(new AuraBrewer());
        e.getRegistry().register(new AuraFisher());
        e.getRegistry().register(new AuraLooter());
        e.getRegistry().register(new AuraMobSpawner());
        e.getRegistry().register(new AuraFurnace());
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

        //Consumers
        e.getRegistry().register(new BlockItem(AuraColorer.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_colorer"));
        e.getRegistry().register(new BlockItem(AuraGrower.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_grower"));
        e.getRegistry().register(new BlockItem(AuraBrewer.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_brewer"));
        e.getRegistry().register(new BlockItem(AuraFisher.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_fisher"));
        e.getRegistry().register(new BlockItem(AuraLooter.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_looter"));
        e.getRegistry().register(new BlockItem(AuraMobSpawner.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_mob_spawner"));
        e.getRegistry().register(new BlockItem(AuraFurnace.BLOCK, new Item.Properties().group(AuraCascade.MAIN_GROUP)).setRegistryName(AuraCascade.MODID, "aura_furnace"));

        //Aura Crystals
        e.getRegistry().register(new AuraCrystalWhiteItem());
        e.getRegistry().register(new AuraCrystalYellowItem());
        e.getRegistry().register(new AuraCrystalOrangeItem());
        e.getRegistry().register(new AuraCrystalRedItem());
        e.getRegistry().register(new AuraCrystalGreenItem());
        e.getRegistry().register(new AuraCrystalBlueItem());
        e.getRegistry().register(new AuraCrystalVioletItem());
        e.getRegistry().register(new AuraCrystalBlackItem());
        //Ingots
        e.getRegistry().register(new AuraIngotWhiteItem());
        e.getRegistry().register(new AuraIngotYellowItem());
        e.getRegistry().register(new AuraIngotOrangeItem());
        e.getRegistry().register(new AuraIngotRedItem());
        e.getRegistry().register(new AuraIngotGreenItem());
        e.getRegistry().register(new AuraIngotBlueItem());
        e.getRegistry().register(new AuraIngotVioletItem());
        e.getRegistry().register(new AuraIngotBlackItem());
        //Gems
        e.getRegistry().register(new AuraGemWhiteItem());
        e.getRegistry().register(new AuraGemYellowItem());
        e.getRegistry().register(new AuraGemOrangeItem());
        e.getRegistry().register(new AuraGemRedItem());
        e.getRegistry().register(new AuraGemGreenItem());
        e.getRegistry().register(new AuraGemBlueItem());
        e.getRegistry().register(new AuraGemVioletItem());
        e.getRegistry().register(new AuraGemBlackItem());
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

        //Consumers
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraColorerTile(), AuraColorer.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_colorer"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraGrowerTile(), AuraGrower.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_grower"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraBrewerTile(), AuraBrewer.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_brewer"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraFisherTile(), AuraFisher.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_fisher"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraLooterTile(), AuraLooter.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_looter"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraMobSpawnerTile(), AuraMobSpawner.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_mob_spawner"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new AuraFurnaceTile(), AuraFurnace.BLOCK).build(null).setRegistryName(AuraCascade.MODID, "aura_furnace"));
    }
}

