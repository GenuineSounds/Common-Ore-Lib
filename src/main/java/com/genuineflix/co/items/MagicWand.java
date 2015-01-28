package com.genuineflix.co.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.genuineflix.co.CommonOre;
import com.genuineflix.co.registry.MetalRegistry;
import com.genuineflix.co.utils.Utility;

public class MagicWand extends ItemShears {

	public static MagicWand instance;

	public MagicWand() {
		setUnlocalizedName("magicWand");
		setTextureName(CommonOre.MODID + ":debug/Wand");
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
		if (world.isRemote)
			return stack;
		int commonTotal = 0;
		final int square = 1;
		for (int ix = -square; ix <= square; ix++)
			for (int iy = -square; iy <= square; iy++) {
				int commonCount = 0;
				final Chunk chunk = world.getChunkFromBlockCoords((int) Math.floor(player.posX + ix * 16), (int) Math.floor(player.posZ + iy * 16));
				final int yMax = Utility.findHighestBlock(world, chunk);
				for (int x = 0; x < 16; x++)
					for (int z = 0; z < 16; z++)
						for (int y = yMax; y > 0; y--)
							if (!MetalRegistry.instance.isCommon(chunk.getBlock(x, y, z)))
								world.setBlock(chunk.xPosition * 16 + x, y, chunk.zPosition * 16 + z, Blocks.air);
							else
								commonCount++;
				commonTotal += commonCount;
				System.out.println("Max Block Height: " + yMax + ", " + commonCount + " Common Ores found.");
			}
		return stack;
	}
}
