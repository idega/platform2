package com.idega.idegaweb.presentation;

import is.idega.idegaweb.campus.block.application.business.ApplicationService;
import is.idega.idegaweb.campus.block.application.data.Applied;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.block.application.data.CurrentResidency;
import is.idega.idegaweb.campus.block.application.data.RejectionHistory;
import is.idega.idegaweb.campus.block.application.data.RejectionHistoryHome;
import is.idega.idegaweb.campus.block.application.data.WaitingList;

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

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.application.data.Status;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.util.IWTimestamp;

public class ExportApplications extends Block {
	protected static final String SUBMIT = "export_applications";

	public void main(IWContext iwc) {
		control(iwc);
	}

	protected void displayForm() {
		Form form = new Form();
		SubmitButton button = new SubmitButton(SUBMIT, "Export applications");
		form.add(button);
		add(form);
	}

	protected void control(IWContext iwc) {
		if (iwc.isParameterSet(SUBMIT)) {
			try {
				Collection applications = getApplicationService(iwc)
						.getApplicationHome().findAll();
				if (applications != null && !applications.isEmpty()) {
					FileOutputStream out = new FileOutputStream(new File(
							"umsoknir.xls"));
					HSSFWorkbook wb = new HSSFWorkbook();
					HSSFSheet sheet = wb.createSheet("Umsoknir");

					short rowNumber = 0;
					short colNumber = 0;
					HSSFRow row = sheet.createRow(rowNumber++);

					// Header
					HSSFCell cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("id");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("kennitala");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("nafn");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("heimili");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("pnr");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("netfang");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("adsetur");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("pnradseturs");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("simi");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("gsm");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("stada");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("umsoknbarst");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("umsoknsamthykkt");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("umsoknsidaststadfest");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("skoli");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("deild");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("braut");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("nuverandihusnaedi");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("leiga");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("erlendurstudent");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("artalupphaf");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("manupphaf");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("artallok");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("manlok");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("athugsemd");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("tegundibudar");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("tegundumsoknar");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("oskastfraogmed");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("fyrstaval");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("annadval");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("thridjaval");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("forgangur");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("uthlutunsamthykt");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("hversuofthafnad");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("sidasthafnad");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("gardurhafnad");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("hushafnad");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("ibudhafnad");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("garduruthlutad");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("husuthlutad");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("ibuduthlutad");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("makikennitala");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("makinafn");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("makiskoli");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("makibraut");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("makiartalhuphaf");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("makimanupphaf");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("makiartallok");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("makimanlok");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("barn");
					cell = row.createCell(colNumber++);
					cell.setCellStyle(getStyleBold(wb));
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue("barnfd");

					Iterator it = applications.iterator();
					while (it.hasNext()) {
						System.out.println("rowNumber = " + rowNumber);
						colNumber = 0;
						Application application = (Application) it.next();
						CampusApplication camApp = getApplicationService(iwc)
								.getCampusApplicationHome()
								.findByApplicationId(
										((Integer) application.getPrimaryKey())
												.intValue());
						Applicant applicant = application.getApplicant();
						Collection applied = getApplicationService(iwc)
								.getAppliedHome().findByApplicationID(
										(Integer) camApp.getPrimaryKey());
						Collection waiting = getApplicationService(iwc)
								.getWaitingListHome().findByApplication(
										application);

						row = sheet.createRow(rowNumber++);

						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(((Integer) application
								.getPrimaryKey()).intValue());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(applicant.getSSN());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(applicant.getFullName());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(applicant.getLegalResidence());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(applicant.getPO());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(camApp.getEmail());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(applicant.getResidence());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue("");
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(applicant.getResidencePhone());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(applicant.getMobilePhone());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(application.getStatus());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(formatDate(application.getSubmitted()));
						if (application.getStatus().equals(
								Status.APPROVED.toString())) {
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(formatDate(application
									.getStatusChanged()));
						} else {
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
						}
						if (application.getStatus().equals(
								Status.APPROVED.toString())) {
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(getLastConfirmationDate(waiting)); // find
																					// the
																					// right
																					// entry
																					// to
																					// insert
																					// the
																					// confirmation
																					// date
						} else {
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (camApp.getSchool() != null) {
							cell.setCellValue(camApp.getSchool().getName());
						} else {
							cell.setCellValue("");							
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(camApp.getFaculty());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(camApp.getStudyTrack());
						if (camApp.getCurrentResidenceId() != null
								&& camApp.getCurrentResidenceId().intValue() > 0) {
							CurrentResidency currRes = getApplicationService(
									iwc).getResidencyHome().findByPrimaryKey(
									camApp.getCurrentResidenceId());
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(currRes.getName());
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
						} else {
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (applicant.getSSN() == null
								|| "9999999999".equals(applicant.getSSN())) {
							cell.setCellValue("J");
						} else {
							cell.setCellValue("N");
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (camApp.getStudyBeginYear() != null) {
							cell.setCellValue(camApp.getStudyBeginYear().intValue());
						} else {
							cell.setCellValue("");
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (camApp.getStudyBeginMonth() != null) {
							cell.setCellValue(camApp.getStudyBeginMonth()
								.intValue());
						} else {
							cell.setCellValue("");
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (camApp.getStudyEndYear() != null) {
							cell.setCellValue(camApp.getStudyEndYear().intValue());
						} else {
							cell.setCellValue("");
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (camApp.getStudyEndMonth() != null) {
							cell.setCellValue(camApp.getStudyEndMonth().intValue());
						} else {
							cell.setCellValue("");
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(camApp.getOtherInfo());
						if (applied == null || applied.isEmpty()) {
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
						} else {
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(((Applied) applied.iterator()
									.next()).getSubcategory()
									.getApartmentCategory().getName());
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(application.getSubject().getName());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(formatDate(camApp.getHousingFrom()));
						if (waiting != null && !waiting.isEmpty()) {
							Iterator it2 = waiting.iterator();
							int count = 0;
							while (it2.hasNext()) {
								count++;
								WaitingList wl = (WaitingList) it2.next();
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(wl.getApartmentSubcategory()
										.getName());
							}
							for (int i = count; i < 3; i++) {
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
							}
						} else if (applied != null && !applied.isEmpty()) {
							Iterator it2 = applied.iterator();
							int count = 0;
							while (it2.hasNext()) {
								count++;
								Applied a = (Applied) it2.next();
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(a.getSubcategory().getName());
							}
							for (int i = count; i < 3; i++) {
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
							}
						} else {
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(camApp.getPriorityLevel());
						if (waiting == null || waiting.isEmpty()) {
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue("");
						} else {
							WaitingList wl = (WaitingList) waiting.iterator()
									.next();
							Collection rejectHist = ((RejectionHistoryHome) IDOLookup
									.getHome(RejectionHistory.class))
									.findAllByApplication(wl.getApplication());
							if (application.getStatus().equals(
									Status.APPROVED.toString())) {
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(formatDate(application
										.getStatusChanged()));
							} else {
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
							}
							cell = row.createCell(colNumber++);
							cell.setCellType(HSSFCell.CELL_TYPE_STRING);
							cell.setCellValue(wl.getNumberOfRejections());
							if (rejectHist != null && !rejectHist.isEmpty()) {
								RejectionHistory hist = ((RejectionHistory) (rejectHist
										.iterator().next()));
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(formatDate(hist
										.getRejectionDate()));
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(hist.getApartment()
										.getFloor().getBuilding().getComplex()
										.getName());
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(hist.getApartment()
										.getFloor().getBuilding().getName());
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(hist.getApartment().getName());
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
							} else {
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue("");
							}
						}
						
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(camApp.getSpouseSSN());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(camApp.getSpouseName());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(camApp.getSpouseSchool());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(camApp.getSpouseStudyTrack());
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (camApp.getSpouseStudyBeginYear() != null) {
							cell.setCellValue(camApp.getSpouseStudyBeginYear()
								.intValue());
						}
						else {
							cell.setCellValue("");
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (camApp.getSpouseStudyBeginMonth() != null) {
							cell.setCellValue(camApp.getSpouseStudyBeginMonth()
								.intValue());
						} else {
							cell.setCellValue("");							
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (camApp.getSpouseStudyEndYear() != null) {
							cell.setCellValue(camApp.getSpouseStudyEndYear()
								.intValue());
						} else {
							cell.setCellValue("");
						}
						cell = row.createCell(colNumber++);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						if (camApp.getSpouseStudyEndMonth() != null) {
							cell.setCellValue(camApp.getSpouseStudyEndMonth()
								.intValue());
						} else {
							cell.setCellValue("");
						}

						Collection children = getApplicationService(iwc)
								.getChildrenForApplication(applicant);
						if (children != null && !children.isEmpty()) {
							Iterator it2 = children.iterator();
							while (it2.hasNext()) {
								Applicant child = (Applicant) it2.next();
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(child.getFullName());
								cell = row.createCell(colNumber++);
								cell.setCellType(HSSFCell.CELL_TYPE_STRING);
								cell.setCellValue(child.getSSN());
							}
						}						
					}

					wb.write(out);
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FinderException e) {
				e.printStackTrace();
			}

		}

		displayForm();
	}

	protected ApplicationService getApplicationService(IWContext iwc)
			throws RemoteException {
		return (ApplicationService) IBOLookup.getServiceInstance(
				iwc.getApplicationContext(), ApplicationService.class);
	}

	private static HSSFCellStyle getStyleBold(HSSFWorkbook wb) {
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle styleBold = wb.createCellStyle();
		styleBold.setFont(font);

		return styleBold;
	}

	private String formatDate(Date date) {
		if (date == null) {
			return "";
		}

		IWTimestamp stamp = new IWTimestamp(date);
		return stamp.getDateString("yyyy-MM-dd HH:mm:ss");
	}

	private String getLastConfirmationDate(Collection waitingList) {
		if (waitingList == null || waitingList.isEmpty()) {
			return "";
		}

		Iterator it = waitingList.iterator();
		IWTimestamp latest = null;

		while (it.hasNext()) {
			WaitingList wl = (WaitingList) it.next();
			if (latest == null) {
				latest = new IWTimestamp(wl.getLastConfirmationDate());
			} else {
				if (wl.getLastConfirmationDate() != null) {
					IWTimestamp wlLatest = new IWTimestamp(
							wl.getLastConfirmationDate());

					if (latest.isEarlierThan(wlLatest)) {
						latest = wlLatest;
					}
				}
			}
		}

		if (latest != null) {
			return formatDate(latest.getDate());
		}
		
		return "";
	}

}