package com.idega.block.importer.data;

import java.io.File;

/**
 * @author <a href="mailto:joakim@idega.is">Joakim Johnson</a>
 * To add this to the "File Type" dropdown for the import function, execute the following SQL:
 * insert into im_file_class values(4, 'Tab separatede file', 'com.idega.block.importer.data.TabSeparatedImportFile', 'A tab separated file read. By default each record is separated with a new line character and each value is separated by a tab, but it can be adjusted by properties.')
 * 
 * Note that the "4" value in the SQL might have to be adjusted in the sql, 
 * depending on the number of records already inserted in the table. 
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
