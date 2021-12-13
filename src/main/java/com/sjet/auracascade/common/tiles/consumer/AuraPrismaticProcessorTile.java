package com.sjet.auracascade.common.tiles.consumer;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.common.crafting.ModRecipes;
import com.sjet.auracascade.common.crafting.PrismaticProcessingRecipe;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.registries.ObjectHolder;

import java.util.List;
import java.util.Optional;

public class AuraPrismaticProcessorTile extends AuraCascadingProcessorTile {

    @ObjectHolder(AuraCascade.MODID + ":aura_prismatic_processor")
    public static final TileEntityType<AuraCascadingProcessorTile> AURA_PRISMATIC_PROCESSOR = null;

    public AuraPrismaticProcessorTile() {
        super(AURA_PRISMATIC_PROCESSOR);
    }

    @Override
    public int getMaxProgress() {
        return 9;
    }

    @Override
    public int getPowerPerProgress() {
        return 1000;
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

                    //if the current combination is a valid prismatic recipe
                    if(isValidPrismaticRecipe(list)) {
                        processPrismaticRecipe(list);
                        validRecipe = true;
                        break;
                    } else if (isValidCascadingRecipe(list)) { //if the current combination is a valid cascading recipe
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
    private boolean isValidPrismaticRecipe(List<ItemEntity> itemEntities) {
        //instantiates the Inventory for the recipe
        Inventory inventory = new Inventory(itemEntities.size());

        //populates the Inventory with the input items
        for (ItemEntity itemEntity : itemEntities) {
            inventory.addItem(itemEntity.getItem());
        }

        //creates the recipe
        Optional<PrismaticProcessingRecipe> optionalPrismaticRecipe = world.getRecipeManager().getRecipe(ModRecipes.PRISMATIC_PROCESSING_TYPE, inventory, world);

        return optionalPrismaticRecipe.isPresent();
    }

    /**
     * Assumes that the input list is a valid recipe. No exception handling if the recipe is not valid.
     * Consumes the items in the world and spawns in the recipe result.
     *
     * @param itemEntities the list of item entities that make a valid recipe
     */
    private void processPrismaticRecipe(List<ItemEntity> itemEntities) {
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
        Optional<PrismaticProcessingRecipe> optionalPrismaticRecipe = world.getRecipeManager().getRecipe(ModRecipes.PRISMATIC_PROCESSING_TYPE, inventory, world);

        //get the result of the crafting operation
        ItemStack result = optionalPrismaticRecipe.get().getRecipeOutput().copy();

        //spawn the new item in the world
        ItemEntity newItemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D, result);
        newItemEntity.setMotion(Vec3d.ZERO);
        world.addEntity(newItemEntity);
    }
}
