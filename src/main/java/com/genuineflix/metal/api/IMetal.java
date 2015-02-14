package com.genuineflix.metal.api;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface IMetal { // extends SaveableData, LoadableData<IMetal> {

	public String getName();

	public String getDisplayName();

	public Block getOre();

	public Block getBlock();

	public Item getIngot();

	public Item getNugget();

	public Item getDust();

	public Settings getSettings();

	public List<Compound> getCompounds();

	public void setSettings(Settings props);

	public void setCompounds(Compound... components);

	public boolean isComposite();

	public static class Settings { // implements SaveableData, LoadableData<Settings> {

		public float rarity;
		public float depth;
		public int nodes;
		public int size;
		public float spread;
		public float hardness;
		public float resistance;
		private boolean generate;

		public Settings() {}

		public Settings(final float rarity, final float depth, final int nodes, final int size, final float spread, final float hardness, final float resistance) {
			this(rarity, depth, nodes, size, spread, hardness, resistance, false);
		}

		public Settings(final float rarity, final float depth, final int nodes, final int size, final float spread, final float hardness, final float resistance, final boolean generate) {
			this.rarity = rarity;
			this.depth = depth;
			this.nodes = nodes;
			this.size = size;
			this.spread = spread;
			this.hardness = hardness;
			this.resistance = resistance;
			this.generate = generate;
		}

		public boolean canGenerate() {
			return generate;
		}

		public void setGenerate(final boolean generate) {
			this.generate = generate;
		}
		/*
		@Override
		public DataCompound save(final DataCompound compound) {
			compound.set("rarity", rarity);
			compound.set("depth", depth);
			compound.set("nodes", nodes);
			compound.set("size", size);
			compound.set("spread", spread);
			compound.set("hardness", hardness);
			compound.set("resistance", resistance);
			compound.set("generate", generate);
			return compound;
		}

		@Override
		public Settings load(final DataCompound compound) {
			rarity = compound.getFloat("rarity");
			depth = compound.getFloat("depth");
			nodes = compound.getInteger("nodes");
			size = compound.getInteger("size");
			spread = compound.getFloat("spread");
			hardness = compound.getFloat("hardness");
			resistance = compound.getFloat("resistance");
			generate = compound.hasKey("generate") && compound.getBoolean("generate");
			return this;
		}

		public static Settings fromCompound(final DataCompound compound) {
			return new Settings().load(compound);
		}
		*/
	}

	public static class Compound { // implements SaveableData, LoadableData<Compound> {

		public IMetal metal;
		public int factor;

		public Compound() {}

		public Compound(final IMetal metal) {
			this(metal, 1);
		}

		public Compound(final IMetal metal, final int factor) {
			this.metal = metal;
			this.factor = factor;
		}

		@Override
		public boolean equals(final Object obj) {
			if (super.equals(obj))
				return true;
			return obj instanceof Compound && metal.equals(((Compound) obj).metal);
		}
		/*
		@Override
		public DataCompound save(final DataCompound compound) {
			compound.set("class", metal.getClass().getName());
			compound.set("metal", metal.save(new DataCompound()));
			compound.set("factor", factor);
			return compound;
		}

		@Override
		public Compound load(final DataCompound compound) {
			try {
				final ClassLoader cl = ClassLoader.getSystemClassLoader();
				final Class<IMetal> clazz = (Class<IMetal>) cl.loadClass(compound.getString("class"));
				metal = clazz.newInstance().load(compound.getCompound("metal"));
				factor = compound.getInteger("factor");
			}
			catch (final Exception e) {
				return null;
			}
			return this;
		}

		public static Compound from(final DataCompound compound) {
			return new Compound().load(compound);
		}
		*/
	}
}
/*
interface LoadableData<T> {

	public T load(DataCompound compound);
}

interface SaveableData {

	public DataCompound save(DataCompound compound);
}
*/