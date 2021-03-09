package ru.vital1ks.gardenmod.core.init;

import java.util.Collection;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ru.vital1ks.gardenmod.GardenMod;
import ru.vital1ks.gardenmod.common.tiles.DryingTableTileEntity;
@SuppressWarnings("unused")
public class TileEntityTypeInit {
	public static final DeferredRegister<TileEntityType<?>> TILEENTITYTYPE = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, GardenMod.MOD_ID);

	public static final RegistryObject<TileEntityType<DryingTableTileEntity>> DRYING_TABLE = TILEENTITYTYPE.register(
			"drying_table", makeType(DryingTableTileEntity::new, () -> BlockInit.DRYING_TABLE.get()));
	
	
	
	private static <T extends TileEntity> Supplier<TileEntityType<T>> makeType(Supplier<T> create, Supplier<Block> valid)
	{
		return makeTypeMultipleBlocks(create, () -> ImmutableSet.of(valid.get()));
	}

	private static <T extends TileEntity> Supplier<TileEntityType<T>> makeTypeMultipleBlocks(
			Supplier<T> create,
			Supplier<Collection<Block>> valid)
	{
		return () -> new TileEntityType<>(create, ImmutableSet.copyOf(valid.get()), null);
	}
}
