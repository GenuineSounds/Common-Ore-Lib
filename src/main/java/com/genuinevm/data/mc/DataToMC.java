package com.genuinevm.data.mc;

public class DataToMC {
	/*
	public static ItemStack createItemStack(final DataCompound compound) {
		return ItemStack.loadItemStackFromNBT(create(compound));
	}

	private static NBTTagEnd create(final DataNull nbt) {
		return new NBTTagEnd();
	}

	public static NBTTagByte create(final DataBoolean nbt) {
		return new NBTTagByte(nbt.toByte());
	}

	public static NBTTagByte create(final DataByte nbt) {
		return new NBTTagByte(nbt.value());
	}

	public static NBTTagShort create(final DataShort nbt) {
		return new NBTTagShort(nbt.value());
	}

	public static NBTTagInt create(final DataInteger nbt) {
		return new NBTTagInt(nbt.value());
	}

	public static NBTTagFloat create(final DataFloat nbt) {
		return new NBTTagFloat(nbt.value());
	}

	public static NBTTagDouble create(final DataDouble nbt) {
		return new NBTTagDouble(nbt.value());
	}

	public static NBTTagLong create(final DataLong nbt) {
		return new NBTTagLong(nbt.value());
	}

	public static NBTTagString create(final DataString nbt) {
		return new NBTTagString(nbt.value());
	}

	public static NBTTagIntArray create(final DataIntegerArray nbt) {
		return new NBTTagIntArray(nbt.value());
	}

	public static NBTTagByteArray create(final DataByteArray nbt) {
		return new NBTTagByteArray(nbt.value());
	}

	public static NBTTagList create(final DataList nbt) {
		final NBTTagList list = new NBTTagList();
		final int count = nbt.size();
		switch (nbt.getListType()) {
			case 6:
				for (int i = 0; i < count; i++)
					list.appendTag(new NBTTagDouble(nbt.getDouble(i)));
				break;
			case 5:
				for (int i = 0; i < count; i++)
					list.appendTag(new NBTTagFloat(nbt.getFloat(i)));
				break;
			case 8:
				for (int i = 0; i < count; i++)
					list.appendTag(new NBTTagString(nbt.getString(i)));
				break;
			case 10:
				for (int i = 0; i < count; i++)
					list.appendTag(create(nbt.getCompound(i)));
				break;
			case 11:
				for (int i = 0; i < count; i++)
					list.appendTag(new NBTTagIntArray(nbt.getIntegerArray(i)));
				break;
		}
		return list;
	}

	public static NBTTagCompound create(final DataCompound nbt) {
		final NBTTagCompound compound = new NBTTagCompound();
		for (final Entry<String, AbstractData> key : nbt.value().entrySet())
			try {
				compound.setTag(key.getKey(), create(key.getValue()));
			}
			catch (Exception e) {}
		return compound;
	}

	public static NBTBase create(final AbstractData<?> nbt) throws Exception {
		if (nbt instanceof DataNull)
			return create((DataNull) nbt);
		if (nbt instanceof DataBoolean)
			return create((DataBoolean) nbt);
		if (nbt instanceof DataByte)
			return create((DataByte) nbt);
		if (nbt instanceof DataShort)
			return create((DataShort) nbt);
		if (nbt instanceof DataInteger)
			return create((DataInteger) nbt);
		if (nbt instanceof DataFloat)
			return create((DataFloat) nbt);
		if (nbt instanceof DataDouble)
			return create((DataDouble) nbt);
		if (nbt instanceof DataLong)
			return create((DataLong) nbt);
		if (nbt instanceof DataString)
			return create((DataString) nbt);
		if (nbt instanceof DataByteArray)
			return create((DataByteArray) nbt);
		if (nbt instanceof DataIntegerArray)
			return create((DataIntegerArray) nbt);
		if (nbt instanceof DataList)
			return create((DataList) nbt);
		if (nbt instanceof DataCompound)
			return create((DataCompound) nbt);
		throw new Exception("No existing translation strategy was found for " + nbt.getClass().getSimpleName());
	}
	*/
}
