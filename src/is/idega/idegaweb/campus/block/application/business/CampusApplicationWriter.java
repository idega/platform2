/*

 * $Id: CampusApplicationWriter.java,v 1.8.4.4 2009/02/06 15:43:53 palli Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */
package is.idega.idegaweb.campus.block.application.business;

import is.idega.idegaweb.campus.block.application.data.ApplicantFamily;
import is.idega.idegaweb.campus.block.application.data.Applied;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.business.CampusSettings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * 
 * 
 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * 
 * @version 1.0
 * 
 * 
 */
public class CampusApplicationWriter implements MediaWritable {

	public static final String PRM_COMPLEX_ID = "cmplx_id";
	public static final String PRM_SUBCATEGORY_ID = "subcat_id";
	public static final String PRM_SUBJECT_ID = "app_sub_id";
	public static final String PRM_APPLICATION_STATUS = "app_status";
	public static final String PRM_CAMPUS_APPLICATION_ID = "cam_app_id";
	private MemoryFileBuffer buffer = null;
	private Collection applicationInfos = null;
	private ApplicationService appService = null;
	private Locale locale = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.io.MediaWritable#getMimeType()
	 */
	public String getMimeType() {
		return "application/pdf";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.idega.io.MediaWritable#init(javax.servlet.http.HttpServletRequest,
	 * com.idega.presentation.IWContext)
	 */
	public void init(HttpServletRequest req, IWContext iwc) {
		locale = iwc.getCurrentLocale();
		try {
			appService = (ApplicationService) IBOLookup.getServiceInstance(iwc,
					ApplicationService.class);
			// one application
			if (iwc.getParameter(PRM_CAMPUS_APPLICATION_ID) != null) {
				Integer id = Integer.valueOf(iwc
						.getParameter(PRM_CAMPUS_APPLICATION_ID));
				CampusApplication application = appService
						.getCampusApplicationHome().findByPrimaryKey(id);
				applicationInfos = new ArrayList();
				applicationInfos.add(application);
			}
			// applications in subject and with given status
			else if (iwc.getParameter(PRM_APPLICATION_STATUS) != null
					&& iwc.getParameter(PRM_SUBJECT_ID) != null) {
				String status = iwc.getParameter(PRM_APPLICATION_STATUS);
				Integer subid = Integer.valueOf(iwc
						.getParameter(PRM_SUBJECT_ID));
				applicationInfos = appService.getCampusApplicationHome()
						.findBySubjectAndStatus(subid, status, null, -1, -1);
			}
			// applications in a given waitinglist specified by apartment type
			// and complex id
			else if (iwc.getParameter(PRM_SUBCATEGORY_ID) != null
					&& iwc.getParameter(PRM_COMPLEX_ID) != null) {
				// Integer typeid =
				// Integer.valueOf(iwc.getParameter(PRM_APARTMENT_TYPE_ID));
				Integer subcatid = Integer.valueOf(iwc
						.getParameter(PRM_SUBCATEGORY_ID));
				Integer cplxid = Integer.valueOf(iwc
						.getParameter(PRM_COMPLEX_ID));
				applicationInfos = appService.getCampusApplicationHome()
						.findBySubcategoryAndComplex(subcatid, cplxid);
			}
			if (applicationInfos != null && !applicationInfos.isEmpty())
				writePDF(iwc.getIWMainApplication().getBundle(
						CampusSettings.IW_BUNDLE_IDENTIFIER).getResourceBundle(
						iwc.getCurrentLocale()));
		} catch (IBOLookupException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.idega.io.MediaWritable#writeTo(java.io.OutputStream)
	 */
	public void writeTo(OutputStream out) throws IOException {
		if (buffer != null) {
			MemoryInputStream mis = new MemoryInputStream(buffer);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// Read the entire contents of the file.
			while (mis.available() > 0) {
				baos.write(mis.read());
			}
			baos.writeTo(out);
		} else
			System.err.println("buffer is null");

	}

	public boolean writePDF(IWResourceBundle iwrb) {
		boolean returner = false;
		try {
			buffer = new MemoryFileBuffer();
			MemoryOutputStream ous = new MemoryOutputStream(buffer);

			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(document, ous);
			document.addAuthor("Idegaweb Campus");
			document.addSubject("");
			document.open();
			if (applicationInfos != null) {
				Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD);
				Font textFont = new Font(Font.HELVETICA, 11, Font.NORMAL);
				DateFormat df = DateFormat.getDateTimeInstance(
						DateFormat.SHORT, DateFormat.SHORT, locale);
				int len = applicationInfos.size();
				CampusApplication campusApplication;
				Application application;
				Applicant applicant;

				for (Iterator iter = applicationInfos.iterator(); iter
						.hasNext();) {
					campusApplication = (CampusApplication) iter.next();
					application = campusApplication.getApplication();
					applicant = application.getApplicant();
					Table datatable = new Table(4);
					datatable.setSpaceBetweenCells(0f);
					datatable.setSpaceInsideCell(3f);
					datatable.setBorder(Rectangle.NO_BORDER);
					int headerwidths[] = { 18, 32, 18, 32 };
					datatable.setWidths(headerwidths);
					datatable.setWidth(100);

					datatable.setDefaultColspan(4);
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"application", "Application")
							+ " nr. "
							+ campusApplication.getPrimaryKey().toString(),
							headerFont)));

					datatable.setDefaultColspan(1);
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"submitted", "Submitted"), headerFont)));
					datatable.addCell(df.format(application.getSubmitted()));
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"subject", "Subject"), headerFont)));
					datatable.addCell(application.getSubject().getName());

					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"changed", "Status change"), headerFont)));
					datatable
							.addCell(df.format(application.getStatusChanged()));
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"priority_level", "Priority level"), headerFont)));
					if (campusApplication.getPriorityLevel() != null) {
						datatable.addCell(campusApplication.getPriorityLevel());
					} else {
						datatable.addCell("");
					}

					datatable.setDefaultColspan(1);
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"status", "Status"), headerFont)));
					datatable.setDefaultColspan(3);
					datatable.addCell(getStatus(application.getStatus(), iwrb));

					datatable.setDefaultColspan(2);
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"applicant", "Applicant"), headerFont)));
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"spouse", "Spouse"), headerFont)));

					datatable.setDefaultColspan(1);
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"name", "Name"), headerFont)));
					datatable.addCell(applicant.getFullName());
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"name", "Name"), headerFont)));
					datatable
							.addCell(campusApplication.getSpouseName() != null ? campusApplication
									.getSpouseName()
									: "");

					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"ssn", "Socialnumber"), headerFont)));
					datatable.addCell(applicant.getSSN());
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"ssn", "Socialnumber"), headerFont)));
					datatable
							.addCell(campusApplication.getSpouseSSN() != null ? campusApplication
									.getSpouseSSN()
									: "");

					datatable
							.addCell((new Phrase(iwrb.getLocalizedString(
									"legal_residence", "Legal Residence"),
									headerFont)));
					datatable.addCell(applicant.getLegalResidence());
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"school", "School"), headerFont)));
					datatable
							.addCell(campusApplication.getSpouseSchool() != null ? campusApplication
									.getSpouseSchool()
									: "");

					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"residence", "Residence"), headerFont)));
					datatable.addCell(applicant.getResidence());
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"studytrack", "Study Track"), headerFont)));
					datatable
							.addCell(campusApplication.getSpouseStudyTrack() != null ? campusApplication
									.getSpouseStudyTrack()
									: "");

					datatable.addCell((new Phrase(iwrb.getLocalizedString("po",
							"PO"), headerFont)));
					datatable.addCell(applicant.getPO());
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"study_begins", "Study begins"), headerFont)));
					if (campusApplication.getSpouseStudyBeginMonth().intValue() == 0
							&& campusApplication.getSpouseStudyBeginYear()
									.intValue() == 0) {
						datatable.addCell("");
					} else {
						String studybegin = campusApplication
								.getSpouseStudyBeginMonth()
								+ " "
								+ campusApplication.getSpouseStudyBeginYear();
						datatable.addCell(studybegin);
					}

					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"phone", "Residence phone"), headerFont)));
					datatable.addCell(applicant.getResidencePhone());
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"study_ends", "Study ends"), headerFont)));
					if (campusApplication.getSpouseStudyEndMonth().intValue() == 0
							&& campusApplication.getSpouseStudyEndYear()
									.intValue() == 0) {
						datatable.addCell("");
					} else {
						String studyend = campusApplication
								.getSpouseStudyEndMonth()
								+ " "
								+ campusApplication.getSpouseStudyEndYear();
						datatable.addCell(studyend);
					}

					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"mobile_phone", "Mobile phone"), headerFont)));
					datatable.addCell(applicant.getMobilePhone() != null ? applicant.getMobilePhone() : "");
					datatable.setDefaultColspan(2);
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"children", "Children"), headerFont)));
					datatable.setDefaultColspan(1);

					int size = 0;
					ApplicantFamily family = new ApplicantFamily(applicant);
					List children = family.getChildren();
					size = children.size();

					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"email", "Email"), headerFont)));
					datatable.addCell(campusApplication.getEmail());
					datatable.setDefaultColspan(2);
					datatable
							.addCell(size > 0 ? getChild(children, 0) : "");
					datatable.setDefaultColspan(1);

					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"faculty", "Faculty"), headerFont)));
					datatable
							.addCell(campusApplication.getFaculty() != null ? campusApplication
									.getFaculty()
									: "");
					datatable.setDefaultColspan(2);
					datatable
							.addCell(size > 1 ? getChild(children, 1) : "");
					datatable.setDefaultColspan(1);

					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"studytrack", "Study Track"), headerFont)));
					datatable.addCell(campusApplication.getStudyTrack());
					datatable.setDefaultColspan(2);
					datatable
							.addCell(size > 2 ? getChild(children, 2) : "");
					datatable.setDefaultColspan(1);

					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"study_begins", "Study begins"), headerFont)));
					String studybegin = campusApplication.getStudyBeginMonth() + " "
							+ campusApplication.getStudyBeginYear();
					datatable.addCell(studybegin);
					datatable.setDefaultColspan(2);
					datatable
							.addCell(size > 3 ? getChild(children, 3) : "");
					datatable.setDefaultColspan(1);

					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"study_ends", "Study ends"), headerFont)));
					String studyend = campusApplication.getStudyEndMonth() + " "
							+ campusApplication.getStudyEndYear();
					datatable.addCell(studyend);

					datatable.setDefaultColspan(2);
					datatable
							.addCell(size > 4 ? getChild(children, 4) : "");
					datatable.setDefaultColspan(1);
					if (size > 5) {
						for (int j = 5; j < size; j++) {
							datatable.addCell("");
							datatable.addCell("");
							datatable.setDefaultColspan(2);
							datatable.addCell(getChild(children, j));
						}
					}

					datatable.setDefaultColspan(2);
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"applied", "Applied"), headerFont)));
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"requests", "Requests"), headerFont)));
					datatable.setDefaultColspan(1);
					Vector applied = new Vector(campusApplication.getApplied());
					size = applied.size();
					datatable.addCell("1");
					if (size >= 1) {
						Applied A = (Applied) applied.get(0);
						StringBuffer display = new StringBuffer(A.getSubcategory().getName());
						display.append(" (");
						display.append(A.getSubcategory().getApartmentCategory().getName());
						display.append(")");
						datatable.addCell(display.toString());
					} else
						datatable.addCell("");
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"housingfrom", "Housing from"), headerFont)));
					datatable.addCell(df.format(campusApplication
							.getHousingFrom()));
					datatable.addCell("2");
					if (size >= 2) {
						Applied A = (Applied) applied.get(1);
						StringBuffer display = new StringBuffer(A.getSubcategory().getName());
						display.append(" (");
						display.append(A.getSubcategory().getApartmentCategory().getName());
						display.append(")");
						datatable.addCell(display.toString());
					} else
						datatable.addCell("");
					datatable.addCell("");
					datatable.addCell("");
					datatable.addCell("3");
					if (size >= 3) {
						Applied A = (Applied) applied.get(2);
						StringBuffer display = new StringBuffer(A.getSubcategory().getName());
						display.append(" (");
						display.append(A.getSubcategory().getApartmentCategory().getName());
						display.append(")");
						datatable.addCell(display.toString());
					} else
						datatable.addCell("");
					datatable.addCell("");
					datatable.addCell("");
					datatable.setDefaultColspan(1);
					datatable.addCell((new Phrase(iwrb.getLocalizedString(
							"other_info", "Other info"), headerFont)));
					datatable.setDefaultColspan(3);
					datatable.addCell(campusApplication.getOtherInfo() == null ? "" : campusApplication.getOtherInfo());
					
					document.add(datatable);
					document.newPage();
				}
			} else
				System.err.println("nothing to file");
			document.close();
			try {
				ous.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			returner = true;
		} catch (Exception ex) {
			ex.printStackTrace();
			returner = false;
		}
		return returner;
	}
	
	private String getChild(List children, int index) {
		Applicant child = (Applicant) children.get(index);
		StringBuffer childName = new StringBuffer(child.getFullName());
		if (child.getSSN() != null) {
			childName.append(" (");
			childName.append(child.getSSN());
			childName.append(")");
		}
		
		return childName.toString();
	}

	private String getStatus(String status, IWResourceBundle iwrb) {
		if (appService != null)
			try {
				return appService.getStatus(status, iwrb);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		return status;
	}

}
