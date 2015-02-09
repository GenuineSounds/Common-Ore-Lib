package com.genuineflix.metal.registry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.interfaces.IOre;

public class Metal implements IOre {

	public final String name;
	public final String nameFixed;
	public Block ore;
	public Block block;
	public Item dust;
	public Item ingot;
	public Item nugget;
	private IOre.Property property;
	private List<IOre.Component> components = new ArrayList<IOre.Component>();

	public Metal(final String name) {
		this.name = name;
		nameFixed = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	}

	@Override
	public List<IOre.Component> getComponents() {
		return components;
	}

	@Override
	public Property getProperty() {
		return property;
	}

	@Override
	public boolean isAlloy() {
		return components != null && components.size() > 0;
	}

	@Override
	public void setComponents(final Component... comps) {
		components = new ArrayList<Component>();
		for (int i = 0; i < comps.length; i++) {
			final Component comp = comps[i];
			if (!components.isEmpty() && components.contains(comp))
				components.get(components.indexOf(comp)).factor++;
			else
				components.add(comp);
		}
	}

	public void setGeneration(final boolean generate) {
		if (!property.canGenerate() && generate)
			CommonOre.log.info("Setting " + nameFixed + " implicitly to generate");
		if (property.canGenerate() && !generate)
			CommonOre.log.info("Setting " + nameFixed + " explicitly to not generate");
		property.setGenerate(generate);
	}

	@Override
	public void setProperty(final Property property) {
		this.property = property;
	}

	void setup(final Property property) {
		setProperty(property);
	}

	void finallize() {
		ore.setHardness(property.hardness);
		ore.setResistance(property.resistance);
		block.setHardness(property.hardness);
		block.setResistance(property.resistance);
	}
}
