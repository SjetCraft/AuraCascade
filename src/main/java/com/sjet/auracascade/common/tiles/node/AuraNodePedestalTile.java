package com.sjet.auracascade.common.tiles.node;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodePedestalTile extends BaseAuraNodeTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pedestal")
    public static final TileEntityType<AuraNodePedestalTile> TYPE_NODE = null;

    private final ItemStackHandler items = new ItemStackHandler(4) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            AuraNodePedestalTile.this.markDirty();

            BlockState state = AuraNodePedestalTile.this.getBlockState();
            world.notifyBlockUpdate(pos, state, state, 3);

            requestModelDataUpdate();
        }
    };

    public AuraNodePedestalTile() {
        super(TYPE_NODE);
    }

    @Override
    public void receivePower(int power) {
        auraPower += power;
    }
}

