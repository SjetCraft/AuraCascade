package com.sjet.auracascade.common.tiles.pump;

import com.sjet.auracascade.client.HUDHelper;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.api.IBaseAuraNodePumpTile;
import com.sjet.auracascade.common.tiles.node.BaseAuraNodeTile;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Map;

import static com.sjet.auracascade.AuraCascade.MAX_DISTANCE;
import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public abstract class BaseAuraNodePumpTile extends BaseAuraNodeTile implements IBaseAuraNodePumpTile {

    protected int pumpTime;
    protected int pumpPower;

    public BaseAuraNodePumpTile(TileEntityType<?> type) {
        super(type);
    }

    /**
     * Only looks UP for nodes to connect to
     */
    @Override
    public void findNodes() {
        connectedNodesList = new ArrayList<>();

        boolean blocked = false; //allows for early exit of search
        for (int i = 1; i <= MAX_DISTANCE && !blocked; i++) {
            BlockPos targetBlock = getPos().offset(Direction.UP, i);

            //if block is not transparent/air stop checking in this direction
            if (canAuraFlow(targetBlock)) {
                blocked = true;
            }
            //if block is an AuraTile, connect and stop checking in this direction
            if (isAuraTile(targetBlock)) {
                connectNode(targetBlock);
                blocked = true; //aura can no longer pass through this block as it is a terminating node
            }
        }
    }

    public void addFuel(int time, int power) {
        if (time * power > pumpPower * pumpTime) {
            pumpTime = time;
            pumpPower = power;
        }
    }

    public void pump() {
        //empty sentNodesMap
        this.sentNodesMap.clear();

        //early exit if there are no nodes to distribute Aura & if redstone is blocking the node from distributing
        if (connectedNodesList.isEmpty() || this.world.isBlockPowered(this.pos)) {
            return;
        }

        if (pumpTime > 0) {
            pumpTime--;

            //iterate over the aura
            for (Map.Entry<IAuraColor, Integer> colorList : auraMap.entrySet()) {
                IAuraColor color = colorList.getKey();
                int currentAura = colorList.getValue();

                //iterate over each connected node - There should only be one node
                for (BlockPos target : connectedNodesList) {

                    int distance = (int) Common.getDistance(this.pos, target);
                    int quantity = pumpPower / distance;

                    //test to check for a divide by zero case
                    float boost = color.getAscentBoost(world);
                    quantity = boost == 0 ? 0 : (int) ((double) quantity / boost);

                    //only pump aura that is in the node and if there is an amount to pump and the aura can move vertically
                    if (currentAura > 0 && quantity > 0 && color.getVerticalTransfer()) {

                        //if the current aura is less than the amount that is going to be pumped pump the available amount of aura
                        if (currentAura < quantity) {
                            quantity = currentAura;
                        }
                        transferAura(target, color, quantity);
                    }
                }
            }
        }
    }

    @Override
    public boolean canTransfer(BlockPos target, IAuraColor color) {
        return false;
    }

    @Override
    public boolean canReceive(BlockPos source, IAuraColor color) {
        return source.getY() <= getPos().getY() && super.canReceive(source, color);
    }

    @Override
    public void tick() {
        if (!world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 1) {
            findNodes();
            updateAura();
            pump();
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
        } else if (world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 2) {
            transferAuraParticles();
        }
    }

    public abstract void findFuelAndAdd();

    @Override
    @OnlyIn(Dist.CLIENT)
    public void transferAuraParticles() {
        for(Map.Entry<BlockPos, String> target : sentNodesMap.entrySet()) {
            String[] array = target.getValue().split(";");
            ParticleHelper.pumpTransferParticles(this.world, this.pos, target.getKey(), IAuraColor.WHITE, Integer.parseInt(array[1]));
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

        if (pumpTime > 0) {
            list.add("Time left: " + pumpTime + " seconds");
            list.add("Power: " + pumpPower + " per second");
        } else {
            list.add("No Power");
        }
        HUDHelper.printTextOnScreen(minecraft, list);
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        pumpTime = tag.getInt("pumpTime");
        pumpPower = tag.getInt("pumpPower");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT nbt = super.write(tag);
        tag.putInt("pumpTime", pumpTime);
        tag.putInt("pumpPower", pumpPower);
        return nbt;
    }
}
