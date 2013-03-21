package com.idega.idegaweb.presentation;

import is.idega.idegaweb.campus.block.allocation.business.ContractService;
import is.idega.idegaweb.campus.block.allocation.data.Contract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.FinderException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.idega.business.IBOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

public class ExportContracts extends Block {
	protected static final String SUBMIT = "export_contracts";

	public void main(IWContext iwc) {
		control(iwc);
	}

	protected void displayForm() {
		Form form = new Form();
		SubmitButton button = new SubmitButton(SUBMIT,"Export contracts");
		form.add(button);
		add(form);	
	}

	protected void control(IWContext iwc) {
		if (iwc.isParameterSet(SUBMIT)) {
			exportContracts(iwc);
		}
		
		displayForm();			
	}
	
	private void exportContracts(IWContext iwc) {
		try {
			Collection contracts = getContractService(iwc).getContractHome().findAll();
			if (contracts != null && !contracts.isEmpty()) {
				FileOutputStream out = new FileOutputStream(new File("leigusamningar.xls"));
				HSSFWorkbook wb = new HSSFWorkbook();
				HSSFSheet sheet = wb.createSheet("Leigusamningar");

				short rowNumber = 0;
				short colNumber = 0;
				HSSFRow row = sheet.createRow(rowNumber++);

				//Header
				HSSFCell cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("kennitala");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("dagsetning");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("stada");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("fra");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("til");
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
				cell.setCellValue("prent");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("afhent");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("skilad");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("losnar");
				cell = row.createCell(colNumber++);
				cell.setCellStyle(getStyleBold(wb));
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue("umsokn_id");
				
				Iterator it = contracts.iterator();
				while (it.hasNext()) {
					colNumber = 0;
					Contract contract = (Contract) it.next();

					row = sheet.createRow(rowNumber++);
					
					cell = row.createCell(colNumber++);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(contract.getUser().getPersonalID());

					cell = row.createCell(colNumber++);
					cell.setCellValue(formatDate(contract.getStatusDate()));

					cell = row.createCell(colNumber++);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(contract.getStatus());

					cell = row.createCell(colNumber++);
					cell.setCellValue(formatDate(contract.getValidFrom()));

					cell = row.createCell(colNumber++);
					cell.setCellValue(formatDate(contract.getValidTo()));

					cell = row.createCell(colNumber++);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(contract.getApartment().getFloor().getBuilding().getComplex().getName());

					cell = row.createCell(colNumber++);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(contract.getApartment().getFloor().getBuilding().getName());

					cell = row.createCell(colNumber++);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(contract.getApartment().getName());

					cell = row.createCell(colNumber++);
					cell.setCellValue("");

					cell = row.createCell(colNumber++);
					cell.setCellValue(formatDate(contract.getDeliverTime()));

					cell = row.createCell(colNumber++);
					cell.setCellValue(formatDate(contract.getReturnTime()));

					cell = row.createCell(colNumber++);
					cell.setCellValue(formatDate(contract.getMovingDate()));
					
					cell = row.createCell(colNumber++);
					cell.setCellValue(contract.getApplicationID());

				}

				wb.write(out);
				out.close();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		
		IWTimestamp stamp = new IWTimestamp(date);
		return stamp.getDateString("yyyy-MM-dd HH:mm:ss");
	}
	
	protected ContractService getContractService(IWContext iwc)
			throws RemoteException {
		return (ContractService) IBOLookup.getServiceInstance(iwc
				.getApplicationContext(), ContractService.class);
	}
	
	private static HSSFCellStyle getStyleBold(HSSFWorkbook wb) {
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle styleBold = wb.createCellStyle();
		styleBold.setFont(font);

		return styleBold;
	}
}