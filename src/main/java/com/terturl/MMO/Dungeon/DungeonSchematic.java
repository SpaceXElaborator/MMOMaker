package com.terturl.MMO.Dungeon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;

import com.terturl.MMO.Dungeon.Exceptions.LengthExceedsException;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class DungeonSchematic {

	@Getter
	private int blockCount;
	
	@Getter @Setter
	private short width;
	
	@Getter @Setter
	private short height;
	
	@Getter @Setter
	private short length;
	
	@Getter @Setter
	private byte[] blockIds;
	
	@Getter @Setter
	private Map<Integer, String> palette = new HashMap<>();
	
	public DungeonSchematic(File f) {
		try {
			loadSchematic(f);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LengthExceedsException e) {
			e.printStackTrace();
		}
	}
	
	private void loadSchematic(File f) throws IOException, LengthExceedsException {
		InputStream stream = new FileInputStream(f);
		NBTTagCompound tagCompound = NBTCompressedStreamTools.a(stream);
		Bukkit.getConsoleSender().sendMessage(tagCompound.toString());
		if(tagCompound.getShort("Width") > 16) throw new LengthExceedsException((int)tagCompound.getShort("Width"));
		if(tagCompound.getShort("Length") > 16) throw new LengthExceedsException((int)tagCompound.getShort("Length"));
		
		setWidth(tagCompound.getShort("Width"));
		setHeight(tagCompound.getShort("Height"));
		setLength(tagCompound.getShort("Length"));
		setBlockIds(tagCompound.getByteArray("BlockData"));
		NBTTagCompound test = tagCompound.getCompound("Palette");
		Set<String> testKeys = test.getKeys();
		for(String s : testKeys) {
			palette.put(test.getInt(s), s);
		}
		stream.close();
	}
	
}