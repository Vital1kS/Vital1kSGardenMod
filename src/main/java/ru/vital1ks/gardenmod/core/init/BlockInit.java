package ru.vital1ks.gardenmod.core.init;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import ru.vital1ks.gardenmod.GardenMod;
import ru.vital1ks.gardenmod.common.blocks.DryingTable;
import ru.vital1ks.gardenmod.common.blocks.HazelnutBush;

public class BlockInit {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GardenMod.MOD_ID);
	
	public static final RegistryObject<Block> HORSERADISH = BLOCKS.register("horseradish", 
			() -> new Block(AbstractBlock.Properties.create(Material.PLANTS)));
	public static final RegistryObject<HazelnutBush> HAZELNUT_BUSH = BLOCKS.register("hazelnut_bush", 
			() -> new HazelnutBush(AbstractBlock.Properties.create(Material.PLANTS).notSolid().sound(SoundType.PLANT).doesNotBlockMovement()));
	public static final RegistryObject<DryingTable> DRYING_TABLE = BLOCKS.register("drying_table",
			() -> new DryingTable(AbstractBlock.Properties.create(Material.WOOD).sound(SoundType.WOOD).notSolid().hardnessAndResistance(1.0f)));
	
}

