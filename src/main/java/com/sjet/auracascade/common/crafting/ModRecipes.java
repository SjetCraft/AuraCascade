package com.sjet.auracascade.common.crafting;

import com.sjet.auracascade.AuraCascade;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(AuraCascade.MODID)
public final class ModRecipes {
    private ModRecipes() {}

    @ObjectHolder("cascading_processing")
    public static IRecipeSerializer CASCADING_PROCESSING_SERIALIZER;

    @ObjectHolder("prismatic_processing")
    public static IRecipeSerializer PRISMATIC_PROCESSING_SERIALIZER;

    public static IRecipeType<CascadingProcessingRecipe> CASCADING_PROCESSING_TYPE;
    public static IRecipeType<PrismaticProcessingRecipe> PRISMATIC_PROCESSING_TYPE;

    @Mod.EventBusSubscriber(modid = AuraCascade.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationHandler {

        @SubscribeEvent
        public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
            final IForgeRegistry<IRecipeSerializer<?>> registry = event.getRegistry();

            registry.registerAll(
                    new CascadingProcessingRecipeSerializer().setRegistryName("cascading_processing"),
                    new PrismaticProcessingRecipeSerializer().setRegistryName("prismatic_processing")
            );
        }

        public static void registerRecipeTypes() {
            CASCADING_PROCESSING_TYPE = Registry.register(
                    Registry.RECIPE_TYPE, new ResourceLocation( "cascading_processing"),
                    new IRecipeType<CascadingProcessingRecipe>() {
                public String toString() {
                    return "cascading_processing";
                }
            });
            PRISMATIC_PROCESSING_TYPE = Registry.register(
                    Registry.RECIPE_TYPE, new ResourceLocation( "prismatic_processing"),
                    new IRecipeType<PrismaticProcessingRecipe>() {
                public String toString() {
                    return "prismatic_processing";
                }
            });
        }
    }
}
