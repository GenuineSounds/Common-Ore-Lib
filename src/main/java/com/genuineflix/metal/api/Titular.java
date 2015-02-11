package com.genuineflix.metal.api;

import java.util.Map.Entry;

public interface Titular<T> extends Entry<String, T>, Comparable<String> {

	@Override
	int compareTo(String key);

	int compareToValue(T t);

	@Override
	boolean equals(Object obj);

	@Override
	String getKey();

	@Override
	T getValue();

	boolean nameEquals(String str);

	@Override
	T setValue(T value);
}
