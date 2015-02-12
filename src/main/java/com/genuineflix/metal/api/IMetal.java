package com.genuineflix.metal.api;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import com.genuineflix.data.collections.DataCompound;

public interface IMetal extends SaveableData, LoadableData<IMetal> {

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

	public static class Settings implements SaveableData, LoadableData<Settings> {

		public static Settings fromCompound(final DataCompound nbt) {
			return new Settings().load(nbt);
		}

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

		@Override
		public DataCompound save(final DataCompound nbt) {
			nbt.setFloat("rarity", rarity);
			nbt.setFloat("depth", depth);
			nbt.setInteger("nodes", nodes);
			nbt.setInteger("size", size);
			nbt.setFloat("spread", spread);
			nbt.setFloat("hardness", hardness);
			nbt.setFloat("resistance", resistance);
			nbt.setBoolean("generate", generate);
			return nbt;
		}

		@Override
		public Settings load(final DataCompound nbt) {
			rarity = nbt.getFloat("rarity");
			depth = nbt.getFloat("depth");
			nodes = nbt.getInteger("nodes");
			size = nbt.getInteger("size");
			spread = nbt.getFloat("spread");
			hardness = nbt.getFloat("hardness");
			resistance = nbt.getFloat("resistance");
			generate = nbt.hasKey("generate") && nbt.getBoolean("generate");
			return this;
		}
	}

	public static class Compound implements SaveableData, LoadableData<Compound> {

		public static Compound from(final DataCompound nbt) {
			return new Compound().load(nbt);
		}

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
			if (obj instanceof Compound)
				return metal.equals(((Compound) obj).metal);
			return super.equals(obj);
		}

		@Override
		public DataCompound save(final DataCompound nbt) {
			nbt.setString("class", metal.getClass().getName());
			nbt.setData("metal", metal.save(new DataCompound()));
			nbt.setInteger("factor", factor);
			return nbt;
		}

		@Override
		public Compound load(final DataCompound nbt) {
			try {
				final ClassLoader cl = ClassLoader.getSystemClassLoader();
				final Class<IMetal> clazz = (Class<IMetal>) cl.loadClass(nbt.getString("class"));
				metal = clazz.newInstance().load(nbt.getCompound("metal"));
				factor = nbt.getInteger("factor");
			}
			catch (final Exception e) {
				return null;
			}
			return this;
		}
	}
}
