package com.sjet.auracascade.common.tiles.pump;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.EggEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Map;

public class AuraNodePumpProjectileTile extends BaseAuraNodePumpTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_projectile")
    public static final TileEntityType<AuraNodePumpProjectileTile> TYPE_PUMP_PROJECTILE = null;

    private static final int POWER = 500;

    public AuraNodePumpProjectileTile() {
        super(TYPE_PUMP_PROJECTILE);
    }

    public void onEntityCollidedWithBlock(ProjectileImpactEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof AbstractArrowEntity) {
            addFuel(20, POWER * 2);
            entity.remove(); //remove the arrow from the block
        }
        if (entity instanceof EggEntity) {
            addFuel(90, POWER);
        }
        if (entity instanceof SnowballEntity) {
            addFuel(10, POWER);
        }
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
    }

    @Override
    public void findFuelAndAdd() {

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void transferAuraParticles() {
        for(Map.Entry<BlockPos, String> target : sentNodesMap.entrySet()) {
            String array[]  = target.getValue().split(";");
        ParticleHelper.pumpTransferParticles(this.world, this.pos, target.getKey(), IAuraColor.VIOLET, Integer.parseInt(array[1]));
        }
    }
}
