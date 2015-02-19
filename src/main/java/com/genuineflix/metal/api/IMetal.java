package com.genuineflix.metal.api;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

public interface IMetal extends SaveableData, LoadableData<IMetal> {

	public String getName();

	public String getDisplayName();

	public Block getOre();

	public Block getBlock();

	public Item getIngot();

	public Item getNugget();

	public Item getDust();

	public Generation getGeneration();

	public List<Compound> getCompounds();

	public void setGeneration(Generation generation);

	public void setCompounds(Compound... components);

	public boolean isComposite();

	public static class Generation implements SaveableData, LoadableData<Generation> {

		public float rarity;
		public float depth;
		public int nodes;
		public int size;
		public float spread;
		public float hardness;
		public float resistance;
		private boolean generate;

		public Generation() {}

		public Generation(final float rarity, final float depth, final int nodes, final int size, final float spread, final float hardness, final float resistance) {
			this(rarity, depth, nodes, size, spread, hardness, resistance, false);
		}

		public Generation(final float rarity, final float depth, final int nodes, final int size, final float spread, final float hardness, final float resistance, final boolean generate) {
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
		public NBTTagCompound save(final NBTTagCompound compound) {
			compound.setFloat("rarity", rarity);
			compound.setFloat("depth", depth);
			compound.setInteger("nodes", nodes);
			compound.setInteger("size", size);
			compound.setFloat("spread", spread);
			compound.setFloat("hardness", hardness);
			compound.setFloat("resistance", resistance);
			compound.setBoolean("generate", generate);
			return compound;
		}

		@Override
		public Generation load(final NBTTagCompound compound) {
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
	}

	public static class Compound implements SaveableData, LoadableData<Compound> {

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

		@Override
		public NBTTagCompound save(final NBTTagCompound compound) {
			compound.setString("class", metal.getClass().getName());
			compound.setTag("metal", metal.save(new NBTTagCompound()));
			compound.setInteger("factor", factor);
			return compound;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Compound load(final NBTTagCompound compound) {
			try {
				metal = ((Class<IMetal>) ClassLoader.getSystemClassLoader().loadClass(compound.getString("class"))).newInstance().load(compound.getCompoundTag("metal"));
				factor = compound.getInteger("factor");
				return this;
			}
			catch (final Exception e) {}
			return null;
		}
	}
}

interface LoadableData<T> {

	public T load(NBTTagCompound compound);
}

interface SaveableData {

	public NBTTagCompound save(NBTTagCompound compound);
}
