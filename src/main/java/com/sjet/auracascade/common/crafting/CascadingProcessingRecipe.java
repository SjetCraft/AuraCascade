package com.sjet.auracascade.common.crafting;

import com.sjet.auracascade.common.blocks.consumer.AuraCascadingProcessor;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;

public class CascadingProcessingRecipe implements IRecipe<IInventory> {

    protected final ResourceLocation id;
    protected final NonNullList<Ingredient> ingredients;
    protected final ItemStack  output;

    public CascadingProcessingRecipe(ResourceLocation resourceLocation, NonNullList<Ingredient> ingredients, ItemStack output) {
        this.id = resourceLocation;
        this.ingredients = ingredients;
        this.output = output;
    }

    @Override
    @Deprecated
    public boolean matches(IInventory iInventory, World worldIn) {
        //adapted from ShapelessRecipe#matches in vanilla
        List<ItemStack> inputs = new ArrayList<>();
        int i = 0;

        for(int j = 0; j < iInventory.getSizeInventory(); ++j) {
            ItemStack itemstack = iInventory.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                ++i;
                inputs.add(itemstack);
            }
        }

        return i == this.ingredients.size() && RecipeMatcher.findMatches(inputs,  this.ingredients) != null;
    }

    @Override
    @Deprecated
    public ItemStack getCraftingResult(IInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    @Deprecated
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public ItemStack getIcon() {
        return new ItemStack(AuraCascadingProcessor.BLOCK);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipes.CASCADING_PROCESSING_SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType () {
        return ModRecipes.CASCADING_PROCESSING_TYPE;
    }

    @Override
    public boolean isDynamic () {
        return true;
    }

    @Override
    public boolean canFit (int width, int height) {
        return true;
    }
}