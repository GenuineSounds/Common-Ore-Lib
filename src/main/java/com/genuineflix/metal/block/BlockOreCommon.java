package com.genuineflix.metal.block;

import java.util.List;

import net.minecraft.block.BlockOre;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.genuineflix.metal.CommonOre;

public class BlockOreCommon extends BlockOre {

	public static final String[] FOLDERS = new String[] {
			"", "nether/", "ender/"
	};
	public static final String[] NAMES = new String[] {
			"", "Nether", "Ender"
	};
	private final String name;
	private final IIcon[] icons = new IIcon[FOLDERS.length];

	public BlockOreCommon(final String name) {
		this.name = name;
	}

	@Override
	public void getSubBlocks(final Item item, final CreativeTabs tab, final List blocks) {
		for (int i = 0; i < icons.length; i++)
			blocks.add(new ItemStack(this, 1, i));
	}

	@Override
	public IIcon getIcon(final int side, final int meta) {
		return icons[meta % icons.length];
	}

	@Override
	public void registerBlockIcons(final IIconRegister registerer) {
		super.registerBlockIcons(registerer);
		for (int i = 0; i < icons.length; i++)
			icons[i] = registerer.registerIcon(CommonOre.MODID + ":ores/" + FOLDERS[i] + name);
	}

	@Override
	public int damageDropped(final int meta) {
		return meta;
	}
}
