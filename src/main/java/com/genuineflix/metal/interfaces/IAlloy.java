package com.genuineflix.metal.interfaces;

public interface IAlloy extends IOre {

	public static class Component {

		public final String name;
		public final int factor;

		public Component(final String name) {
			this(name, 1);
		}

		public Component(final String name, final int factor) {
			this.name = name;
			this.factor = factor;
		}
	}

	public Component[] getComponents();

	public boolean isAlloy();

	public void setComponents(Component... components);
}
