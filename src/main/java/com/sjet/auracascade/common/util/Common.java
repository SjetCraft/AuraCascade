package com.sjet.auracascade.common.util;

import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Common {

    /**
     * Intended to be used only in cardinal directions
     * formula: sqrt( (x1 + x2)^2 + (y1 + y2)^2 + (z1 + z2)^2 )
     * @param target
     * @return the distance in Blocks from the source to the target
     */
    public static double getDistance(BlockPos source, BlockPos target) {
        double deltaX = source.getX() - target.getX();
        double deltaY = source.getY() - target.getY();
        double deltaZ = source.getZ() - target.getZ();
        return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
    }

    /**
     * @param source
     * @return the 'center' of the source block
     */
    public static BlockPos normalize(BlockPos source) {
        return new BlockPos(source.getX() + 0.5D, source.getY() + 0.5D, source.getZ() + 0.5D);
    }

    public static boolean isHorizontal(BlockPos source, BlockPos target) {
        if  (target.getY() == source.getY()) {
            return true;
        }
        return false;
    }

    public static boolean isVertical(BlockPos source, BlockPos target) {
        if  (target.getY() != source.getY()) {
            return true;
        }
        return false;
    }

    public static int getTotalAura(HashMap<IAuraColor, Integer> auraMap) {
        int output = 0;
        for (Map.Entry<IAuraColor, Integer> colorList : auraMap.entrySet()) {
            output += colorList.getValue();
        }
        return output;
    }

    public static Iterable<BlockPos> getBlocksInRange(BlockPos source, int range) {
        return BlockPos.getAllInBoxMutable(source.add(-range, -range, -range), source.add(range, range, range));
    }

    public static void keepItemsAlive(TileEntity tileEntity, int range) {
        List<ItemEntity> nearbyItems = tileEntity.getWorld().getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(tileEntity.getPos()).grow(range));
        for (ItemEntity itemEntity : nearbyItems) {
            itemEntity.setNoDespawn();
        }
    }

    public static CompoundNBT writeVec3D(Vec3d vec3d) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putDouble("X", vec3d.x);
        nbt.putDouble("Y", vec3d.y);
        nbt.putDouble("Z", vec3d.z);
        return nbt;
    }

    public static Vec3d readVec3D(CompoundNBT nbt) {
        return new Vec3d(nbt.getDouble("X"), nbt.getDouble("Y"), nbt.getDouble("Z"));
    }
}
