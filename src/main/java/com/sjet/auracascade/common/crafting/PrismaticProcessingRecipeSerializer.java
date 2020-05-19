package com.sjet.auracascade.common.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class PrismaticProcessingRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PrismaticProcessingRecipe> {
    @Override
    public PrismaticProcessingRecipe read(ResourceLocation recipeId, JsonObject json) {
        NonNullList<Ingredient> ingredients = readIngredients(JSONUtils.getJsonArray(json, "ingredients"));
        //throw exception if the json recipe has no ingredients
        if (ingredients.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        } else {
            final ItemStack output = ShapedRecipe.deserializeItem(json.getAsJsonObject("output"));
            return new PrismaticProcessingRecipe(recipeId, ingredients, output);
        }
    }

    @Override
    public PrismaticProcessingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        int i = buffer.readVarInt();
        NonNullList<Ingredient> ingredients = NonNullList.withSize(i, Ingredient.EMPTY);

        for(int j = 0; j < ingredients.size(); ++j) {
            ingredients.set(j, Ingredient.read(buffer));
        }

        final ItemStack output = buffer.readItemStack();
        return new PrismaticProcessingRecipe(recipeId, ingredients, output);
    }

    @Override
    public void write (PacketBuffer buffer, PrismaticProcessingRecipe recipe) {
        buffer.writeVarInt(recipe.ingredients.size());

        for(Ingredient ingredient : recipe.ingredients) {
            ingredient.write(buffer);
        }
        buffer.writeItemStack(recipe.output);
    }

    private static NonNullList<Ingredient> readIngredients(JsonArray jsonArray) {
        NonNullList<Ingredient> nonnulllist = NonNullList.create();

        for(int i = 0; i < jsonArray.size(); ++i) {
            Ingredient ingredient = Ingredient.deserialize(jsonArray.get(i));
            if (!ingredient.hasNoMatchingItems()) {
                nonnulllist.add(ingredient);
            }
        }

        return nonnulllist;
    }
}