package com.sjet.auracascade.common.tiles;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.api.IAuraColor;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;
import java.util.Map;

import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public class AuraNodePumpBurningTile extends BaseAuraPumpTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_node_pump_burning")
    public static final TileEntityType<AuraNodePumpBurningTile> TYPE_PUMP_BURNING = null;

    private Vec3d itemConsumed = null;

    public AuraNodePumpBurningTile() {
        super(TYPE_PUMP_BURNING);
    }


    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 0) {
            findFuelAndAdd();
        } else if (world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 2) {
            burnParticles();
        }
    }

    public void findFuelAndAdd() {
        itemConsumed = null;
        if(pumpPower <= 0) {
            int range = 3;
            List<ItemEntity> nearbyItems = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.add(-range, -range, -range), pos.add(range, range, range)));

            //iterate through the nearby items
            for (ItemEntity itemEntity : nearbyItems) {
                ItemStack item = itemEntity.getItem();
                if (itemEntity.isAlive() && item.getBurnTime() != 0) {

                    int burnTime;
                    //burnTime is -1 for vanilla
                    if (item.getBurnTime() < 0) {
                        burnTime = ForgeHooks.getBurnTime(item);
                    } else {
                      burnTime = item.getBurnTime();
                    }

                    addFuel(burnTime, 300);
                    itemConsumed = itemEntity.getPositionVector();
                    item.shrink(1);
                    break;
                }
            }
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 2);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void transferAuraParticles() {
        for(Map.Entry<BlockPos, String> target : sentNodesMap.entrySet()) {
            String array[]  = target.getValue().split(";");
            ParticleHelper.transferAuraParticles(this.world, this.pos, target.getKey(), IAuraColor.RED, Integer.parseInt(array[1]));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void burnParticles() {
        if(itemConsumed != null) {
            ParticleHelper.itemBurningParticles(this.world, itemConsumed);
        }
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);

        double x = tag.getFloat("itemConsumedX");
        double y = tag.getFloat("itemConsumedY");
        double z = tag.getFloat("itemConsumedZ");
        Vec3d read = new Vec3d(x, y, z);
        if (read != new Vec3d(0,0,0)) {
            itemConsumed = read;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        if(itemConsumed != null) {
            tag.putDouble("itemConsumedX", itemConsumed.x);
            tag.putDouble("itemConsumedY", itemConsumed.y);
            tag.putDouble("itemConsumedZ", itemConsumed.z);
        }
        return super.write(tag);
    }
}
