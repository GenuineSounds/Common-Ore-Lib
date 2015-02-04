package com.genuineflix.metal.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.util.Utility;

public class MagicWand extends ItemShears {

	public static MagicWand instance;

	public MagicWand() {
		setUnlocalizedName("magicWand");
		setTextureName(CommonOre.MODID + ":debug/Wand");
		setCreativeTab(Utility.COMMON_TAB);
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack thisStack, final World world, final EntityPlayer player) {
		if (world.isRemote)
			return thisStack;
		final int square = 2;
		for (int ix = -square; ix <= square; ix++)
			for (int iy = -square; iy <= square; iy++) {
				final int commonCount = 0;
				final Chunk chunk = world.getChunkFromBlockCoords((int) Math.floor(player.posX + ix * 16), (int) Math.floor(player.posZ + iy * 16));
				final int yMax = Utility.findHighestBlock(world, chunk);
				for (int x = 0; x < 16; x++)
					for (int z = 0; z < 16; z++)
						for (int y = yMax; y > 0; y--) {
							final int worldX = chunk.xPosition * 16 + x;
							final int worldZ = chunk.zPosition * 16 + z;
							if (world.isAirBlock(worldX, y, worldZ))
								continue;
							final Block block = chunk.getBlock(x, y, z);
							final int meta = world.getBlockMetadata(worldX, y, worldZ);
							if (!Utility.isCommonBlock(block, meta))
								world.setBlock(worldX, y, worldZ, Blocks.air);
						}
			}
		return thisStack;
	}
}
