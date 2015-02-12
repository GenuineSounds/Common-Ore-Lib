package com.genuineflix.metal.util;

public class StringHelper {

	public static String camelCase(final String prefix, final String str) {
		return prefix.toLowerCase() + str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
	}

	public static String cleanName(String name) {
		final String old = name;
		name = name.replaceAll("^(.+?)\\.", "");
		for (final String string : StringHelper.fixes) {
			name = name.replaceAll("^" + string, "");
			name = name.replaceAll(string + "$", "");
		}
		return name.toLowerCase().replace("aluminum", "aluminium");
	}

	private static final String[] fixes = { "ore", "dust", "pulv(erized*)*", "block", "ingot", "nugget", "storage", "compress(ed)*" };
}
