package ru.vital1ks.gardenmod.common.tiles;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import ru.vital1ks.gardenmod.GardenMod;
import ru.vital1ks.gardenmod.common.blocks.DryingTable;
import ru.vital1ks.gardenmod.core.init.ItemInit;
import ru.vital1ks.gardenmod.core.init.TileEntityTypeInit;
import ru.vital1ks.gardenmod.recipes.DryingRecipe;
import ru.vital1ks.gardenmod.recipes.DryingRecipes;
import ru.vital1ks.gardenmod.recipes.IGardenRecipe;
import ru.vital1ks.gardenmod.recipes.IGardenRecipeType;

public class DryingTableTileEntity extends TileEntity implements IClearable, ITickableTileEntity {
	private ITextComponent customName;
	private final NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);
	private final int[] cookingTimes = new int[5];
	private final int[] cookingTotalTimes = new int[5];
	private int nextTake = 0;
	public boolean isHold = false;
	private BlockState oldState;

	public DryingTableTileEntity() {
		super(TileEntityTypeInit.DRYING_TABLE.get());
	}

	public void tick() {
		if (!isHold) {
			nextTake = 0;
		}
		World world = this.world;
		boolean flag1 = this.world.isRemote;
		if (!flag1) {
			if (world.canBlockSeeSky(getPos())) {
				BlockPos blockpos = this.getPos();
				addParticle(world, blockpos);
				for (int i = 0; i < this.inventory.size(); ++i) {
					ItemStack itemstack = this.inventory.get(i);
					if (!itemstack.isEmpty()) {
						int j = this.cookingTimes[i]++;
						if (this.cookingTimes[i] >= this.cookingTotalTimes[i]) {
							IInventory iinventory = new Inventory(itemstack);
							Item item = this.inventory.get(i).getItem();
							Item item1 = getDryingRecipe(item);
							if (item1 != null) {
								int l = this.getBlockState().get(DryingTable.FACING).getHorizontalIndex();
								this.inventory.set(i, item1.getDefaultInstance());
							}
							this.inventoryChanged();
						}
					}
				}
			}
		}

	}

	public Item getDryingRecipe(Item input) {
		if (input == Items.APPLE.asItem()) {
			return Items.GOLDEN_APPLE.asItem();
		} else {
			return null;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void addParticle(World world, BlockPos blockpos){
		world.addParticle(ParticleTypes.CLOUD, blockpos.getX() + 1D, blockpos.getY() + 0.5D,
				blockpos.getX() + 0.5D, 0.0D, 5.0E-4D, 0.0D);
	}
	@Override
	public void clear() {
		this.inventory.clear();
	}

	public Optional<DryingRecipe> findMatchingRecipe(ItemStack itemstack) {
		return null;
	}

	public NonNullList<ItemStack> getInventory() {
		return this.inventory;
	}

	public void setCustomName(ITextComponent name) {
		this.customName = name;
	}

	public ITextComponent getName() {
		return this.customName != null ? this.customName : this.getDefaultName();
	}

	private ITextComponent getDefaultName() {
		return new TranslationTextComponent("container." + GardenMod.MOD_ID + ".drying_table");
	}

	public ITextComponent getDisplayName() {
		return this.getName();
	}

	public ItemStack getStackInSlot(int index) {
		return this.inventory.get(index);
	}

	@Nullable
	public ITextComponent getCustomName() {
		return this.customName;
	}

	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		this.inventory.clear();
		ItemStackHelper.loadAllItems(nbt, this.inventory);
		if (nbt.contains("CookingTimes", 0)) {
			int[] aint = nbt.getIntArray("CookingTimes");
			System.arraycopy(aint, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, aint.length));
		}

		if (nbt.contains("CookingTotalTimes", 0)) {
			int[] aint1 = nbt.getIntArray("CookingTotalTimes");
			System.arraycopy(aint1, 0, this.cookingTotalTimes, 0,
					Math.min(this.cookingTotalTimes.length, aint1.length));
		}

	}

	public CompoundNBT write(CompoundNBT compound) {
		this.writeItems(compound);
		compound.putIntArray("CookingTimes", this.cookingTimes);
		compound.putIntArray("CookingTotalTimes", this.cookingTotalTimes);
		return compound;
	}

	private CompoundNBT writeItems(CompoundNBT compound) {
		super.write(compound);
		ItemStackHelper.saveAllItems(compound, this.inventory, true);
		return compound;
	}

	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.write(nbt);
		this.writeItems(nbt);
		return new SUpdateTileEntityPacket(this.getPos(), 0, nbt);

	}

	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.read(this.getBlockState(), packet.getNbtCompound());
	}

	public CompoundNBT getUpdateTag() {
		return this.writeItems(new CompoundNBT());
	}

	public void handleUpdateTag(CompoundNBT tag) {
		this.read(this.getBlockState(), tag);
	}

	public boolean addItem(ItemStack itemStackIn, int cookTime) {
		for (int i = 0; i < this.inventory.size(); ++i) {
			ItemStack itemstack = this.inventory.get(i);
			if (itemstack.isEmpty()) {
				this.cookingTotalTimes[i] = cookTime;
				this.cookingTimes[i] = 0;
				this.inventory.set(i, itemStackIn.split(1));
				this.inventoryChanged();
				return true;
			}
		}

		return false;
	}

	public boolean takeItem(BlockPos blockpos) {

		for (int i = 4; i >= 0; --i) {
			ItemStack itemstack = this.inventory.get(i);
			if (!itemstack.isEmpty()) {
				if (isHold) {
					if (nextTake == 0) {
						nextTake = 100;
						InventoryHelper.spawnItemStack(this.world, (double) blockpos.getX(), (double) blockpos.getY(),
								(double) blockpos.getZ(), itemstack);
						this.inventory.set(i, ItemStack.EMPTY);
						this.inventoryChanged();
						return true;
					} else {
						--nextTake;
					}
					isHold = false;
				} else {
					nextTake = 0;
				}
			}

		}

		return false;
	}

	private void inventoryChanged() {
		super.markDirty();
		this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 0);
	}
}
