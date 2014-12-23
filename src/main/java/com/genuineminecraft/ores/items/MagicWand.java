package com.genuineminecraft.ores.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.genuineminecraft.ores.registry.MetalRegistry;

public class MagicWand extends ItemShears {

	public static MagicWand instance;

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		int size = 16;
		int x = (int) Math.floor(player.posX);
		int y = (int) Math.floor(player.posY - 0.5);
		int z = (int) Math.floor(player.posZ);
		for (int bx = x - size; bx < x + size; bx++) {
			for (int bz = z - size; bz < z + size; bz++) {
				for (int by = 1; by < 96; by++) {
					Block block = world.getBlock(bx, by, bz);
					if (!MetalRegistry.isCommon(block.getUnlocalizedName()))
						world.setBlock(bx, by, bz, Blocks.air, 0, 6);
				}
			}
		}
		return stack;
	}
}
