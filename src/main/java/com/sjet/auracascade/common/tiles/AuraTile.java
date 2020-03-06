package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        auraStorage = nbt.getInt("storage");
        auraEnergy = nbt.getInt("energy");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putInt("storage", auraStorage);
        nbt.putInt("energy", auraEnergy);
        return super.write(nbt);
    }

    @OnlyIn(Dist.CLIENT)
    public void renderHUD(Minecraft mc) {
        String auraString = "White Aura: " + auraStorage;
        int width = 16 + mc.fontRenderer.getStringWidth(auraString) / 2;
        int x = mc.getMainWindow().getScaledWidth() / 2 - width;
        int y = mc.getMainWindow().getScaledHeight() / 2 + 50;

        mc.fontRenderer.drawStringWithShadow(auraString, x + 20, y + 5, 0xFFFFFF);
    }

    public void addAura(BlockPos sourcePos, IAuraColor color, int auraIn) {
        auraStorage += auraIn;
        this.markDirty();
    }
}
