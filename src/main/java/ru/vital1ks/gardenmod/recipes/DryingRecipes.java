package ru.vital1ks.gardenmod.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class DryingRecipes {
	public ItemStack getDryingRecipe(ItemStack input) {
		if(input == Items.APPLE.getDefaultInstance()) {
			return Items.GOLDEN_APPLE.getDefaultInstance();
		}
		else {
			return ItemStack.EMPTY;
		}
	}
}
