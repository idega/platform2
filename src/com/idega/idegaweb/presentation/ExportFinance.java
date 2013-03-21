package com.idega.idegaweb.presentation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

public class ExportFinance extends Block {
	protected static final String SUBMIT = "export_finance";

	public void main(IWContext iwc) {
		control(iwc);
	}

	protected void displayForm() {
		Form form = new Form();
		SubmitButton button = new SubmitButton(SUBMIT, "Export finance");
		form.add(button);
		add(form);
	}

	protected void control(IWContext iwc) {
		if (iwc.isParameterSet(SUBMIT)) {
			try {
				FileOutputStream out = new FileOutputStream(new File(
						"reikningar.xls"));
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet("Reikningar");

				short rowNumber = 0;
				short colNumber = 0;
				HSSFRow row = sheet.createRow(rowNumber++);

				// Header
				HSSFCell cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("kennitala");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("gardur");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("hus");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("ibud");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("gjald");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("lykill");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("dags");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("dagstil");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("magn");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("everd");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("upph");

				wb.write(out);
				out.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		displayForm();
	}

	private static HSSFCellStyle getStyleBold(HSSFWorkbook wb) {
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle styleBold = wb.createCellStyle();
		styleBold.setFont(font);

		return styleBold;
	}
}