/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.experimental.poitest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This class does something very clever.....
 * 
 * @author palli
 * @version 1.0
 */
public class POITest {
	public static void main(String[] args) {
    HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet sheet = wb.createSheet("new sheet");

    HSSFRow row = sheet.createRow((short)0);
		sheet.setRowSumsBelow(true);
    row.createCell((short)0).setCellValue("Virkur");
    row.createCell((short)1).setCellValue("Nafn");
    row.createCell((short)2).setCellValue("Heimili");
    row.createCell((short)3).setCellValue("Kennitala");
    row.createCell((short)4).setCellValue("Skuldar");

    row = sheet.createRow((short)1);
    row.createCell((short)0).setCellValue(true);
    row.createCell((short)1).setCellValue("Páll Helgason");
    row.createCell((short)2).setCellValue("Galtalind 13");
    row.createCell((short)3).setCellValue("0610703899");
    row.createCell((short)4).setCellValue(100);

    row = sheet.createRow((short)2);
    row.createCell((short)0).setCellValue(1.2);
    row.createCell((short)1).setCellValue("Þórhallur Helgason");
    row.createCell((short)2).setCellValue("Stafnasel 5");
    row.createCell((short)3).setCellValue("0202774919");
    row.createCell((short)4).setCellValue(200);

    row = sheet.createRow((short)3);
    row.createCell((short)0).setCellValue(false);
    row.createCell((short)1).setCellValue("Karitas Gunnarsdóttir");
    row.createCell((short)2).setCellValue("Galtalind 13");
    row.createCell((short)3).setCellValue("1409743589");
    row.createCell((short)4).setCellValue(300);

    row = sheet.createRow((short)4);
    HSSFCell cell = row.createCell((short)4);
//    cell.setCellValue("=sum(E2:E4)");
   
    // Write the output to a file
    FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("c:\\output\\workbook.xls");
			wb.write(fileOut);
	    fileOut.close();
		}
		catch (FileNotFoundException e) {
		}
		catch (IOException e) {
		}
  }
}
