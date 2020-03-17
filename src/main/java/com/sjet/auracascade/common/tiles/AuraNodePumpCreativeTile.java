package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Map;
import java.util.Random;

public class AuraNodePumpCreativeTile extends BaseAuraPumpTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_creative")
    public static final TileEntityType<AuraNodePumpCreativeTile> TYPE_PUMP_CREATIVE = null;

    public AuraNodePumpCreativeTile() {
        super(TYPE_PUMP_CREATIVE);
    }

    @Override
    public void tick() {
        super.tick();
        if(!world.isRemote) {
            addFuel(2, 100000);
        }
    }

    @Override
    public void transferAuraParticles() {
        // Get a random color from the HashMap.
        Object[] colorArray = auraMap.keySet().toArray();
        IAuraColor randomColor = (IAuraColor) colorArray[new Random().nextInt(colorArray.length)];

        for(Map.Entry<BlockPos, String> target : sentNodesMap.entrySet()) {
            String array[]  = target.getValue().split(";");
            ParticleHelper.transferAuraParticles(this.world, this.pos, target.getKey(), randomColor, Integer.parseInt(array[1]));
        }
    }
}
