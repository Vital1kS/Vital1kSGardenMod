package ru.vital1ks.gardenmod.common.blocks;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.vital1ks.gardenmod.core.init.BlockInit;
import ru.vital1ks.gardenmod.core.init.ItemInit;

public class HazelnutBush extends BushBlock implements IGrowable{
	ResourceLocation bushTag = new ResourceLocation("gardenmod", "bushes");
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_5;
	 private static final VoxelShape FIRST_SHAPE = Block.makeCuboidShape(6.0D, 0.0D, 6.0D, 9.0D, 4.0D, 9.0D);
	 private static final VoxelShape SECOND_SHAPE = Block.makeCuboidShape(5.0D, 0.0D, 5.0D, 11.0D, 8.0D, 11.0D);
	 private static final VoxelShape THIRD_SHAPE = Block.makeCuboidShape(4.0D, 0.0D, 4.0D, 13.0D, 12.0D, 13.0D);
	 private static final VoxelShape FOURTH_SHAPE = Block.makeCuboidShape(1.0D, 0.0D, 1.0D, 15.0D, 18.0D, 15.0D);
	public HazelnutBush(AbstractBlock.Properties properties) {
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(AGE, Integer.valueOf(0)));
	}

	public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
	      return new ItemStack(ItemInit.HAZELNUT.get());
	   }
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
	      if (state.get(AGE) == 0) {
	         return FIRST_SHAPE;
	      }
	      else if (state.get(AGE) == 1) {
	    	  return SECOND_SHAPE;
	      }
	      else if (state.get(AGE) == 2) {
	    	  return THIRD_SHAPE;
	      }
	      else {
	    	  return FOURTH_SHAPE;
	      }
	   }
	public boolean ticksRandomly(BlockState state) {
	      return state.get(AGE) < 4;
	   }
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
	      int i = state.get(AGE);
	      if (i == 4) {
	    	  this.setRegistryName("hazelnut_bush_bloom");
	      }
	      if (i < 5 && worldIn.getLightSubtracted(pos.up(), 0) >= 9 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state,random.nextInt(5) == 0)) {
	         worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(i + 1)), 2);
	         net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
	      }

	   }
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
	      return true;
	   }
	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
	      int i = state.get(AGE);
	      boolean flag = i == 3;
	      if (!flag && player.getHeldItem(handIn).getItem() == Items.BONE_MEAL) {
	    	  if (state.get(AGE) < 4){
	    		  worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(state.get(AGE)+1)), 2);
		    	  BoneMealItem.spawnBonemealParticles(worldIn, pos, 0);
	    	  }	    	  
	    	  return ActionResultType.SUCCESS;
	      } else if (i == 5) {
	         int j = 1 + worldIn.rand.nextInt(2);
	         spawnAsEntity(worldIn, pos, new ItemStack(ItemInit.HAZELNUT.get(), j + (flag ? 1 : 0)));
	         worldIn.playSound((PlayerEntity)null, pos, SoundEvents.ITEM_SWEET_BERRIES_PICK_FROM_BUSH, SoundCategory.BLOCKS, 1.0F, 0.8F + worldIn.rand.nextFloat() * 0.4F);
	         worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(3)), 2);
	         return ActionResultType.func_233537_a_(worldIn.isRemote);
	      } else {
	         return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	      }
	   }
	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
	      if (entityIn instanceof LivingEntity && entityIn.getType() == EntityType.BEE && entityIn.serializeNBT().getByte("HasNectar")==1) {
	         if (state.get(AGE) == 4){
	        	 worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(5)), 2);
	         }
	      }
	      if(entityIn instanceof LivingEntity && entityIn.getType() != EntityType.BEE){
	    	  entityIn.setMotionMultiplier(state, new Vector3d(0.5D, (double)0.5F, 0.5D));
	      }
	   }
	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(AGE) < 5;
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state) {
		int i = Math.min(3, state.get(AGE) + 1);
	    worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(i)), 2);
	}
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
	      builder.add(AGE);
	   }
	public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
	      return true;
	   }
	
}
