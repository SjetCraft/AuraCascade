package com.sjet.auracascade.common.tiles.pump;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Map;

public class AuraNodePumpFallTile extends BaseAuraNodePumpTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_fall")
    public static final TileEntityType<AuraNodePumpFallTile> TYPE_PUMP_FALL = null;

    public AuraNodePumpFallTile() {
        super(TYPE_PUMP_FALL);
    }

    public void onFall(LivingFallEvent event) {
        addFuel((int) (2 * event.getDistance()), 500);
    }

    @Override
    public void findFuelAndAdd() {}

    @Override
    @OnlyIn(Dist.CLIENT)
    public void transferAuraParticles() {
        for(Map.Entry<BlockPos, String> target : sentNodesMap.entrySet()) {
            String array[]  = target.getValue().split(";");
            ParticleHelper.pumpTransferParticles(this.world, this.pos, target.getKey(), IAuraColor.BLUE, Integer.parseInt(array[1]));
        }
    }
}
