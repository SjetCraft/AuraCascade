package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public class AuraBrewerTile extends BaseAuraConsumerTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_brewer")
    public static final TileEntityType<AuraBrewerTile> AURA_BREWER = null;

    private Vec3d itemConsumed = null;
    private static final int RANGE = 3;

    public AuraBrewerTile() {
        super(AURA_BREWER);
    }

    @Override
    public int getMaxProgress() {
        return 25;
    }

    @Override
    public int getPowerPerProgress() {
        return 500;
    }

    @Override
    public void onUsePower() {
        itemConsumed = null;
        //get all items nearby the brewer
        List<ItemEntity> nearbyItems = world.getEntitiesWithinAABB(ItemEntity.class, Common.getAABB(this.pos, RANGE));

        //iterate over all nearby items
        for (ItemEntity itemEntity : nearbyItems) {
            ItemStack itemStack = itemEntity.getItem();

            //check if the item is a watter bottle
            if (itemStack.getItem() == Items.POTION && PotionUtils.getPotionFromItem(itemStack) == Potions.WATER) {

                //create a new random potion item
                ItemStack newItemStack = getBrewResult(itemStack);

                //spawn the new potion item in the world
                ItemEntity newItemEntity = new ItemEntity(world, itemEntity.getPosX(), itemEntity.getPosY(), itemEntity.getPosZ(), newItemStack);
                newItemEntity.lifespan = itemEntity.lifespan;
                newItemEntity.setMotion(itemEntity.getMotion());
                world.addEntity(newItemEntity);

                //save the position for rendering particles on the client
                itemConsumed = itemEntity.getPositionVector();

                //remove the existing potion
                itemEntity.getItem().shrink(1);

                //only convert one water bottle to a potion
                break;
            }
        }
    }

    public ItemStack getBrewResult(ItemStack stack) {
        Potion potion = Registry.POTION.getRandom(new Random());
        ItemStack newStack = new ItemStack(stack.getItem());
        PotionUtils.addPotionToItemStack(newStack, potion);
        return newStack;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote && world.getGameTime() % TICKS_PER_SECOND * 120 == 0) {
            Common.keepItemsAlive(this, RANGE);
        }
        if (!world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 0) {
            //clear Vec3d position for rendering
            itemConsumed = null;
        }
        else if (world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 2) {
            consumeItemParticles();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void consumeItemParticles() {
        if (itemConsumed != null) {
            ParticleHelper.itemCreateParticles(this.world, itemConsumed);
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
