package com.genuineflix.metal.registry;

import net.minecraft.nbt.NBTTagCompound;

import com.genuineflix.metal.api.IMetal;

public class MetalFactory {

	public static IMetal from(NBTTagCompound tag) {
		if (tag == null || !tag.hasKey("class"))
			return null;
		IMetal metal = null;
		try {
			String className = tag.getString("class");
			ClassLoader cl = ClassLoader.getSystemClassLoader();
			Class<?> clazz = cl.loadClass(className);
			metal = (IMetal) clazz.newInstance();
			metal.load(tag);
		} catch (Exception e) {}
		return metal;
	}

	public static NBTTagCompound to(IMetal metal) {
		return metal.save(new NBTTagCompound());
	}
}
