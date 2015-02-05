package com.genuineflix.metal.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import com.genuineflix.metal.CommonOre;
import com.genuineflix.metal.util.Utility;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInterModComms;

public class MagicWand extends ItemShears {

	public static boolean ccIsLoaded() {
		return Loader.isModLoaded(CC_MOD_NAME);
	}

	public static final String CC_MOD_NAME = "ClosedCaption";
	public static final String CC_DIRECT_MESSAGE_KEY = "[Direct]";
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
		int commonCount = 0;
		final Chunk chunk = world.getChunkFromBlockCoords((int) Math.floor(player.posX), (int) Math.floor(player.posZ));
		final int yMax = Utility.findHighestBlock(world, chunk);
		for (int x = 0; x < 16; x++)
			for (int z = 0; z < 16; z++)
				for (int y = yMax; y > 0; y--) {
					final int worldX = chunk.xPosition * 16 + x;
					final int worldZ = chunk.zPosition * 16 + z;
					if (world.isAirBlock(worldX, y, worldZ))
						continue;
					if (Utility.isCommonBlock(chunk.getBlock(x, y, z), world.getBlockMetadata(worldX, y, worldZ)))
						commonCount++;
				}
		if (ccIsLoaded()) {
			final NBTTagCompound tag = new NBTTagCompound();
			tag.setString("type", "damage");
			tag.setFloat("amount", commonCount);
			tag.setString("message", "Common ores found: ");
			tag.setInteger("ticks", 120);
			FMLInterModComms.sendMessage(CC_MOD_NAME, CC_DIRECT_MESSAGE_KEY, tag);
		} else
			CommonOre.log.warn("Common Ore count: " + commonCount);
		return thisStack;
	}
}
