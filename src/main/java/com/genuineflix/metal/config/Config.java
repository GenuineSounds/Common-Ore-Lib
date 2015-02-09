package com.genuineflix.metal.config;

import java.io.File;
import java.util.Arrays;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.genuineflix.metal.event.OreGenerationEvent;
import com.genuineflix.metal.interfaces.IOre;
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

	public void pre() {
		OreGenerationEvent.restrictOreGen = common.getBoolean("restrictOreGen", "Options", true, "Intrusively restrict ore generation for ores that are the same as the common ores. (recommended)");
		genAlloys = common.getBoolean("genAlloys", "Options", true, "Generate alloy ores in world. This might break balance of other mods if rareAlloys is disabled. (recommended)");
		rareAlloys = common.getBoolean("rareAlloys", "Options", true, "Generation of alloy ores only occur when the two components of the ore appear close together. (recommended)");
		flatBedrock = common.getBoolean("flatBedrock", "Options", false, "Generate flat bedrock in the Over-world and Nether. (You probably have a mod that does this already)");
		searchRadius = common.getInt("searchRadius", "Options", 2, 1, 8, "Radius for rare alloys to search for their required component ores.");
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

	public void init() {
		for (final Metal metal : MetalRegistry.getMetals()) {
			final IOre.Property properties = new IOre.Property(getRarity(metal), getDepth(metal), getNodes(metal), getSize(metal), getSpread(metal), getHardness(metal), getResistance(metal));
			metal.setProperty(properties);
			if (!metal.isAlloy())
				continue;
			final String[] names = getComponentNames(metal);
			final IOre.Component[] components = new IOre.Component[names.length];
			for (int i = 0; i < components.length; i++)
				components[i] = new IOre.Component(names[i]);
			metal.setComponents(components);
		}
		metals.save();
	}

	public void post() {
		for (final Metal metal : MetalRegistry.getMetals())
			metal.setGeneration(getGeneration(metal));
		metals.save();
	}

	private String[] getComponentNames(final Metal metal) {
		String[] names = new String[0];
		for (int i = 0; i < metal.getComponents().size(); i++) {
			final IOre.Component comp = metal.getComponents().get(i);
			for (int j = 0; j < comp.factor; j++) {
				names = Arrays.copyOf(names, names.length + 1);
				names[names.length - 1] = comp.name;
			}
		}
		final Property prop = metals.get("components", metal.name, names);
		prop.setValidValues(MetalRegistry.COMMON_NAMES);
		prop.setLanguageKey("CommonOre.components");
		return prop.getStringList();
	}

	private float getDepth(final Metal metal) {
		return getFloat(metal, "depth", metal.getProperty().depth, 0, 1);
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
			return value;
		}
	}

	private boolean getGeneration(final Metal metal) {
		final Property prop = metals.get("generation", metal.name, metal.getProperty().canGenerate());
		prop.setLanguageKey("CommonOre.generation");
		return prop.getBoolean(metal.getProperty().canGenerate());
	}

	private float getHardness(final Metal metal) {
		return getFloat(metal, "hardness", metal.getProperty().hardness, 0, 100);
	}

	private int getInt(final Metal metal, final String category, final int value, final int min, final int max) {
		final Property prop = metals.get(category, metal.name, value);
		prop.setLanguageKey("CommonOre." + category);
		prop.setMinValue(min);
		prop.setMaxValue(max);
		return prop.getInt(value) < min ? min : prop.getInt(value) > max ? max : prop.getInt(value);
	}

	private int getNodes(final Metal metal) {
		return getInt(metal, "nodes", metal.getProperty().nodes, 1, 8);
	}

	private float getRarity(final Metal metal) {
		return getFloat(metal, "rarity", metal.getProperty().rarity, 0, 1);
	}

	private float getResistance(final Metal metal) {
		return getFloat(metal, "resistance", metal.getProperty().resistance, 0, 100);
	}

	private int getSize(final Metal metal) {
		return getInt(metal, "size", metal.getProperty().size, 1, 16);
	}

	private float getSpread(final Metal metal) {
		return getFloat(metal, "spread", metal.getProperty().spread, 0, 1);
	}
}
