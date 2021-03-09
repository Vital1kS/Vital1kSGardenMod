package ru.vital1ks.gardenmod.core.init;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ru.vital1ks.gardenmod.GardenMod;
import ru.vital1ks.gardenmod.recipes.GardenRecipeSerializer;
import ru.vital1ks.gardenmod.recipes.IGardenRecipe;

public class RecipeSerializerInit {
	
	public static final IRecipeType<IGardenRecipe> DRYING_TYPE = registerType(IGardenRecipe.RECIPE_TYPE_ID);
	
	public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZER = 
			DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GardenMod.MOD_ID);
	
	public static final RegistryObject<GardenRecipeSerializer> DRYING_SERIALIZER = RECIPE_SERIALIZER.register("drying", 
			() -> new GardenRecipeSerializer());

	public static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
		@Override
		public String toString() {
			return Registry.RECIPE_TYPE.getKey(this).toString();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends IRecipeType<?>> T registerType(ResourceLocation recipeTypeId) {
		return (T) Registry.register(Registry.RECIPE_TYPE, recipeTypeId, new RecipeType<>());
	}
}
