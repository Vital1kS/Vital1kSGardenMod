package ru.vital1ks.gardenmod.common.blocks;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.CampfireTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import ru.vital1ks.gardenmod.common.tiles.DryingTableTileEntity;
import ru.vital1ks.gardenmod.core.init.TileEntityTypeInit;
import ru.vital1ks.gardenmod.recipes.DryingRecipe;

public class DryingTable extends Block implements IWaterLoggable{
	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
	public DryingTable(AbstractBlock.Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH));

	}
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TileEntityTypeInit.DRYING_TABLE.get().create();
	}
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
	      builder.add(FACING);
	   }
	
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	      return SHAPE;
	   }
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing());
	}
	public BlockState rotate(BlockState state, Rotation rot) {
	      return state.with(FACING, rot.rotate(state.get(FACING)));
	}
	@Override
	@SuppressWarnings("deprecation")
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
	      return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	   }
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		if (stack.hasDisplayName()) {
			TileEntity tile = worldIn.getTileEntity(pos);
			if (tile instanceof DryingTableTileEntity) {
				((DryingTableTileEntity) tile).setCustomName(stack.getDisplayName());
			}
		}
	}
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
	      TileEntity tileentity = worldIn.getTileEntity(pos);
	      int cookTime = 100;
	      if (tileentity instanceof DryingTableTileEntity) {
	         DryingTableTileEntity dryingtabletileentity = (DryingTableTileEntity)tileentity;
	         ItemStack itemstack = player.getHeldItem(handIn);
	         dryingtabletileentity.addItem(itemstack, cookTime);
	         return ActionResultType.CONSUME;
	      }

	      return ActionResultType.PASS;
	   }
	public BlockRenderType getRenderType(BlockState state) {
	      return BlockRenderType.MODEL;
	   }
}