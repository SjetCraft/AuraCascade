package com.sjet.auracascade.common.tiles;
import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.blocks.AuraNode;
import com.sjet.auracascade.data.PosUtil;
import net.minecraft.block.Block;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedList;


public class AuraNodeTile extends TileEntity implements ITickableTileEntity {

    public boolean hasConnected = false;
    public LinkedList<BlockPos> connected = new LinkedList<>();
    private int aura = 0;

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
        if ((!hasConnected || world.getGameTime() % 200 == 0) && !world.isRemote) {
            for (int i = 1; i < 16; i++) {
                for (Direction dir : Direction.values()) {
                    connect(getPos().offset(dir, i));
                }
            }
            hasConnected = true;
        }
    }

    public void connect(BlockPos pos) {
        if (world.getTileEntity(pos) instanceof AuraNodeTile && world.getTileEntity(pos) != this && isOpenPath(pos)) {
            AuraNodeTile otherNode = (AuraNodeTile) world.getTileEntity(pos);

            otherNode.connected.add(getPos());
            this.connected.add(otherNode.getPos());
            //This should only happen on initial placement
            //Not on 'follow ups'
            if (!hasConnected) {
                System.out.println(otherNode.getPos());
                System.out.println(this.getPos());
                //burst(pos, "spell");
            }
        }
    }

    public boolean isOpenPath(BlockPos target) {
        int dist = (int) Math.sqrt(getPos().distanceSq(target));

        Direction direction = PosUtil.directionTo(getPos(), target);
        if (direction == null) {
            return false;
        }
        for (int i = 1; i < dist; i++) {
            BlockPos between = getPos().offset(direction, i);
            if (connectionBlockedByBlock(between)) {
                return false;
            }
        }
        return true;
    }

    public boolean connectionBlockedByBlock(BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return !block.isAir(block.getDefaultState(), world, pos) && ( block instanceof AuraNode || block.isSolid(block.getDefaultState()) );
    }

}

