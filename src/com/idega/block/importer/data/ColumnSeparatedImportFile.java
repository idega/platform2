package com.idega.block.importer.data;

import java.io.File;

/**
 * @author <a href="mailto:eiki@idega.is">Eirikur S. Hrafnsson</a>
 *
 */
public class ColumnSeparatedImportFile extends GenericImportFile {
	
	
	/**
	 * Constructor for ColumnSeparatedImportFile.
	 */
	public ColumnSeparatedImportFile() {
		super();
		this.setAddNewLineAfterRecord(false);
		this.setRecordDilimiter("\n");
		this.setValueSeparator(";");
	}

	/**
	 * Constructor for ColumnSeparatedImportFile.
	 * @param file
	 */
	public ColumnSeparatedImportFile(File file) {
		this();
		setFile(file);
	}



}
