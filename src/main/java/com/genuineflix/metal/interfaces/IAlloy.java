package com.genuineflix.metal.interfaces;

import java.util.List;

public interface IAlloy extends IOre {

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

	public List<Component> getComponents();

	public boolean isAlloy();

	public void setComponents(Component... components);
}
