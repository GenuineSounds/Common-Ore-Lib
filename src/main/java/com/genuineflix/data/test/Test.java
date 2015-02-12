package com.genuineflix.data.test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.genuineflix.data.collections.DataCompound;
import com.genuineflix.data.helpers.IOHelper;
import com.genuineflix.data.helpers.JSONHelper;

public class Test {

	public static void main(final String[] args) {
		/**
		 *
		 */
		// Init
		final File mainFolder = new File("C:\\", "Main");
		mainFolder.mkdirs();
		final File file = new File(mainFolder, "main.json");
		// Populate
		DataCompound main1 = new DataCompound();
		main1.set("BigDecimal", new BigDecimal("3.14159265358979323846264338327950288419716939937510582"));
		main1.set("BigInteger", new BigInteger("2345984321896432168945165165169849913216843216897431024"));
		main1.set("boolean", true);
		main1.set("byte", (byte) 36);
		main1.set("byte[]", new byte[] { (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE });
		main1.set("DataCompound", new DataCompound());
		main1.set("float", 8.5F);
		main1.set("double", 1234.5678);
		main1.set("int[]", new int[] { 0x6E745B5D, 4, 0, 89758, });
		main1.set("int", 28941321);
		main1.set("long", 0xCCCCCCCCCCCCCCCCL);
		main1.set("short", Short.MAX_VALUE);
		main1.set("String", "Test \"stuff\"");
		// Display Main 1
		// System.out.println(main1);
		try {
			System.out.println(bytesToHex(IOHelper.encodeToBytes(main1)));
		}
		catch (final IOException e) {}
		// Save Main1
		JSONHelper.saveDataToJSON(main1, file);
		// Load Main2
		DataCompound main2 = JSONHelper.loadFromJSON(file);
		// Display Main2
		// System.out.println(main2);
		try {
			System.out.println(bytesToHex(IOHelper.encodeToBytes(main2)));
		}
		catch (final IOException e) {}
		/**
		 *
		 */
		main1 = new DataCompound();
		main1.set("11", (long) 0);
		main1.set("22", 0);
		main1.set("33", (short) 0);
		// Display Main 1
		// System.out.println(main1);
		try {
			System.out.println(bytesToHex(IOHelper.encodeToBytes(main1)));
			System.out.println(bytesToHex(IOHelper.compressToBytes(main1)));
		}
		catch (final IOException e) {}
		// Save Main1
		JSONHelper.saveDataToJSON(main1, file);
		// Load Main2
		main2 = JSONHelper.loadFromJSON(file);
		// Display Main2
		// System.out.println(main2);
		try {
			System.out.println(bytesToHex(IOHelper.encodeToBytes(main2)));
			System.out.println(bytesToHex(IOHelper.compressToBytes(main2)));
		}
		catch (final IOException e) {}
		/**
		 *
		 */
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String bytesToHex(final byte[] bytes) {
		final char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			final int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
}
