package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.items.AuraCrystalItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.LinkedList;

public abstract class AuraTile extends TileEntity implements ITickableTileEntity {

    public LinkedList<BlockPos> connectedNodes = new LinkedList<BlockPos>();
    private int auraStorage = 0;
    private int auraEnergy = 0;

    public AuraTile(TileEntityType<?> type) {
        super(type);
    }

    public void findNodes() {
        if (!world.isRemote) {
            connectedNodes = new LinkedList<BlockPos>();

            //search for connected nodes in each direction
            for (Direction direction : Direction.values()) {
                boolean blocked = false; //allows for early exit of search
                for (int i = 1; i <= 15 && !blocked; i++) {
                    BlockPos targetBlock = getPos().offset(direction, i);
                    //if block is not transparent/air stop checking in this direction
                    if (isBlocked(targetBlock)) {
                        blocked = true;
                    }
                    //if block is an AuraTile, connect and stop checking in this direction
                    if (isAuraTile(targetBlock)) {
                        connect(targetBlock);
                        blocked = true;
                    }
                }
            }
        }
        this.markDirty();
    }

    public void connect(BlockPos pos) {
        if (isAuraTile(pos) && world.getTileEntity(pos) != this) {
            AuraNodeTile otherNode = (AuraNodeTile) world.getTileEntity(pos);

            //add the found node this this node's connected Nodes list
            this.connectedNodes.add(otherNode.getPos());
            //add this node the found node's connected Nodes list
            otherNode.connectedNodes.add(getPos());
        }
    }

    public boolean isAuraTile(BlockPos pos) {
        return world.getTileEntity(pos) instanceof AuraNodeTile;
    }

    /**
     * @param target
     * @return false if the target cannot have aura passed through it
     */
    public boolean isBlocked(BlockPos target) {
        Block block = world.getBlockState(target).getBlock();

        //false if block is not air and if block is not transparent or an aura node
        return !block.isAir(block.getDefaultState(), world, target) &&
                !(isAuraTile(target)  || !block.isSolid(block.getDefaultState()));
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        auraStorage = tag.getInt("storage");
        auraEnergy = tag.getInt("energy");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putInt("storage", auraStorage);
        tag.putInt("energy", auraEnergy);
        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        read(tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        super.onDataPacket(net, packet);
        handleUpdateTag(packet.getNbtCompound());

        BlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }

    @OnlyIn(Dist.CLIENT)
    public void renderHUD(Minecraft mc) {
        String auraString = "White Aura: " + auraStorage;
        int width = 16 + mc.fontRenderer.getStringWidth(auraString) / 2;
        int x = mc.getMainWindow().getScaledWidth() / 2 - width;
        int y = mc.getMainWindow().getScaledHeight() / 2 + 50;

        mc.fontRenderer.drawStringWithShadow(auraString, x + 20, y + 5, 0xFFFFFF);
    }

    /**
     * To be used to add aura to a node from another node
     * @param sourcePos the source of the aura to be added
     * @param color the IAuraColor enum of the aura to be added
     * @param aura the amount of aura to be added
     */
    public void addAura(BlockPos sourcePos, IAuraColor color, int aura) {
        auraStorage += aura;
        this.markDirty();
    }

    /**
     * To be used when a player adds aura to the node
     * @param player
     * @param stack
     * @return
     */
    public boolean playerAddAura(@Nullable PlayerEntity player, ItemStack stack) {
        if (stack.getItem() instanceof AuraCrystalItem) {
            addAura(this.pos, ((AuraCrystalItem) stack.getItem()).getColor(), ((AuraCrystalItem) stack.getItem()).getAura());
            if (player == null || !player.abilities.isCreativeMode) {
                stack.shrink(1);
                return true;
            }
        }
        return false;
    }
}
