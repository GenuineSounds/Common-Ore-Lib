package ninja.genuine.metal.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import ninja.genuine.metal.api.IMetal;

class Metal implements IMetal {

	private String name;
	private String displayName;
	private Block ore;
	private Block block;
	private Item dust;
	private Item ingot;
	private Item nugget;
	private Generation generation;
	private List<Compound> compounds;

	Metal(final String name) {
		this.name = name;
		displayName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

	Metal(final String name, final Generation generation, final Compound... compounds) {
		this.name = name;
		displayName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		setGeneration(generation);
		setCompounds(compounds);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public List<Compound> getCompounds() {
		return compounds;
	}

	@Override
	public Generation getGeneration() {
		return generation;
	}

	@Override
	public boolean isComposite() {
		return compounds != null && compounds.size() > 0;
	}

	@Override
	public void setCompounds(final Compound... compounds) {
		this.compounds = new ArrayList<Compound>();
		for (int i = 0; i < compounds.length; i++) {
			final Compound compound = compounds[i];
			if (!this.compounds.isEmpty() && this.compounds.contains(compound))
				this.compounds.get(this.compounds.indexOf(compound)).factor++;
			else
				this.compounds.add(compound);
		}
	}

	@Override
	public void setGeneration(final Generation generation) {
		this.generation = generation;
	}

	public void setCompounds(final List<Compound> compounds) {
		this.compounds = compounds;
	}

	void finallize() {
		if (generation == null)
			return;
		ore.setHardness(generation.hardness);
		ore.setResistance(generation.resistance);
		block.setHardness(generation.hardness);
		block.setResistance(generation.resistance);
	}

	@Override
	public Block getOre() {
		return ore;
	}

	@Override
	public Block getBlock() {
		return block;
	}

	@Override
	public Item getDust() {
		return dust;
	}

	@Override
	public Item getIngot() {
		return ingot;
	}

	@Override
	public Item getNugget() {
		return nugget;
	}

	void setOre(final Block ore) {
		this.ore = ore;
	}

	void setBlock(final Block block) {
		this.block = block;
	}

	void setDust(final Item dust) {
		this.dust = dust;
	}

	void setIngot(final Item ingot) {
		this.ingot = ingot;
	}

	void setNugget(final Item nugget) {
		this.nugget = nugget;
	}

	@Override
	public NBTTagCompound save(final NBTTagCompound data) {
		data.setString("class", this.getClass().getName());
		data.setString("name", name);
		data.setString("displayName", displayName);
		data.setTag("ore", new ItemStack(ore).writeToNBT(new NBTTagCompound()));
		data.setTag("block", new ItemStack(block).writeToNBT(new NBTTagCompound()));
		data.setTag("dust", new ItemStack(dust).writeToNBT(new NBTTagCompound()));
		data.setTag("ingot", new ItemStack(ingot).writeToNBT(new NBTTagCompound()));
		data.setTag("nugget", new ItemStack(nugget).writeToNBT(new NBTTagCompound()));
		if (generation != null)
			data.setTag("generation", generation.save(new NBTTagCompound()));
		if (compounds != null && !compounds.isEmpty()) {
			final NBTTagList comps = new NBTTagList();
			for (int i = 0; i < compounds.size(); i++)
				comps.appendTag(compounds.get(i).save(new NBTTagCompound()));
			data.setTag("compounds", comps);
		}
		return data;
	}

	@Override
	public Metal load(final NBTTagCompound data) {
		name = data.getString("name");
		displayName = data.getString("displayName");
		ore = ((ItemBlock) ItemStack.loadItemStackFromNBT(data.getCompoundTag("ore")).getItem()).field_150939_a;
		block = ((ItemBlock) ItemStack.loadItemStackFromNBT(data.getCompoundTag("block")).getItem()).field_150939_a;
		dust = ItemStack.loadItemStackFromNBT(data.getCompoundTag("dust")).getItem();
		ingot = ItemStack.loadItemStackFromNBT(data.getCompoundTag("ingot")).getItem();
		nugget = ItemStack.loadItemStackFromNBT(data.getCompoundTag("nugget")).getItem();
		if (data.hasKey("generation"))
			generation = new Generation().load(data.getCompoundTag("generation"));
		if (data.hasKey("compounds")) {
			final NBTTagList list = data.getTagList("compounds", 10);
			final List<Compound> compounds = new ArrayList<Compound>();
			for (int i = 0; i < list.tagCount(); i++)
				compounds.add(new Compound().load(list.getCompoundTagAt(i)));
			this.compounds = compounds;
		}
		return this;
	}
}
