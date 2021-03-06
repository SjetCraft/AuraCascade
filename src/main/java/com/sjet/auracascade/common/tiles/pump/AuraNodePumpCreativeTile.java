package com.sjet.auracascade.common.tiles.pump;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.HUDHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;

public class AuraNodePumpCreativeTile extends BaseAuraNodePumpTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_creative")
    public static final TileEntityType<AuraNodePumpCreativeTile> TYPE_PUMP_CREATIVE = null;

    private static final int POWER = 100000;

    public AuraNodePumpCreativeTile() {
        super(TYPE_PUMP_CREATIVE);
    }

    @Override
    public void tick() {
        super.tick();
        if(!world.isRemote) {
            addFuel(2, POWER);
        }
    }

    @Override
    public void findFuelAndAdd() {}

    /**
     * Used to render the amount of Power and Aura on the screen
     *
     * @param minecraft
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHUD(Minecraft minecraft) {
        ArrayList<String> list = new ArrayList<>();

        list.add("Aura Stored");
        for (IAuraColor color : IAuraColor.values()) {
            int auraAmount = auraMap.get(color);
            if (auraAmount > 0) {
                list.add("    " + color.capitalizedName() + ": " + auraAmount);
            }
        }
        if (list.size() == 1) {
            list.set(0, "No Aura");
        }

        list.add("Time left: infinite seconds");
        list.add("Power: " + pumpPower + " per second");

        HUDHelper.printTextOnScreen(minecraft, list);
    }
}
