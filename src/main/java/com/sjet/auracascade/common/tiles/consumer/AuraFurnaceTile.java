package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.BlastingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;
import java.util.Optional;

import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public class AuraFurnaceTile extends BaseAuraConsumerTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_furnace")
    public static final TileEntityType<AuraFurnaceTile> AURA_FURNACE = null;

    private Vec3d itemConsumed = null;
    private static final int RANGE = 3;

    public AuraFurnaceTile() {
        super(AURA_FURNACE);
    }

    @Override
    public int getMaxProgress() {
        return 10;
    }

    @Override
    public int getPowerPerProgress() {
        return 190;
    }

    @Override
    public void onUsePower() {
        List<ItemEntity> itemEntities = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(3.0));

        for (ItemEntity itemEntity : itemEntities) {
            //get the smelting/blasting recipes for the existing item
            Optional<FurnaceRecipe> optionalSmeltingRecipe = world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(itemEntity.getItem()), world);
            Optional<BlastingRecipe> optionalBlastingRecipe = world.getRecipeManager().getRecipe(IRecipeType.BLASTING, new Inventory(itemEntity.getItem()), world);

            //if there is a smelting/blasting recipe for the item
            if (optionalSmeltingRecipe.isPresent() || optionalBlastingRecipe.isPresent()) {
                ItemStack result = optionalSmeltingRecipe
                        .map(smeltingRecipe -> optionalSmeltingRecipe.get().getRecipeOutput().copy())
                        .orElseGet(() -> optionalBlastingRecipe.get().getRecipeOutput().copy());
                //result.setCount(result.getCount() * itemEntity.getItem().getCount());

                //spawn the new item item in the world
                ItemEntity newItemEntity = new ItemEntity(world, itemEntity.getPosX(), itemEntity.getPosY(), itemEntity.getPosZ(), result);
                newItemEntity.setMotion(itemEntity.getMotion());
                world.addEntity(newItemEntity);

                //save the position for rendering particles on the client
                itemConsumed = itemEntity.getPositionVector();

                //decriment the stack count
                itemEntity.getItem().shrink(1);

                //only smelt one item per operation
                return;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote && world.getGameTime() % TICKS_PER_SECOND * 120 == 0) {
            Common.keepItemsAlive(this, RANGE);
        }
        //clear the Vec3d position for rendering
        if (!world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 0) {
            itemConsumed = null;
        }
        else if (world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 2) {
            burnParticles();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void burnParticles() {
        if(itemConsumed != null) {
            ParticleHelper.itemBurningParticles(this.world, itemConsumed);
        }
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);

        double x = nbt.getDouble("itemConsumedX");
        double y = nbt.getDouble("itemConsumedY");
        double z = nbt.getDouble("itemConsumedZ");
        Vec3d read = new Vec3d(x, y, z);
        if (read != new Vec3d(0, 0, 0)) {
            itemConsumed = read;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        if (itemConsumed != null) {
            nbt.putDouble("itemConsumedX", itemConsumed.x);
            nbt.putDouble("itemConsumedY", itemConsumed.y);
            nbt.putDouble("itemConsumedZ", itemConsumed.z);
        }
        return super.write(nbt);
    }
}
