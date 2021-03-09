package ru.vital1ks.gardenmod.core.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ru.vital1ks.gardenmod.GardenMod;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GardenMod.MOD_ID);
	
	public static final RegistryObject<Item> SPROUT_ITEM = ITEMS.register("sprout", () -> new Item(new Item.Properties().group(ItemGroup.FOOD)));
	public static final RegistryObject<Item> HAZELNUT = ITEMS.register("hazelnut", () -> new BlockNamedItem(BlockInit.HAZELNUT_BUSH.get(), 
			(new Item.Properties()).group(ItemGroup.FOOD)));
	
	public static final RegistryObject<BlockItem> DRYING_TABLE = ITEMS.register("drying_table", 
			() -> new BlockItem(BlockInit.DRYING_TABLE.get(), new Item.Properties().group(ItemGroup.FOOD)));
	

}
