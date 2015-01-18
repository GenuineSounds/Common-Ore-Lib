package com.genuineminecraft.ores.interfaces;

public interface IAlloy {

	public String getPrimaryComponent();

	public String getSecondaryComponent();

	public boolean isAlloy();

	public void setComponents(String primary, String secondary);
}
