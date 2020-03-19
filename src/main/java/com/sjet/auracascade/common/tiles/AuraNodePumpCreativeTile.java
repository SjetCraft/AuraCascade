package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.HUDHandler;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
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
    public void findFuelAndAdd() {}

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

        HUDHandler.printAuraOnScreen(minecraft, list);
    }
}
