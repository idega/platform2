/*
 * $Id: NationalRegisterImportFileDeceased.java,v 1.1.2.1 2006/11/21 23:35:25 idegaweb Exp $
 * Created on Nov 20, 2006
 *
 * Copyright (C) 2006 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.nationalregister.data;

import is.idega.block.nationalregister.business.NationalRegisterDeceasedFileImportHandlerBean;
import java.util.ArrayList;
import com.idega.block.importer.data.GenericImportFile;
import com.idega.block.importer.data.ImportFile;


public class NationalRegisterImportFileDeceased extends GenericImportFile implements ImportFile {

	/**
	 * @see com.idega.block.importer.data.ImportFile#getValuesFromRecordString(java.lang.String)
	 */
	public ArrayList getValuesFromRecordString(String recordString) {
		ArrayList values = new ArrayList();
		for (int i = 0; i < 9; i++) {
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
				case NationalRegisterDeceasedFileImportHandlerBean.COLUMN_SYMBOL : return recordString.substring(0,2);
				case NationalRegisterDeceasedFileImportHandlerBean.COLUMN_SSN : return recordString.substring(2,12);
				case NationalRegisterDeceasedFileImportHandlerBean.COLUMN_DATE_OF_DEATH : return recordString.substring(12, 20);
				case NationalRegisterDeceasedFileImportHandlerBean.COLUMN_NAME : return recordString.substring(20,51);
				case NationalRegisterDeceasedFileImportHandlerBean.COLUMN_STREET : return recordString.substring(51,72);
				case NationalRegisterDeceasedFileImportHandlerBean.COLUMN_COMMUNE : return recordString.substring(72,93);
				case NationalRegisterDeceasedFileImportHandlerBean.COLUMN_GENDER : return recordString.substring(93,94);
				case NationalRegisterDeceasedFileImportHandlerBean.COLUMN_MARITIAL_STATUS : return recordString.substring(94,95);
				case NationalRegisterDeceasedFileImportHandlerBean.COLUMN_SPOUSE_SSN : return recordString.substring(95,105);
				default : return null;
			}
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}

	public String getEncoding() {
		return "ISO-8859-1";
	}
}
