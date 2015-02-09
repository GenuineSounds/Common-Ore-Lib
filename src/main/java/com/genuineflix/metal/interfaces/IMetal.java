package com.genuineflix.metal.interfaces;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import com.genuineflix.metal.registry.Metal;

public interface IMetal {

	public static class Settings {

		public static Settings fromNBT(final NBTTagCompound nbt) {
			final float rarity = nbt.getFloat("rarity");
			final float depth = nbt.getFloat("depth");
			final int nodes = nbt.getInteger("nodes");
			final int size = nbt.getInteger("size");
			final float spread = nbt.getFloat("spread");
			final float hardness = nbt.getFloat("hardness");
			final float resistance = nbt.getFloat("resistance");
			return new Settings(rarity, depth, nodes, size, spread, hardness, resistance, nbt.hasKey("generate") && nbt.getBoolean("generate"));
		}

		public final float rarity;
		public final float depth;
		public final int nodes;
		public final int size;
		public final float spread;
		public final float hardness;
		public final float resistance;
		private boolean generate;

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

		public void writeToNBT(final NBTTagCompound nbt) {
			nbt.setFloat("rarity", rarity);
			nbt.setFloat("depth", depth);
			nbt.setInteger("nodes", nodes);
			nbt.setInteger("size", size);
			nbt.setFloat("spread", spread);
			nbt.setFloat("hardness", hardness);
			nbt.setFloat("resistance", resistance);
			nbt.setBoolean("generate", generate);
		}
	}

	public static class Compound {

		public Metal metal;
		public int factor;

		public Compound(final Metal metal) {
			this(metal, 1);
		}

		public Compound(final Metal metal, final int factor) {
			this.metal = metal;
			this.factor = factor;
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof Compound)
				return metal.equals(((Compound) obj).metal);
			return super.equals(obj);
		}
	}

	public boolean isComposite();

	public List<Compound> getCompounds();

	public void setCompounds(Compound... components);

	public Settings getSettings();

	public void setSettings(Settings props);
}
