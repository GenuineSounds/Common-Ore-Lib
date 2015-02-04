package com.genuineflix.metal.interfaces;

public interface IOre {

	public static class Properties {

		public final float rarity;
		public final float depth;
		public final int nodes;
		public final int size;
		public final float spread;
		public final float hardness;
		public final float resistance;
		public final boolean generate;

		public Properties(final float rarity, final float depth, final int nodes, final int size, final float spread, final float hardness, final float resistance) {
			this(rarity, depth, nodes, size, spread, hardness, resistance, false);
		}

		public Properties(final float rarity, final float depth, final int nodes, final int size, final float spread, final float hardness, final float resistance, final boolean generate) {
			this.rarity = rarity;
			this.depth = depth;
			this.nodes = nodes;
			this.size = size;
			this.spread = spread;
			this.hardness = hardness;
			this.resistance = resistance;
			this.generate = generate;
		}
	}

	public Properties getProperties();

	public void setOreProperties(Properties props);
}
