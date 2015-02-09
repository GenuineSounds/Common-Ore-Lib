package com.genuineflix.metal.interfaces;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

public interface IOre {

	public static class Property {

		public static Property fromNBT(final NBTTagCompound nbt) {
			final float rarity = nbt.getFloat("rarity");
			final float depth = nbt.getFloat("depth");
			final int nodes = nbt.getInteger("nodes");
			final int size = nbt.getInteger("size");
			final float spread = nbt.getFloat("spread");
			final float hardness = nbt.getFloat("hardness");
			final float resistance = nbt.getFloat("resistance");
			return new Property(rarity, depth, nodes, size, spread, hardness, resistance, nbt.hasKey("generate") && nbt.getBoolean("generate"));
		}

		public final float rarity;
		public final float depth;
		public final int nodes;
		public final int size;
		public final float spread;
		public final float hardness;
		public final float resistance;
		private boolean generate;

		public Property(final float rarity, final float depth, final int nodes, final int size, final float spread, final float hardness, final float resistance) {
			this(rarity, depth, nodes, size, spread, hardness, resistance, false);
		}

		public Property(final float rarity, final float depth, final int nodes, final int size, final float spread, final float hardness, final float resistance, final boolean generate) {
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

	public static class Component {

		public final String name;
		public int factor;

		public Component(final String name) {
			this(name, 1);
		}

		public Component(final String name, final int factor) {
			this.name = name;
			this.factor = factor;
		}

		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof Component)
				return name.equals(((Component) obj).name);
			return super.equals(obj);
		}
	}

	public boolean isAlloy();

	public List<Component> getComponents();

	public void setComponents(Component... components);

	public Property getProperty();

	public void setProperty(Property props);
}
