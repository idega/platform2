package is.idega.block.nationalregister.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.idega.block.importer.data.GenericImportFile;
import com.idega.block.importer.data.ImportFile;

/**
 * @author palli
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class NationalRegisterImportFile extends GenericImportFile implements ImportFile {
	public NationalRegisterImportFile() {
		super();
		setRecordDilimiter("\n");
	}
	
	public NationalRegisterImportFile(File file) {
		this();
		setFile(file);
	}
	
	/**
	 * @see com.idega.block.importer.data.ImportFile#getRecords()
	 */
	public Collection getRecords() {
		return super.getRecords();
	}
	
	/**
	 * @see com.idega.block.importer.data.ImportFile#getValuesFromRecordString(java.lang.String)
	 */
	public ArrayList getValuesFromRecordString(String recordString) {
		ArrayList values = new ArrayList();
		for (int i = 0; i < 20; i++) {
			String value = getValueAtIndexFromRecordString(i,recordString);
			if (value != null)
				values.add(value);
			else
				values.add("");
		}
		
		return values;
	}
		
	/* (non-Javadoc)
	 * @see com.idega.block.importer.data.ImportFile#getValueAtIndexFromRecordString(int, java.lang.String)
	 */
	public String getValueAtIndexFromRecordString(int index, String recordString) {
		try {
			switch(index) {
				case 0 : return recordString.substring(0,2);
				case 1 : return recordString.substring(2,10);
				case 2 : return recordString.substring(10,20);
				case 3 : return recordString.substring(20,30);
				case 4 : return recordString.substring(30,61);
				case 5 : return recordString.substring(61,65);
				case 6 : return recordString.substring(65,69);
				case 7 : return recordString.substring(69,73);
				case 8 : return recordString.substring(73,76);
				case 9 : return recordString.substring(76,77);
				case 10 : return recordString.substring(77,78);
				case 11 : return recordString.substring(78,80);
				case 12 : return recordString.substring(80,81);
				case 13 : return recordString.substring(81,83);
				case 14 : return recordString.substring(83,87);
				case 15 : return recordString.substring(87,97);
				case 16 : return recordString.substring(97,101);
				case 17 : return recordString.substring(101,104);
				case 18 : return recordString.substring(104,107);
				case 19 : return recordString.substring(107,128);
				default : return null;
			}
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
}