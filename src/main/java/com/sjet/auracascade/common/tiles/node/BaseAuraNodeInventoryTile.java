package com.sjet.auracascade.common.tiles.node;

import com.google.common.base.Preconditions;

import com.sjet.auracascade.common.util.InventoryHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;


public abstract class BaseAuraNodeInventoryTile extends BaseAuraNodeTile implements IInventory{

    @SuppressWarnings("WeakerAccess")
    protected ItemStack item;
    private IItemHandler inventoryWrapper = new InvWrapper(this);

    public BaseAuraNodeInventoryTile(TileEntityType<?> type) {
        super(type);
        item = ItemStack.EMPTY;
    }

    public ItemStack getItem() {
        return item;
    }

    public void dropPedestalInventory() {
        if (!item.isEmpty()) {
            InventoryHelper.spawnItemStack(world, pos, item);
        }
    }

    public void removeAndSpawnItem() {
        if (!item.isEmpty()) {
            if (!world.isRemote) {
                markDirty();
                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1D, pos.getZ() + 0.5D, item);
                world.addEntity(itemEntity);
            }
            item = ItemStack.EMPTY;
            notifyBlock();
        }
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index == 0 ? item : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index == 0) {
            return decrStackInInventory(count);
        }
        return ItemStack.EMPTY;
    }

    private ItemStack decrStackInInventory(int count) {
        if (!item.isEmpty()) {
            ItemStack stack = item.split(count);

            if (item.isEmpty()) {
                notifyBlock();
            }

            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index == 0) {
            ItemStack stack = item;
            item = ItemStack.EMPTY;
            notifyBlock();
            return stack;
        }

        return ItemStack.EMPTY;
    }

    private void notifyBlock() {
        BlockState blockState = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), blockState, blockState, 3);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        item = compound.contains("item") ? ItemStack.read(compound.getCompound("item")) : ItemStack.EMPTY;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);

        if (!item.isEmpty()) {
            compound.put("item", item.write(new CompoundNBT()));
        }
        return compound;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == 0) {
            item = stack;
            notifyBlock();
        }
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void openInventory(PlayerEntity player) {
        //noop
    }

    @Override
    public void closeInventory(PlayerEntity player) {
        //noop
    }



    @Override
    public void clear() {
        for (int i = 0; i < getSizeInventory(); i++) {
            setInventorySlotContents(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean isEmpty() {
        return item.isEmpty();
    }


    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> inventoryWrapper));
        }
        return super.getCapability(cap, side);
    }
}
