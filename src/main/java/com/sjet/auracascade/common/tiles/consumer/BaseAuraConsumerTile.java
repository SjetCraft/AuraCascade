package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.client.HUDHelper;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.api.IBaseAuraConsumerTile;
import com.sjet.auracascade.common.tiles.node.AuraNodeTile;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;

public abstract class BaseAuraConsumerTile extends TileEntity implements IBaseAuraConsumerTile, ITickableTileEntity {

    public int storedPower;
    public int lastPower;
    public int progress;

    protected ArrayList<BlockPos> connectedNodesList = new ArrayList<>();

    public BaseAuraConsumerTile(TileEntityType<?> type) {
        super(type);
    }

    public abstract int getMaxProgress();

    public abstract int getPowerPerProgress();

    public abstract void onUsePower();

    public abstract boolean validItemsNearby();

    public void getPowerfromAdjacentNodes() {
        connectedNodesList.clear();
        lastPower = 0;

        for (Direction direction : Direction.values()) {
            TileEntity tileEntity = world.getTileEntity(getPos().offset(direction));
            if (tileEntity instanceof AuraNodeTile) {
                AuraNodeTile auraTile = (AuraNodeTile) tileEntity;
                if (auraTile.auraPower > 0) {
                    storedPower += auraTile.auraPower;
                    lastPower += auraTile.auraPower;
                    auraTile.auraPower = 0;
                    connectedNodesList.add(auraTile.getPos());
                }
            }
        }
    }

    public void process() {
        int nextBoostCost = getPowerPerProgress();
        while (true) {
            if (progress > getMaxProgress()) {
                progress = 0;
                onUsePower();
            }
            if (storedPower < nextBoostCost) {
                break;
            }
            progress += 1;
            storedPower -= nextBoostCost;
            nextBoostCost *= 2;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void transferPowerParticles() {
        for (BlockPos target : connectedNodesList) {
            ParticleHelper.pumpTransferParticles(this.world, this.pos, target, IAuraColor.WHITE, lastPower);
        }
    }

    /**
     * Used to render the amount of Aura on the screen when the cursor is on this node
     *
     * @param minecraft
     */
    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHUD(Minecraft minecraft) {
        ArrayList<String> list = new ArrayList<>();
        list.add("Progress: " + progress + " / " + getMaxProgress());
        list.add("Power per progress: " + getPowerPerProgress());
        list.add("Last Power: " + lastPower);

        HUDHelper.printTextOnScreen(minecraft, list);
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);

        storedPower = nbt.getInt("storedPower");
        lastPower = nbt.getInt("lastPower");
        progress = nbt.getInt("progress");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt.putInt("storedPower", storedPower);
        nbt.putInt("lastPower",lastPower);
        nbt.putInt("progress", progress);

        return super.write(nbt);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(CompoundNBT nbt) {
        read(nbt);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        write(nbt);
        return new SUpdateTileEntityPacket(pos, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        this.read(packet.getNbtCompound());
    }
}
