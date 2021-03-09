package ru.vital1ks.gardenmod.common.tiles;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.IClearable;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import ru.vital1ks.gardenmod.GardenMod;
import ru.vital1ks.gardenmod.core.init.ItemInit;
import ru.vital1ks.gardenmod.core.init.TileEntityTypeInit;
import ru.vital1ks.gardenmod.recipes.DryingRecipe;
import ru.vital1ks.gardenmod.recipes.IGardenRecipe;
import ru.vital1ks.gardenmod.recipes.IGardenRecipeType;

public class DryingTableTileEntity extends TileEntity implements IClearable, ITickableTileEntity {
	private ITextComponent customName;
	private final NonNullList<ItemStack> inventory = NonNullList.withSize(5, ItemStack.EMPTY);
	private final int[] cookingTimes = new int[5];
	private final int[] cookingTotalTimes = new int[5];

	public DryingTableTileEntity() {
		super(TileEntityTypeInit.DRYING_TABLE.get());
	}

	public void tick() {
		World world = this.world;
		boolean flag1 = this.world.isRemote;
		if (!flag1) {
			if (world.getDimensionType().hasSkyLight()) {
				for (int i = 0; i < this.inventory.size(); ++i) {
					ItemStack itemstack = this.inventory.get(i);
					if (!itemstack.isEmpty()) {
						int j = this.cookingTimes[i]++;
						if (this.cookingTimes[i] >= this.cookingTotalTimes[i]) {
							IInventory iinventory = new Inventory(itemstack);
							ItemStack itemstack1 = this.world.getRecipeManager().getRecipe(IRecipeType.CAMPFIRE_COOKING, iinventory, this.world).map((campfireRecipe) -> {
				                  return campfireRecipe.getCraftingResult(iinventory);
				               }).orElse(itemstack);
				               BlockPos blockpos = this.getPos();
				               InventoryHelper.spawnItemStack(this.world, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), itemstack1);
				               this.inventory.set(i, ItemStack.EMPTY);
				               this.inventoryChanged();
						}
					}
				}
			}
		}

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

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	public Optional<DryingRecipe> findMatchingRecipe(ItemStack itemstack) {
		// TODO Auto-generated method stub
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

	private void inventoryChanged() {
		this.markDirty();
		this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), -1);
	}

	public void read(BlockState state, CompoundNBT nbt) {
		super.read(state, nbt);
		this.inventory.clear();
		ItemStackHelper.loadAllItems(nbt, this.inventory);
		if (nbt.contains("CookingTimes", 11)) {
			int[] aint = nbt.getIntArray("CookingTimes");
			System.arraycopy(aint, 0, this.cookingTimes, 0, Math.min(this.cookingTotalTimes.length, aint.length));
		}

		if (nbt.contains("CookingTotalTimes", -1)) {
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
		return new SUpdateTileEntityPacket(this.pos, -1, this.getUpdateTag());
	}

	public CompoundNBT getUpdateTag() {
		return this.writeItems(new CompoundNBT());
	}
}
