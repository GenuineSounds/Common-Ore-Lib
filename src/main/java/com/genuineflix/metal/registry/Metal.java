package com.genuineflix.metal.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.genuineflix.data.collections.DataCompound;
import com.genuineflix.data.collections.DataList;
import com.genuineflix.data.helpers.NBTHelper;
import com.genuineflix.metal.api.IMetal;

public class Metal implements IMetal {

	private String name;
	private String displayName;
	private Block ore;
	private Block block;
	private Item dust;
	private Item ingot;
	private Item nugget;
	private Settings settings;
	private List<Compound> compounds;
	private boolean manual;

	Metal() {}

	Metal(final String name) {
		this.name = name;
		displayName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

	Metal(final String name, final Settings property, final Compound... compounds) {
		this.name = name;
		displayName = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		setSettings(property);
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
	public Settings getSettings() {
		return settings;
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
	public void setSettings(final Settings settings) {
		this.settings = settings;
	}

	public void setCompounds(final List<Compound> compounds) {
		this.compounds = compounds;
	}

	void finallize() {
		if (manual)
			return;
		ore.setHardness(settings.hardness);
		ore.setResistance(settings.resistance);
		block.setHardness(settings.hardness);
		block.setResistance(settings.resistance);
	}

	public boolean isManual() {
		return manual;
	}

	public Metal manual() {
		manual = true;
		return this;
	}

	@Override
	public DataCompound save(final DataCompound data) {
		data.setString("name", name);
		data.setString("displayName", displayName);
		data.setData("ore", NBTHelper.create(new ItemStack(ore)));
		data.setData("block", NBTHelper.create(new ItemStack(block)));
		data.setData("dust", NBTHelper.create(new ItemStack(dust)));
		data.setData("ingot", NBTHelper.create(new ItemStack(ingot)));
		data.setData("nugget", NBTHelper.create(new ItemStack(nugget)));
		if (settings != null)
			data.setData("settings", settings.save(new DataCompound()));
		if (compounds != null && !compounds.isEmpty()) {
			final DataList comps = new DataList();
			for (int i = 0; i < compounds.size(); i++)
				comps.add(compounds.get(i).save(new DataCompound()));
			data.setData("compounds", comps);
		}
		if (manual)
			data.setBoolean("manual", manual);
		return data;
	}

	@Override
	public IMetal load(final DataCompound data) {
		name = data.getString("name");
		displayName = data.getString("displayName");
		ore = ((ItemBlock) NBTHelper.create(data.getCompound("ore")).getItem()).field_150939_a;
		block = ((ItemBlock) NBTHelper.create(data.getCompound("block")).getItem()).field_150939_a;
		dust = NBTHelper.create(data.getCompound("dust")).getItem();
		ingot = NBTHelper.create(data.getCompound("ingot")).getItem();
		nugget = NBTHelper.create(data.getCompound("nugget")).getItem();
		if (data.hasKey("settings"))
			settings = Settings.fromCompound(data.getCompound("settings"));
		if (data.hasKey("compounds")) {
			final DataList list = data.getList("compounds", DataCompound.TYPE);
			final List<Compound> compounds = new ArrayList<Compound>();
			for (int i = 0; i < list.size(); i++)
				compounds.add(Compound.from(list.getCompound(i)));
			this.compounds = compounds;
		}
		return this;
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
}
