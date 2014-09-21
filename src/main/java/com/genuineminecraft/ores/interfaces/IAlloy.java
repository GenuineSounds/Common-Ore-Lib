package com.genuineminecraft.ores.interfaces;

import com.genuineminecraft.ores.metals.Metal;

public interface IAlloy {

	public void setComponents(Metal primary, Metal secondary);

	public Metal getPrimaryComponent();

	public Metal getSecondaryComponent();

	public boolean isAlloy();
}
