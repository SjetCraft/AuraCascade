package com.sjet.auracascade.common.tiles;
import com.sjet.auracascade.AuraCascade;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;


public class AuraNodeTile extends TileEntity implements ITickableTileEntity {

    @ObjectHolder(AuraCascade.MODID + ":aura_node")
    public static final TileEntityType<AuraNodeTile> TYPE = null;

    public AuraNodeTile() {
        super(TYPE);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void tick() {
        System.out.println("AuraNodeTile.tick");
    }

}

