package com.idega.block.importer.data;

import java.io.File;

/**
 * @author <a href="mailto:joakim@idega.is">Joakim Johnson</a>
 *
 */
public class TabSeparatedImportFile extends GenericImportFile {
	
	
	/**
	 * Constructor for TabSeparatedImportFile.
	 */
	public TabSeparatedImportFile() {
		super();
		this.setAddNewLineAfterRecord(false);
		this.setRecordDilimiter("\n");
		this.setValueSeparator("\t");
	}

	/**
	 * Constructor for TabSeparatedImportFile.
	 * @param file
	 */
	public TabSeparatedImportFile(File file) {
		this();
		setFile(file);
	}

}
