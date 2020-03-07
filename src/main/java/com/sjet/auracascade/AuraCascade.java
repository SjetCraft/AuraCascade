package com.sjet.auracascade;

import com.sjet.auracascade.setup.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("auracascade")
public class AuraCascade {

    public static final String MODID = "auracascade";
    public static final ItemGroup MAIN_GROUP = new MainItemGroup();
    public static final int MAX_DISTANCE = 15;
    public static final int AURA_IN_ITEM = 1000;

    public static IProxy proxy = DistExecutor.runForDist( () -> () -> new ClientProxy(), () -> () -> new ServerProxy() );

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public AuraCascade() {
        CommonSetup commonSetup = new CommonSetup();

        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, commonSetup::onRegisterBlocks);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, commonSetup::onRegisterTiles);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, commonSetup::onRegisterItems);
    }

}
