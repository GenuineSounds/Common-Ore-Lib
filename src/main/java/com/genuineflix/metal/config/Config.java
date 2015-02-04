package com.genuineflix.metal.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.genuineflix.metal.interfaces.IAlloy.Component;
import com.genuineflix.metal.interfaces.IOre.Properties;
import com.genuineflix.metal.registry.Metal;
import com.genuineflix.metal.registry.MetalRegistry;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Config {

	public boolean flatBedrock;
	public boolean genAlloys;
	public boolean rareAlloys;
	public int searchRadius;
	public Configuration common;
	public Configuration metals;

	public Config(final FMLPreInitializationEvent event) {
		final File folder = new File(event.getModConfigurationDirectory(), "CommonOre");
		folder.mkdirs();
		common = new Configuration(new File(folder, "Common.cfg"));
		common.save();
		metals = new Configuration(new File(folder, "Metals.cfg"));
		metals.save();
	}

	private String[] getComponentNames(final Metal metal) {
		final String[] names = new String[metal.getComponents().length];
		for (int i = 0; i < metal.getComponents().length; i++)
			names[i] = metal.getComponents()[i].name;
		final Property prop = metals.get("components", metal.name, names);
		prop.setValidValues(MetalRegistry.instance.getMetalNames());
		prop.setLanguageKey("CommonOre.components");
		return prop.getStringList();
	}

	private float getDepth(final Metal metal) {
		return getFloat(metal, "depth", metal.getProperties().depth, 0, 1);
	}

	private float getFloat(final Metal metal, final String category, final float value, final float min, final float max) {
		final Property prop = metals.get(category, metal.name, Float.toString(value));
		prop.setLanguageKey("CommonOre." + category);
		prop.setMinValue(min);
		prop.setMaxValue(max);
		try {
			return Float.parseFloat(prop.getString()) < min ? min : Float.parseFloat(prop.getString()) > max ? max : Float.parseFloat(prop.getString());
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	private boolean getGeneration(final Metal metal) {
		final Property prop = metals.get("generation", metal.name, metal.generate());
		prop.setLanguageKey("CommonOre.generation");
		return prop.getBoolean(metal.generate());
	}

	private float getHardness(final Metal metal) {
		return getFloat(metal, "hardness", metal.getProperties().hardness, 0, 100);
	}

	private int getInt(final Metal metal, final String category, final int value, final int min, final int max) {
		final Property prop = metals.get(category, metal.name, value);
		prop.setLanguageKey("CommonOre." + category);
		prop.setMinValue(min);
		prop.setMaxValue(max);
		return prop.getInt(value) < min ? min : prop.getInt(value) > max ? max : prop.getInt(value);
	}

	private int getNodes(final Metal metal) {
		return getInt(metal, "nodes", metal.getProperties().nodes, 1, 8);
	}

	private float getRarity(final Metal metal) {
		return getFloat(metal, "rarity", metal.getProperties().rarity, 0, 1);
	}

	private float getResistance(final Metal metal) {
		return getFloat(metal, "resistance", metal.getProperties().resistance, 0, 100);
	}

	private int getSize(final Metal metal) {
		return getInt(metal, "size", metal.getProperties().size, 1, 16);
	}

	private float getSpread(final Metal metal) {
		return getFloat(metal, "spread", metal.getProperties().spread, 0, 1);
	}

	public void init() {
		for (int metalCounter = 0; metalCounter < MetalRegistry.instance.getAllMetals().size(); metalCounter++) {
			final Metal metal = MetalRegistry.instance.getAllMetals().get(metalCounter);
			final Properties properties = new Properties(getRarity(metal), getDepth(metal), getNodes(metal), getSize(metal), getSpread(metal), getHardness(metal), getResistance(metal));
			metal.setup(properties);
			if (!metal.isAlloy())
				continue;
			final String[] componentNames = getComponentNames(metal);
			final Component[] components = new Component[componentNames.length];
			for (int componentCounter = 0; componentCounter < components.length; componentCounter++)
				components[componentCounter] = new Component(componentNames[componentCounter], 1); //componentFactors[componentCounter]);
			metal.setComponents(components);
		}
		metals.save();
	}

	public void post() {
		for (int i = 0; i < MetalRegistry.instance.getAllMetals().size(); i++) {
			final Metal metal = MetalRegistry.instance.getAllMetals().get(i);
			metal.setGeneration(getGeneration(metal));
		}
		metals.save();
	}

	public void pre() {
		genAlloys = common.getBoolean("genAlloys", "Options", true, "Generate alloy ores in world. Will break balance of other mods if rareAlloys is disabled (recommended)");
		rareAlloys = common.getBoolean("rareAlloys", "Options", true, "Generation of alloy ores only occur when the two components of the ore appear close together (recommended)");
		flatBedrock = common.getBoolean("flatBedrock", "Options", false, "Generate flat bedrock in the Over-world and Nether. Off by default since you probably already have a mod that does this.");
		searchRadius = common.getInt("searchRadius", "Options", 4, 1, 16, "Radius for rare alloys to search in each direction to find their component ores. Higher numbers will take longer to generate");
		common.save();
		metals.getCategory("rarity").setComment("Chance of generating in any given chunk [range: 0.0 ~ 1.0]");
		metals.getCategory("depth").setComment("Depth at which the ore is most common (Percentage / 64) [range: 0.0 ~ 1.0]");
		metals.getCategory("nodes").setComment("How many nodes have a chance to generate in a chunk [range: 1 ~ 8]");
		metals.getCategory("size").setComment("How many ore can generate in each node [range: 1 ~ 16]");
		metals.getCategory("spread").setComment("How far can the ore generation deviate from its depth (Percentage / 64) [range: 0.0 ~ 1.0]");
		metals.getCategory("hardness").setComment("How easy is the metal to harvest [range: 0.0 ~ 100.0]");
		metals.getCategory("resistance").setComment("How resistant are the blocks to explosions [range: 0.0 ~ 100.0]");
		metals.getCategory("components").setComment("Components of the alloy");
		metals.getCategory("generation").setComment("Whether the ore will generate");
		metals.save();
	}
}
