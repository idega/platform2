/*
 * $Id: SimpleReportBusinessBean.java,v 1.4 2004/10/15 13:58:15 sigtryggur Exp $
 * Created on 21.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.datareport.business;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.idega.block.datareport.util.ReportDescription;
import com.idega.block.datareport.util.ReportableField;
import com.idega.business.IBOServiceBean;
import com.idega.util.text.TextSoap;


/**
 * 
 *  Last modified: $Date: 2004/10/15 13:58:15 $ by $Author: sigtryggur $
 * 
 * @author <a href="mailto:gummi@idega.com">Gudmundur Agust Saemundsson</a>
 * @version $Revision: 1.4 $
 */
public class SimpleReportBusinessBean extends IBOServiceBean implements SimpleReportBusiness {

	/**
	 * 
	 */
	public SimpleReportBusinessBean() {
		super();
	}
	
	
	
	public void writeSimpleExcelFile(JRDataSource reportData, String nameOfReport, String filePathAndName, ReportDescription description) throws IOException{
		if(nameOfReport==null || "".equals(nameOfReport)){
			nameOfReport = "Report";
		}
		System.out.println(nameOfReport);
		System.out.println(TextSoap.encodeToValidExcelSheetName(nameOfReport));
		System.out.println(nameOfReport);
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = sheet = wb.createSheet(TextSoap.encodeToValidExcelSheetName(nameOfReport));
	    int rowIndex = 0;

	    
	    //-- Report Name --//
	    // Create a row and put some cells in it. Rows are 0 based.
	    HSSFRow row = sheet.createRow((short)rowIndex++);
	    // Create a cell and put a value in it.
	    HSSFCell cell = row.createCell((short)0);

	    // Create a new font and alter it.
	    HSSFFont font = wb.createFont();
	    font.setFontHeightInPoints((short)24);
	    font.setFontName("Courier New");
	    font.setItalic(true);
	    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

	    // Fonts are set into a style so create a new one to use.
	    HSSFCellStyle style = wb.createCellStyle();
	    style.setFont(font);

	    // Create a cell and put a value in it.
	    cell.setCellValue(nameOfReport);
	    cell.setCellStyle(style);
	    

	    //-- Report Parameters --//
	    rowIndex++;
	    HSSFRow row1 = null;
	    String parameterString = "";
	    List labels = description.getListOfHeaderParameterLabelKeys();
	    List parameters = description.getListOfHeaderParameterKeys();
	    Iterator labelIter = labels.iterator();
	    Iterator parameterIter = parameters.iterator();
	    boolean newLineForeEachParameter = description.doCreateNewLineForEachParameter();
	    while (labelIter.hasNext() && parameterIter.hasNext()) {
			String label = description.getParameterOrLabelName((String)labelIter.next());
			String parameter = description.getParameterOrLabelName((String)parameterIter.next());
			if(newLineForeEachParameter){
				row1 = sheet.createRow((short)rowIndex++);
				row1.createCell((short)0).setCellValue(label + " "+parameter);
			} else {
				parameterString += label + " "+parameter+"      ";
			}
		}
	    if(!newLineForeEachParameter){
		    row1 = sheet.createRow((short)rowIndex++);
		    row1.createCell((short)0).setCellValue(parameterString);
	    }
	    rowIndex++;
	    
	    //-- Report ColumnHeader --//
	    List fields = description.getListOfFields();
	    HSSFRow headerRow = sheet.createRow((short)rowIndex++);
	    
	 	HSSFCellStyle headerCellStyle = wb.createCellStyle();
	    
	 	headerCellStyle.setWrapText( true );
	 	headerCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	 	headerCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	 	
	    HSSFFont headerCellFont = wb.createFont();
	    //headerCellFont.setFontHeightInPoints((short)12);
	    headerCellFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	 	headerCellStyle.setFont(headerCellFont);
	 	
		int colIndex = 0;
		int columnWithUnit = 256; // the unit is 1/256 of a character
		int numberOfCharactersPerLineInLongTextFields = 60;
		int numberOfCharactersPerLineInRatherLongTextFields = 35;
		int numberOfCharactersPerLineInUndifinedTextFields = 20;
		
	    for (Iterator iter = fields.iterator(); iter.hasNext();colIndex++) {
			ReportableField field = (ReportableField) iter.next();
			HSSFCell headerCell = headerRow.createCell((short)colIndex);
			headerCell.setCellValue((String)description.getColumnName(field));
			headerCell.setCellStyle(headerCellStyle);
			
			//column width
			int fieldsMaxChar = field.getMaxNumberOfCharacters();
			int colWith = numberOfCharactersPerLineInRatherLongTextFields*columnWithUnit;  //default, can be rather long text
			if(fieldsMaxChar > 0 && fieldsMaxChar < numberOfCharactersPerLineInRatherLongTextFields){
				colWith = (fieldsMaxChar+1)*columnWithUnit;  // short fields
			} else if(fieldsMaxChar > 500){ // when the field is set to be able to contain very long text
				colWith = numberOfCharactersPerLineInLongTextFields*columnWithUnit; //can be very long text
			} else if(fieldsMaxChar < 0){
				colWith = numberOfCharactersPerLineInUndifinedTextFields*columnWithUnit;
			}
			sheet.setColumnWidth((short)colIndex,(short)colWith);
			
		}
	    
	    //-- Report ColumnDetail --//
	    try {
	    	 	HSSFCellStyle dataCellStyle = wb.createCellStyle();
	    	 	dataCellStyle.setWrapText( true );
	    	 	dataCellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
	    	 	sheet.createFreezePane( 0, rowIndex );
	    	 	
			while(reportData.next()){
				HSSFRow dataRow = sheet.createRow((short)rowIndex++);
				colIndex = 0;
				for (Iterator iter = fields.iterator(); iter.hasNext();colIndex++) {
					ReportableField field = (ReportableField) iter.next();
					HSSFCell dataCell = dataRow.createCell((short)colIndex);
					Object fieldValue = reportData.getFieldValue(field);
					if(fieldValue != null){
						dataCell.setCellValue(String.valueOf(fieldValue));
					}
					dataCell.setCellStyle(dataCellStyle);
				}
			}
		}
		catch (JRException e) {
			//-- Exception fetching data --//
			HSSFRow exceptionRow = sheet.createRow((short)rowIndex++);
		    HSSFCell exceptionCell = exceptionRow.createCell((short)0);

		    // Create a new font and alter it.
		    HSSFFont exceptionFont = wb.createFont();
		    exceptionFont.setFontName("Courier New");
		    exceptionFont.setItalic(true);

		    // Fonts are set into a style so create a new one to use.
		    HSSFCellStyle exceptionStyle = wb.createCellStyle();
		    exceptionStyle.setFont(exceptionFont);

		    // Create a cell and put a value in it.
		    exceptionCell.setCellValue("Error occurred while getting data. Check log for more details.");
		    exceptionCell.setCellStyle(exceptionStyle);
			
			e.printStackTrace();
		}
	    
	    
	    // Write the output to a file
	    FileOutputStream fileOut = new FileOutputStream(filePathAndName);
	    wb.write(fileOut);
	    fileOut.close();
	}
	
	
}
