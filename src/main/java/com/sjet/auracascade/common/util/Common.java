package com.sjet.auracascade.common.util;

import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
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

    public static int getTotalAura(HashMap<IAuraColor, Integer> auraMap) {
        int output = 0;
        for (Map.Entry<IAuraColor, Integer> colorList : auraMap.entrySet()) {
            output += colorList.getValue();
        }
        return output;
    }

    public static Iterable<BlockPos> inRange(BlockPos source, int range) {
        return BlockPos.getAllInBoxMutable(source.add(-range, -range, -range), source.add(range, range, range));
    }
}
