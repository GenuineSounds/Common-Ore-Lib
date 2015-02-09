package com.genuineflix.metal.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import com.genuineflix.metal.interfaces.IMetal;

public class Metal implements IMetal {

	public final String name;
	public final String nameFixed;
	private Block ore;
	private Block block;
	private Item dust;
	private Item ingot;
	private Item nugget;
	private Settings settings;
	private List<Compound> compounds;
	private boolean manuallyInitiated = false;

	public Metal(final String name) {
		this.name = name;
		nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

	public Metal(final String name, final Settings property, final Compound... compounds) {
		this.name = name;
		nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
		setSettings(property);
		setCompounds(compounds);
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
		if (manuallyInitiated)
			return;
		ore.setHardness(settings.hardness);
		ore.setResistance(settings.resistance);
		block.setHardness(settings.hardness);
		block.setResistance(settings.resistance);
	}

	public boolean manuallyInitiated() {
		return manuallyInitiated;
	}

	public Metal flagManualInitiation() {
		manuallyInitiated = true;
		return this;
	}

	public Block getOre() {
		return ore;
	}

	void setOre(final Block ore) {
		this.ore = ore;
	}

	public Block getBlock() {
		return block;
	}

	void setBlock(final Block block) {
		this.block = block;
	}

	public Item getDust() {
		return dust;
	}

	void setDust(final Item dust) {
		this.dust = dust;
	}

	public Item getIngot() {
		return ingot;
	}

	void setIngot(final Item ingot) {
		this.ingot = ingot;
	}

	public Item getNugget() {
		return nugget;
	}

	void setNugget(final Item nugget) {
		this.nugget = nugget;
	}
}
