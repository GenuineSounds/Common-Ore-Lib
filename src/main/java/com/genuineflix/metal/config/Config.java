package com.genuineflix.metal.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.genuineflix.metal.metals.Metal;
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

	private String getComponent(final Metal metal, final boolean primary) {
		final Property prop = metals.get("components." + metal.name, primary ? "primary" : "secondary", primary ? metal.getPrimary() : metal.getSecondary());
		prop.setValidValues(MetalRegistry.instance.getMetalNames());
		prop.setLanguageKey("CommonOre.components");
		metals.getCategory("components." + metal.name).setComment("[default: " + metal.getPrimary() + ", " + metal.getSecondary() + "]");
		return prop.getString();
	}

	private int getComponentFactor(final Metal metal, final boolean primary) {
		final int min = 1;
		final int max = 8;
		final String category = "recipe." + metal.name;
		final int value = primary ? metal.getPrimaryComponents() : metal.getSecondaryComponents();
		final Property prop = metals.get(category, primary ? metal.getPrimary() : metal.getSecondary(), value);
		prop.setLanguageKey("CommonOre." + category);
		metals.getCategory(category).setComment("[default: " + metal.getPrimary() + "=" + metal.getPrimaryComponents() + ", " + metal.getSecondary() + "=" + metal.getSecondaryComponents() + "]");
		prop.setMinValue(min);
		prop.setMaxValue(max);
		return prop.getInt(value) < min ? min : prop.getInt(value) > max ? max : prop.getInt(value);
	}

	private float getDepth(final Metal metal) {
		return getFloat(metal, "depth", metal.getDepth(), 0, 1);
	}

	private float getFloat(final Metal metal, final String category, final float value, final float min, final float max) {
		final Property prop = metals.get(category, metal.name, Float.toString(value));
		prop.setLanguageKey("CommonOre." + category);
		prop.comment = "[default: " + value + "]";
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
		prop.comment = "[default: " + metal.generate() + "]";
		return prop.getBoolean(metal.generate());
	}

	private float getHardness(final Metal metal) {
		return getFloat(metal, "hardness", metal.getHardness(), 0, 100);
	}

	private int getInt(final Metal metal, final String category, final int value, final int min, final int max) {
		final Property prop = metals.get(category, metal.name, value);
		prop.setLanguageKey("CommonOre." + category);
		prop.comment = "[default: " + value + "]";
		prop.setMinValue(min);
		prop.setMaxValue(max);
		return prop.getInt(value) < min ? min : prop.getInt(value) > max ? max : prop.getInt(value);
	}

	private int getNodes(final Metal metal) {
		return getInt(metal, "nodes", metal.getNodesPerChunk(), 1, 8);
	}

	private float getRarity(final Metal metal) {
		return getFloat(metal, "rarity", metal.getChunkRarity(), 0, 1);
	}

	private float getResistance(final Metal metal) {
		return getFloat(metal, "resistance", metal.getResistance(), 0, 100);
	}

	private int getSize(final Metal metal) {
		return getInt(metal, "size", metal.getNodeSize(), 1, 16);
	}

	private float getSpread(final Metal metal) {
		return getFloat(metal, "spread", metal.getSpread(), 0, 1);
	}

	public void init() {
		for (int i = 0; i < MetalRegistry.instance.getAllMetals().size(); i++) {
			final Metal metal = MetalRegistry.instance.getAllMetals().get(i);
			metal.setup(getRarity(metal), getDepth(metal), getNodes(metal), getSize(metal), getSpread(metal), getHardness(metal), getResistance(metal));
			if (!metal.isAlloy())
				continue;
			metal.setComponents(getComponent(metal, true), getComponentFactor(metal, true), getComponent(metal, false), getComponentFactor(metal, false));
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
		metals.getCategory("spread").setComment("How far can the ore gneration deviate from its depth (Percentage / 64) [range: 0.0 ~ 1.0]");
		metals.getCategory("hardness").setComment("How easy is the metal to harvest [range: 0.0 ~ 100.0]");
		metals.getCategory("resistance").setComment("How resistant are the blocks to explosions [range: 0.0 ~ 100.0]");
		metals.getCategory("components").setComment("Components of the alloy");
		metals.getCategory("recipe").setComment("How many component dusts it takes to craft the alloy dust [range: 1 ~ 8]");
		metals.getCategory("generation").setComment("Whether the ore will generate");
		metals.save();
	}
}
