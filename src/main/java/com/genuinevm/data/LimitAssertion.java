package com.genuinevm.data;

import com.genuinevm.data.primitive.DataByte;

public class LimitAssertion {

	public static final LimitAssertion NONE = new LimitAssertion(0L) {

		@Override
		public void assertLimit(final long size) {}
	};
	private final long byteMax;
	private long bytesAllocated;

	public LimitAssertion(final long sizeLimit) {
		byteMax = sizeLimit;
	}

	public void assertLimit(final long bytes) {
		bytesAllocated += bytes / DataByte.SIZE;
		if (bytesAllocated > byteMax)
			throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + bytesAllocated + " bytes where max allowed: " + byteMax);
	}
}