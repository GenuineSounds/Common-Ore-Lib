package com.genuineflix.data.helpers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;

import com.genuineflix.data.AbstractData;
import com.genuineflix.data.SizeLimit;
import com.genuineflix.data.collections.DataCompound;
import com.genuineflix.data.primitives.DataNull;

public class IOHelper {

	public static DataCompound readCompressed(final InputStream stream) throws IOException {
		final DataInputStream compressedInput = new DataInputStream(new BufferedInputStream(new GZIPInputStream(stream)));
		DataCompound data;
		try {
			data = readStream(compressedInput, SizeLimit.NONE);
		}
		finally {
			compressedInput.close();
		}
		return data;
	}

	public static void writeCompressed(final DataCompound compound, final OutputStream stream) throws IOException {
		final DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(stream)));
		try {
			writeToOutput(compound, output);
		}
		finally {
			output.close();
		}
	}

	public static DataCompound readCompressedBytes(final byte[] bs, final SizeLimit limit) throws IOException {
		final DataInputStream compressedInput = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bs))));
		DataCompound compound;
		try {
			compound = readStream(compressedInput, limit);
		}
		finally {
			compressedInput.close();
		}
		return compound;
	}

	public static DataCompound readEncodedBytes(final byte[] bs, final SizeLimit limit) throws IOException {
		final DataInputStream compressedInput = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bs))));
		DataCompound compound;
		try {
			compound = readStream(compressedInput, limit);
		}
		finally {
			compressedInput.close();
		}
		return compound;
	}

	public static byte[] compressToBytes(final DataCompound compound) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream compressedOutput = new DataOutputStream(new GZIPOutputStream(baos));
		try {
			writeToOutput(compound, compressedOutput);
		}
		finally {
			compressedOutput.close();
		}
		return baos.toByteArray();
	}

	public static byte[] encodeToBytes(final DataCompound compound) throws IOException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DataOutputStream compressedOutput = new DataOutputStream(baos);
		try {
			writeToOutput(compound, compressedOutput);
		}
		finally {
			compressedOutput.close();
		}
		return baos.toByteArray();
	}

	public static void saveToFileSafe(final DataCompound compound, final File file) {
		try {
			final File tmp = new File(file.getAbsolutePath() + "_tmp");
			if (tmp.exists())
				tmp.delete();
			saveToFile(compound, tmp);
			if (file.exists())
				file.delete();
			if (file.exists())
				throw new IOException("Failed to delete " + file);
			else
				tmp.renameTo(file);
		}
		catch (final Exception e) {}
	}

	public static DataCompound readStream(final DataInputStream stream) throws IOException {
		return readStream(stream, SizeLimit.NONE);
	}

	public static DataCompound readStream(final DataInput input, final SizeLimit limit) throws IOException {
		final AbstractData nbtbase = getData(input, 0, limit);
		if (nbtbase instanceof DataCompound)
			return (DataCompound) nbtbase;
		else
			throw new IOException("Root byte must be DataCompound byte");
	}

	private static void writeToOutput(final AbstractData data, final DataOutput output) throws IOException {
		output.writeByte(data.getTypeByte());
		if (data.getTypeByte() != 0) {
			output.writeUTF("");
			data.write(output);
		}
	}

	private static AbstractData getData(final DataInput input, final int depth, final SizeLimit limit) throws IOException {
		final byte type = input.readByte();
		if (type == 0)
			return DataNull.INSTANCE;
		else {
			input.readUTF();
			final AbstractData nbtbase = AbstractData.create(type);
			try {
				nbtbase.read(input, depth, limit);
				return nbtbase;
			}
			catch (final IOException e) {
				final CrashReport report = CrashReport.makeCrashReport(e, "Loading GDF data");
				final CrashReportCategory category = report.makeCategory("GDF Byte");
				category.addCrashSection("Byte name", AbstractData.TYPES[type].getTypeName());
				category.addCrashSection("Byte type", Byte.valueOf(type));
				throw new ReportedException(report);
			}
		}
	}

	public static void saveToFile(final DataCompound compound, final File file) throws IOException {
		final DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
		try {
			writeToOutput(compound, stream);
		}
		finally {
			stream.close();
		}
	}

	public static DataCompound open(final File file) throws IOException {
		return open(file, SizeLimit.NONE);
	}

	public static DataCompound open(final File file, final SizeLimit limit) throws IOException {
		if (!file.exists())
			return null;
		else {
			final DataInputStream input = new DataInputStream(new FileInputStream(file));
			DataCompound compound;
			try {
				compound = readStream(input, limit);
			}
			finally {
				input.close();
			}
			return compound;
		}
	}
}