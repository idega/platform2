package is.idega.block.nationalregister.data;

import is.idega.block.nationalregister.business.NationalRegisterFileImportHandlerBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.idega.block.importer.data.GenericImportFile;
import com.idega.block.importer.data.ImportFile;

/**
 * @author gimmi
 */
public class NationalRegisterImportFileE36		extends	GenericImportFile implements ImportFile {

	public NationalRegisterImportFileE36() {
		super();
		setRecordDilimiter("\n");
	}
	
	public NationalRegisterImportFileE36(File file) {
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
		for (int i = 0; i < 27; i++) {
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
				case NationalRegisterFileImportHandlerBean.COLUMN_SYMBOL : return recordString.substring(0,2);
				case NationalRegisterFileImportHandlerBean.COLUMN_OLD_ID : return recordString.substring(2,10);
				case NationalRegisterFileImportHandlerBean.COLUMN_SSN : return recordString.substring(10,20);
				case NationalRegisterFileImportHandlerBean.COLUMN_FAMILY_ID : return recordString.substring(20,30);
				case NationalRegisterFileImportHandlerBean.COLUMN_COMMUNE : return recordString.substring(61,65); // Is actually 1 field that is 12 spaces long.
				case NationalRegisterFileImportHandlerBean.COLUMN_STREET : return recordString.substring(65,69);    // Used to be 3 fields (4/4/4), so I assume
				case NationalRegisterFileImportHandlerBean.COLUMN_BUILDING : return recordString.substring(69,73);  // that this is still the case
				case NationalRegisterFileImportHandlerBean.COLUMN_SEX : return recordString.substring(76,77);
				case NationalRegisterFileImportHandlerBean.COLUMN_MARITIAL_STATUS : return recordString.substring(77,78);
				case NationalRegisterFileImportHandlerBean.COLUMN_EMPTY : return recordString.substring(78,80);
				case NationalRegisterFileImportHandlerBean.COLUMN_NO_MAIL : return recordString.substring(80,81);
				case NationalRegisterFileImportHandlerBean.COLUMN_NATIONALITY : return recordString.substring(81,83);
				case NationalRegisterFileImportHandlerBean.COLUMN_PLACE_OF_BIRTH : return recordString.substring(83,87);
				case NationalRegisterFileImportHandlerBean.COLUMN_DATE_OF_BIRTH : return recordString.substring(87, 95);
				case NationalRegisterFileImportHandlerBean.COLUMN_NAME : return recordString.substring(95,126);
				case NationalRegisterFileImportHandlerBean.COLUMN_SPOUSE_SSN : return recordString.substring(126,136);
				case NationalRegisterFileImportHandlerBean.COLUMN_ADDRESS_CODE : return recordString.substring(136,148);
				case NationalRegisterFileImportHandlerBean.COLUMN_DATE_OF_MODIFICATION : return recordString.substring(151,157);
				case NationalRegisterFileImportHandlerBean.COLUMN_PLACEMENT_CODE : return recordString.substring(157,169);
				case NationalRegisterFileImportHandlerBean.COLUMN_DATE_OF_CREATION : return recordString.substring(172,178);
				case NationalRegisterFileImportHandlerBean.COLUMN_LAST_DOMESTIC_ADDRESS : return recordString.substring(178,190);
				case NationalRegisterFileImportHandlerBean.COLUMN_AGENT_SSN : return recordString.substring(190,200);
				case NationalRegisterFileImportHandlerBean.COLUMN_NEW : return recordString.substring(200, 201);
				case NationalRegisterFileImportHandlerBean.COLUMN_PO : return recordString.substring(204,207);
				case NationalRegisterFileImportHandlerBean.COLUMN_ADDRESS_NAME  : return recordString.substring(207,228);
				case NationalRegisterFileImportHandlerBean.COLUMN_ADDRESS : return recordString.substring(228,249);
				case NationalRegisterFileImportHandlerBean.COLUMN_STATUS : return recordString.substring(272, 276);
				//case NationalRegisterFileImportHandlerBean.COLUMN_PARISH : return recordString.substring(101,104);
				default : return null;
			}
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
}
