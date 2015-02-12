package com.genuineflix.data;

import com.genuineflix.data.primitives.DataByte;

public class SizeLimit {

	public static final SizeLimit NONE = new SizeLimit(0L) {

		@Override
		public void assertLimit(final long size) {}
	};
	private final long byteMax;
	private long bytesAllocated;

	public SizeLimit(final long sizeLimit) {
		byteMax = sizeLimit;
	}

	public void assertLimit(final long bytes) {
		bytesAllocated += bytes / DataByte.SIZE;
		if (bytesAllocated > byteMax)
			throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + bytesAllocated + " bytes where max allowed: " + byteMax);
	}
}