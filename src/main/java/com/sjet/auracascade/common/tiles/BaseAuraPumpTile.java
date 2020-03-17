package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.client.HUDHandler;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.api.IBaseAuraNodePumpTile;
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

public abstract class BaseAuraPumpTile extends BaseAuraTile implements IBaseAuraNodePumpTile {

    protected int pumpPower;
    protected int pumpSpeed;

    public BaseAuraPumpTile(TileEntityType<?> type) {
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
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
    }

    public void addFuel(int time, int speed) {
        if (time * speed > pumpSpeed * pumpPower) {
            pumpPower = time;
            pumpSpeed = speed;
        }
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
    }

    public void pump() {
        //empty sentNodesMap
        this.sentNodesMap.clear();
        if (pumpPower > 0) {
            pumpPower--;

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

                    int distance = (int) Common.getDistance(this.pos, target);
                    int quantity = pumpSpeed / distance;

                    //only pump aura that is in the node and if there is an amount to pump
                    if (currentAura > 0 && quantity > 0) {

                        //if the current aura is less than the amount that is going to be pumped pump the available amount of aura
                        if (currentAura < quantity) {
                            quantity = currentAura;
                        }
                        transferAura(target, color, quantity);
                    }
                }
            }
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
        }
    }

    /**
     * Replace method with a stub to not allow the pump to distribute aura
     */
    @Override
    public void distributeAura() {
    }

    /**
     * @param target
     * @return true if the target node is higher than the current node
     */
    @Override
    public boolean canTransfer(BlockPos target) {
        return pos.getY() < target.getY();
    }

    @Override
    public void tick() {
        if (!world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 1) {
            findNodes();
            pump();
        } else if (world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 2) {
            transferAuraParticles();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void transferAuraParticles() {
        for(Map.Entry<BlockPos, String> target : sentNodesMap.entrySet()) {
            String[] array = target.getValue().split(";");
            ParticleHelper.transferAuraParticles(this.world, this.pos, target.getKey(), IAuraColor.WHITE, Integer.parseInt(array[1]));
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
        super.renderHUD(minecraft);
        String power = "Power: " + pumpPower;
        HUDHandler.printPowerOnScreen(minecraft, power);
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        pumpPower = tag.getInt("pumpPower");
        pumpSpeed = tag.getInt("pumpSpeed");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        CompoundNBT nbt = super.write(tag);
        tag.putInt("pumpPower", pumpPower);
        tag.putInt("pumpSpeed", pumpSpeed);
        return nbt;
    }
}
