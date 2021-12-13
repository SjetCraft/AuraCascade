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
    protected static final int RANGE = 3;
    protected static final int MAX_RECIPE_SIZE = 8;
    private static final String CONSUMED_LIST = "items_consumed";

    public AuraCascadingProcessorTile() {
        super(AURA_CASCADING_PROCESSOR);
    }

    //used for AuraPrismaticProcessorTile
    public AuraCascadingProcessorTile(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public int getMaxProgress() {
        return 60;
    }

    @Override
    public int getPowerPerProgress() {
        return 150;
    }

    @Override
    public void onUsePower() {
        //clear itemsConsumedList for rendering
        itemsConsumedList.clear();

        //get the list of ItemEntities around this TE
        List<ItemEntity> itemEntities = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos).grow(RANGE));

        //used to break out of loop
        boolean validRecipe = false;

        //only check recipes if there are itemEntites around the processor
        if (!itemEntities.isEmpty()) {
            List< List<ItemEntity> > combinations;

            //iterate for each recipe size
            for (int i = MAX_RECIPE_SIZE; i > 0 && !validRecipe; i--) {

                //if there are not enough items for 'i' recipe size don't do anything
                if(itemEntities.size() < i) {
                    continue;
                }

                //get all possible combinations
                combinations = Common.getCombinations(i, itemEntities);

                //for each possible combination
                for (int j = 0; j < combinations.size(); j++) {
                    List<ItemEntity> list = combinations.get(j);

                    //if the current combination is a valid recipe
                    if(isValidCascadingRecipe(list)) {
                        processCascadingRecipe(list);
                        validRecipe = true;
                        break;
                    }
                }
            }
        }
    }

    /**
     * @param itemEntities the list of item entities to test for a valid recipe
     * @return true if the input list is a valid recipe
     */
    protected boolean isValidCascadingRecipe(List<ItemEntity> itemEntities) {
        //instantiates the Inventory for the recipe
        Inventory inventory = new Inventory(itemEntities.size());

        //populates the Inventory with the input items
        for (ItemEntity itemEntity : itemEntities) {
            inventory.addItem(itemEntity.getItem());
        }

        //creates the recipe
        Optional<CascadingProcessingRecipe> optionalCascadingRecipe = world.getRecipeManager().getRecipe(ModRecipes.CASCADING_PROCESSING_TYPE, inventory, world);

        return optionalCascadingRecipe.isPresent();
    }

    /**
     * Assumes that the input list is a valid recipe. No exception handling if the recipe is not valid.
     * Consumes the items in the world and spawns in the recipe result.
     *
     * @param itemEntities the list of item entities that make a valid recipe
     */
    protected void processCascadingRecipe(List<ItemEntity> itemEntities) {
        //instantiates the Inventory for the recipe
        Inventory inventory = new Inventory(itemEntities.size());

        //populates the Inventory with the input items
        for (ItemEntity itemEntity : itemEntities) {
            inventory.addItem(itemEntity.getItem());

            //save the position for rendering particles on the client
            itemsConsumedList.add(itemEntity.getPositionVector());

            //consume the crafting item
            itemEntity.getItem().shrink(1);
        }

        //creates the recipe
        Optional<CascadingProcessingRecipe> optionalCascadingRecipe = world.getRecipeManager().getRecipe(ModRecipes.CASCADING_PROCESSING_TYPE, inventory, world);

        //get the result of the crafting operation
        ItemStack result = optionalCascadingRecipe.get().getRecipeOutput().copy();

        //spawn the new item item in the world
        ItemEntity newItemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D, result);
        newItemEntity.setMotion(Vec3d.ZERO);
        world.addEntity(newItemEntity);
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
