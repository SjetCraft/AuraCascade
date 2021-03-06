package com.sjet.auracascade.common.tiles.pump;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.registries.ObjectHolder;

public class AuraNodePumpFallTile extends BaseAuraNodePumpTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_fall")
    public static final TileEntityType<AuraNodePumpFallTile> TYPE_PUMP_FALL = null;

    private static final int POWER = 500;

    public AuraNodePumpFallTile() {
        super(TYPE_PUMP_FALL);
    }

    public void onFall(LivingFallEvent event) {
        addFuel((int) (2 * event.getDistance()), POWER);
    }

    @Override
    public void findFuelAndAdd() {}
}
