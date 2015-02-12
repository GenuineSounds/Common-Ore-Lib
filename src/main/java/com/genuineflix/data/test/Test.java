package com.genuineflix.data.test;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.genuineflix.data.collections.DataCompound;
import com.genuineflix.data.helpers.JSONHelper;

public class Test {

	public static void main(final String[] args) {
		// Init
		final File mainFolder = new File("C:\\", "Main");
		mainFolder.mkdirs();
		final File file = new File(mainFolder, "main.json");
		// Populate
		final DataCompound main1 = new DataCompound();
		main1.setBigDecimal("BigDecimal", new BigDecimal("1234.567000000000000000000000000000000000000000008"));
		main1.setBigInteger("BigInteger", new BigInteger("2345000000000000000000123412341234123412341234"));
		main1.setBoolean("Boolean", true);
		main1.setByte("Byte", (byte) 36);
		main1.setByteArray("ByteArray", new byte[] {
				(byte) 3, (byte) 5, (byte) 7, (byte) 9
		});
		main1.setData("DataCompound", new DataCompound());
		main1.setFloat("Float", 8.5F);
		main1.setDouble("Double", 1234.5678);
		main1.setIntArray("IntArray", new int[] {
				2, 4, 6, 8,
		});
		main1.setInteger("Integer", -1);
		main1.setLong("Long", -1L);
		main1.setShort("Short", Short.MAX_VALUE);
		main1.setString("String", "Test \"stuff\"");
		// Display Main 1
		System.out.println(main1);
		// Save Main1
		JSONHelper.saveDataToJSON(main1, file);
		// Load Main2
		final DataCompound main2 = JSONHelper.loadFromJSON(file);
		System.out.println(main2);
	}
}
