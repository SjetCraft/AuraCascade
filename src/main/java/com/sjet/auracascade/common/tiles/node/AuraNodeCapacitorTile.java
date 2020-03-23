package com.sjet.auracascade.common.tiles.node;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.HUDHelper;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.Map;

import static com.sjet.auracascade.AuraCascade.MAX_DISTANCE;
import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public class AuraNodeCapacitorTile extends BaseAuraNodeTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_capacitor")
    public static final TileEntityType<AuraNodeCapacitorTile> TYPE_CAPACITOR = null;

    public static int[] storageValues = new int[]{100, 1000, 10000, 100000};
    public int storageValueIndex = 1;
    public int ticksDisabled = 0;

    public AuraNodeCapacitorTile() {
        super(TYPE_CAPACITOR);
    }

    /**
     * Only looks DOWN for nodes to connect to
     */
    @Override
    public void findNodes() {
        connectedNodesList = new ArrayList<>();
        sentNodesMap.clear();

        boolean blocked = false; //allows for early exit of search
        for (int i = 1; i <= MAX_DISTANCE && !blocked; i++) {
            BlockPos targetBlock = getPos().offset(Direction.DOWN, i);

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
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
    }

    public void discharge() {
        //empty sentNodesMap
        this.sentNodesMap.clear();

        //early exit if there are no nodes to distribute Aura & if redstone is blocking the node from distributing
        if (connectedNodesList.isEmpty() || this.world.isBlockPowered(this.pos)) {
            return;
        }

        //iterate over the aura
        for (Map.Entry<IAuraColor, Integer> colorList : auraMap.entrySet()) {
            IAuraColor color = colorList.getKey();
            int currentAura = colorList.getValue();

            //iterate over each connected node - There should only be one node
            for (BlockPos target : connectedNodesList) {
                if (color.getVerticalTransfer()) {
                    transferAura(target, color, currentAura);
                }
            }
        }
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if (ticksDisabled > 0) {
                ticksDisabled--;
            }
            if (world.getGameTime() % TICKS_PER_SECOND == 1) {
                findNodes();
                updateAura();

                //splitting this into a sub 'if' to prevent the total Aura from being calculated every tick
                int totalAura = Common.getTotalAura(auraMap);
                if (totalAura >= storageValues[storageValueIndex] && ticksDisabled <= 0) {
                    discharge();
                    ticksDisabled = 410;
                }
                this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
            }
        } else if (world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 2) {
            transferAuraParticles();
        }
    }

    public void updateCapacity() {
        if (storageValueIndex < storageValues.length - 1) {
            storageValueIndex++;
        } else {
            storageValueIndex = 0;
        }
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
    }

    @Override
    public boolean canTransfer(BlockPos target, IAuraColor color) {
        int totalStorage = Common.getTotalAura(auraMap);
        return totalStorage >= storageValues[storageValueIndex] && super.canTransfer(target, color);
    }

    @Override
    public boolean canReceive(BlockPos source, IAuraColor color) {
        return super.canReceive(source, color) && ticksDisabled == 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void transferAuraParticles() {
        boolean burst = false;
        for (Map.Entry<BlockPos, String> target : sentNodesMap.entrySet()) {
            burst = true;
            String array[] = target.getValue().split(";");
            ParticleHelper.transferAuraParticles(this.world, this.pos, target.getKey(), IAuraColor.valueOf(array[0]), Integer.parseInt(array[1]));
        }
        if (burst) {
            ParticleHelper.burstParticles(this.world, this.pos);
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
        list.add("Max Storage: " + storageValues[storageValueIndex]);

        for (IAuraColor color : IAuraColor.values()) {
            int auraAmount = auraMap.get(color);
            if (auraAmount > 0) {
                list.add(color.capitalizedName() + ": " + auraAmount);
            }
        }
        if (list.size() == 1) {
            list.add("No Aura");
        }

        if (ticksDisabled > 0) {
            list.add("Time until functional: " + ticksDisabled / 20);
        }

        HUDHelper.printTextOnScreen(minecraft, list);
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        storageValueIndex = tag.getInt("storageValueIndex");
        ticksDisabled = tag.getInt("ticksDisabled");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putInt("storageValueIndex", storageValueIndex);
        tag.putInt("ticksDisabled", ticksDisabled);

        return super.write(tag);
    }
}
