package com.genuineflix.metal.block;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockOre extends ItemBlock {

	public final String name;

	public ItemBlockOre(final Block block, final String name) {
		super(block);
		this.name = name;
		setHasSubtypes(true);
		setMaxDamage(0);
		setUnlocalizedName("ore" + name);
	}

	@Override
	public String getItemStackDisplayName(final ItemStack item) {
		final String prefix = Ore.NAMES[item.getItemDamage() % Ore.NAMES.length];
		final String out = I18n.format(getUnlocalizedName() + ".name");
		if (prefix.isEmpty())
			return out;
		return out.replace("Ore", prefix + "ore");
	}

	@Override
	public String getUnlocalizedName(final ItemStack item) {
		return field_150939_a.getUnlocalizedName();
	}

	@Override
	public int getMetadata(final int i) {
		return i;
	}
}
