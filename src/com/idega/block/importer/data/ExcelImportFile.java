package com.idega.block.importer.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.idega.block.importer.business.NoRecordsException;
import com.idega.util.Timer;
import com.idega.util.text.TextSoap;

public class ExcelImportFile extends GenericImportFile {

	private Iterator iter;

	public Object getNextRecord() {
		if (iter == null) {
			Collection records = getAllRecords();
			if (records != null) {
				iter = records.iterator();
			}
		}

		if (iter != null) {
			while (iter.hasNext()) {
				return iter.next();
			}
		}

		return "";
	}

	/**
	 * Method getValuesFromRecordString. Uses the valueSeparator and a stringtokenizer to read the record and create an ArrayList of values.
	 * @param recordString
	 * @return An ArrayList of values or null is no value was found
	 */
	public ArrayList getValuesFromRecordString(String recordString){
		ArrayList values = new ArrayList();
		if (recordString.startsWith(this.valueSeparator)) {
			recordString = " " + recordString;
		}
		recordString = TextSoap.findAndReplace(recordString,this.valueSeparator+this.valueSeparator,this.valueSeparator+this.emptyValueString+this.valueSeparator);
		recordString = TextSoap.findAndReplace(recordString,this.valueSeparator+this.valueSeparator,this.valueSeparator+this.emptyValueString+this.valueSeparator);
		StringTokenizer tokens = new StringTokenizer(recordString,this.valueSeparator);
		String value = null;
		while( tokens.hasMoreTokens() ){
			value = tokens.nextToken();
			values.add(value);	
		}
				
		return values;
	} 

	
	public Collection getAllRecords() throws NoRecordsException {
		//System.out.println("Entering getAllRecords");
		try {
			FileInputStream input = new FileInputStream(getFile());
			HSSFWorkbook wb = new HSSFWorkbook(input);
			HSSFSheet sheet = wb.getSheetAt(0);

			int cnt = 0;
			int records = 0;

			Timer clock = new Timer();
			clock.start();

			StringBuffer buffer = new StringBuffer();
			ArrayList list = new ArrayList();
			//System.out.println("number of rows = " + sheet.getLastRowNum());
			for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
				HSSFRow row = sheet.getRow(i);
				if (buffer == null) {
					buffer = new StringBuffer();
				}

				if (row != null) {
					for (short j = 0; j < row
							.getLastCellNum(); j++) {
						//System.out.println("number of columns = " + row.getLastCellNum());						
						HSSFCell cell = row.getCell(j);
						if (cell != null) {
							if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
								String value = cell.getStringCellValue();
								//System.out.println("str.value = " + value);
								buffer.append(value);
							} else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
								NumberFormat f = DecimalFormat.getInstance();
								f.setMaximumFractionDigits(2);
								f.setMinimumFractionDigits(0);
								f.setGroupingUsed(false);
								String value = f.format(cell.getNumericCellValue());
								//System.out.println("num.value = " + value);
								buffer.append(value);
							} else {
								String value = cell.getStringCellValue();
								//System.out.println("other.value = " + value);
								buffer.append(value);
							}
						} else {
							buffer.append(this.getEmptyValueString());
						}
							
						buffer.append(getValueSeparator());							
					}

					records++;
					if ((records % 1000) == 0) {
						System.out
								.println("Importer: Reading record nr.: "
										+ records + " from file "
										+ getFile().getName());
					}

					list.add(buffer.toString());
					buffer = null;
					cnt++;
				}
			}

			if (records == 0) {
				throw new NoRecordsException(
						"No records where found in the selected file"
								+ getFile().getAbsolutePath());
			}

			return list;
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			return null;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}