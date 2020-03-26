package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.particles.ParticleHelper;
import com.sjet.auracascade.common.crafting.CascadingProcessingRecipe;
import com.sjet.auracascade.common.crafting.ModRecipes;
import com.sjet.auracascade.common.util.Common;
import com.sjet.auracascade.common.util.NBTListHelper;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sjet.auracascade.AuraCascade.TICKS_PER_SECOND;

public class AuraCascadingProcessorTile extends BaseAuraConsumerTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_cascading_processor")
    public static final TileEntityType<AuraCascadingProcessorTile> AURA_CASCADING_PROCESSOR = null;

    protected ArrayList<Vec3d> itemsConsumedList = new ArrayList<>();
    private static final int RANGE = 3;
    private static final String CONSUMED_LIST = "items_consumed";

    public AuraCascadingProcessorTile() {
        super(AURA_CASCADING_PROCESSOR);
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
        //clear itemsConsumedList for rendering
        itemsConsumedList.clear();

        //get the list of ItemEntities around this TE
        List<ItemEntity> itemEntities = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(RANGE));

        //only check recipes if there are itemEntites around the processor
        if (!itemEntities.isEmpty()) {
            //if there are no one item recipes found with the ingredients
            if (!oneItemRecipe(itemEntities)) {
                //if there are no two item recipes found with the ingredients
                if (itemEntities.size() >= 2 && !twoItemRecipe(itemEntities)) {
                    //if there are enough items to calculate eight item recipes
                    if (itemEntities.size() >= 8 && eightItemRecipe(itemEntities)) {

                    }
                }
            }
        }
    }

    private boolean oneItemRecipe(List<ItemEntity> itemEntities) {
        Inventory inventory;

        //check each item for a recipe
        for (int i = 0; i < itemEntities.size(); i++) {
            inventory = new Inventory(itemEntities.get(i).getItem());

            Optional<CascadingProcessingRecipe> optionalCascadingRecipe = world.getRecipeManager().getRecipe(ModRecipes.CASCADING_PROCESSING_TYPE, inventory, world);

            if (optionalCascadingRecipe.isPresent()) {
                //get the result of the crafting operation
                ItemStack result = optionalCascadingRecipe.get().getRecipeOutput().copy();

                //spawn the new item item in the world
                ItemEntity newItemEntity = new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), result);
                world.addEntity(newItemEntity);

                //save the position for rendering particles on the client
                itemsConsumedList.add(itemEntities.get(i).getPositionVector());

                //consume the crafting item
                itemEntities.get(i).getItem().shrink(1);

                //only process one recipe per operation
                return true;
            }
        }
        //if no recipes were matched
        return false;
    }

    private boolean twoItemRecipe(List<ItemEntity> itemEntities) {
        Inventory inventory;

        //iterate over all combinations of the list
        for (int i = 0; i < itemEntities.size(); i++) {
            //don't check the same itemstacks or combinations that have already been checked
            for (int j = i + 1; j < itemEntities.size(); j++) {
                inventory = new Inventory(itemEntities.get(i).getItem(), itemEntities.get(j).getItem());

                Optional<CascadingProcessingRecipe> optionalCascadingRecipe = world.getRecipeManager().getRecipe(ModRecipes.CASCADING_PROCESSING_TYPE, inventory, world);

                //if the inventory items are a match for a CascadingRecipe
                if (optionalCascadingRecipe.isPresent()) {
                    //get the result of the crafting operation
                    ItemStack result = optionalCascadingRecipe.get().getRecipeOutput().copy();

                    //spawn the new item item in the world
                    ItemEntity newItemEntity = new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), result);
                    world.addEntity(newItemEntity);

                    //save the position for rendering particles on the client
                    itemsConsumedList.add(itemEntities.get(i).getPositionVector());
                    itemsConsumedList.add(itemEntities.get(j).getPositionVector());

                    //consume the crafting items
                    itemEntities.get(i).getItem().shrink(1);
                    itemEntities.get(j).getItem().shrink(1);

                    //only process one recipe per operation
                    return true;
                }
            }
        }
        //if no recipes were matched
        return false;
    }

    private boolean eightItemRecipe(List<ItemEntity> itemEntities) {
        Inventory inventory;
        int listMax = itemEntities.size();

        //not proud of this but it works
        for (int a = 0; a < listMax; a++) {
            for (int b = a + 1; b < listMax; b++) {
                for (int c = b + 1; c < listMax; c++) {
                    for (int d = c + 1; d < listMax; d++) {
                        for (int e = d + 1; e < listMax; e++) {
                            for (int f = e + 1; f < listMax; f++) {
                                for (int g = f + 1; g < listMax; g++) {
                                    for (int h = f + 1; h < listMax; h++) {
                                        inventory = new Inventory(  itemEntities.get(a).getItem(), itemEntities.get(b).getItem(), itemEntities.get(c).getItem(), itemEntities.get(d).getItem(),
                                                                    itemEntities.get(e).getItem(), itemEntities.get(f).getItem(), itemEntities.get(g).getItem(), itemEntities.get(h).getItem());

                                        Optional<CascadingProcessingRecipe> optionalCascadingRecipe = world.getRecipeManager().getRecipe(ModRecipes.CASCADING_PROCESSING_TYPE, inventory, world);

                                        //if the inventory items are a match for a CascadingRecipe
                                        if (optionalCascadingRecipe.isPresent()) {
                                            //get the result of the crafting operation
                                            ItemStack result = optionalCascadingRecipe.get().getRecipeOutput().copy();

                                            //spawn the new item item in the world
                                            ItemEntity newItemEntity = new ItemEntity(world, pos.getX(), pos.getY() + 1, pos.getZ(), result);
                                            world.addEntity(newItemEntity);

                                            //save the position for rendering particles on the client
                                            itemsConsumedList.add(itemEntities.get(a).getPositionVector());
                                            itemsConsumedList.add(itemEntities.get(b).getPositionVector());
                                            itemsConsumedList.add(itemEntities.get(c).getPositionVector());
                                            itemsConsumedList.add(itemEntities.get(d).getPositionVector());
                                            itemsConsumedList.add(itemEntities.get(e).getPositionVector());
                                            itemsConsumedList.add(itemEntities.get(f).getPositionVector());
                                            itemsConsumedList.add(itemEntities.get(g).getPositionVector());
                                            itemsConsumedList.add(itemEntities.get(h).getPositionVector());

                                            //consume the crafting items
                                            itemEntities.get(a).getItem().shrink(1);
                                            itemEntities.get(b).getItem().shrink(1);
                                            itemEntities.get(c).getItem().shrink(1);
                                            itemEntities.get(d).getItem().shrink(1);
                                            itemEntities.get(e).getItem().shrink(1);
                                            itemEntities.get(f).getItem().shrink(1);
                                            itemEntities.get(g).getItem().shrink(1);
                                            itemEntities.get(h).getItem().shrink(1);

                                            //only process one recipe per operation
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //if no recipes were matched
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isRemote && world.getGameTime() % TICKS_PER_SECOND * 120 == 0) {
            Common.keepItemsAlive(this, RANGE);
        } else if (world.isRemote && world.getGameTime() % TICKS_PER_SECOND == 2) {
            burnParticles();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void burnParticles() {
        if (!itemsConsumedList.isEmpty()) {
            for (Vec3d vec3d : itemsConsumedList) {
                ParticleHelper.itemBurningParticles(this.world, vec3d);
            }
        }
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);

        itemsConsumedList.clear();
        this.itemsConsumedList = (ArrayList<Vec3d>) CONSUMED_LIST_NBT.read(nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        CONSUMED_LIST_NBT.write(itemsConsumedList, nbt);

        return super.write(nbt);
    }

    private NBTListHelper<Vec3d> CONSUMED_LIST_NBT = new NBTListHelper<Vec3d>(
            CONSUMED_LIST,
            (nbt, vec) -> nbt.put("items_consumed", Common.writeVec3D(vec)),
            nbt -> Common.readVec3D(nbt.getCompound("items_consumed"))
    );
}
