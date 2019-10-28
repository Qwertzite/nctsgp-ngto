package qwertzite.ngtoplugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import jp.ngt.ngtlib.block.NGTObject;
import qwertzite.mctsg.ModLog;
import qwertzite.mctsg.api.IFormatEntry;

public class NgtoFormat implements IFormatEntry<NgtoBuilding>{
	public static final String NGTO_SUFFIX = ".ngto";
	
	@Override
	public boolean checkSuffix(File file) {
		return file.toString().endsWith(NGTO_SUFFIX);
	}

	@Override
	public NgtoBuilding loadFromFile(File file) {
		try {
			NGTObject obj = NGTObject.load(Files.newInputStream(file.toPath()));
			return new NgtoBuilding(obj);
		} catch (IOException e) {
			ModLog.warn("Caught an exception while loading building.", e);
		}
		return null;
	}
}
