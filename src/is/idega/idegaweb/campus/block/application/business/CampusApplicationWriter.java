/*

 * $Id: CampusApplicationWriter.java,v 1.6 2004/06/05 07:43:04 aron Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */
package is.idega.idegaweb.campus.block.application.business;
import is.idega.idegaweb.campus.block.application.data.Applied;

import java.io.FileOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;
/**

 *

 * @author <a href="mailto:aron@idega.is">Aron Birkir</a>

 * @version 1.0

 *

 */
public class CampusApplicationWriter
{
	public static boolean writePDF(Collection listOfCampusApplicationHolders, IWResourceBundle iwrb, String realpath)
	{
		boolean returner = false;
		try
		{
			String file = realpath;
			FileOutputStream fos = new FileOutputStream(file);
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(document, fos);
			document.addAuthor("Idegaweb Campus");
			document.addSubject("");
			document.open();
			if (listOfCampusApplicationHolders != null)
			{
				int len = listOfCampusApplicationHolders.size();
				//System.err.println("listsize:"+len);
				CampusApplicationHolder CAH;
				for (Iterator iter = listOfCampusApplicationHolders.iterator(); iter.hasNext();) {
					 CAH = (CampusApplicationHolder) iter.next();
					//System.err.println("name "+CAH.getApplicant().getFirstName());
					//System.err.println("email "+CAH.getCampusApplication().getEmail());
					Table datatable = new Table(4);
					datatable.setSpaceBetweenCells( 0f);
					datatable.setSpaceInsideCell( 3f);
					datatable.setBorder(Rectangle.NO_BORDER);
					int headerwidths[] = { 18, 32, 18, 32 };
					datatable.setWidths(headerwidths);
					datatable.setWidth(100);
					datatable.setDefaultColspan(4);
					datatable.addCell(iwrb.getLocalizedString("application", "Application"));
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("submitted", "Submitted"));
					datatable.setDefaultColspan(3);
					datatable.addCell(CAH.getApplication().getSubmitted().toString());
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("changed", "Status change"));
					datatable.setDefaultColspan(3);
					datatable.addCell(CAH.getApplication().getStatusChanged().toString());
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("status", "Status"));
					datatable.setDefaultColspan(3);
					datatable.addCell(getStatus(CAH.getApplication().getStatus(), iwrb));
					datatable.setDefaultColspan(2);
					datatable.addCell(iwrb.getLocalizedString("applicant", "Applicant"));
					datatable.setDefaultColspan(2);
					datatable.addCell(iwrb.getLocalizedString("spouse", "Spouse"));
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("name", "Name"));
					datatable.addCell(CAH.getApplicant().getFullName());
					datatable.addCell(iwrb.getLocalizedString("name", "Name"));
					datatable.addCell(CAH.getCampusApplication().getSpouseName());
					datatable.addCell(iwrb.getLocalizedString("ssn", "Socialnumber"));
					datatable.addCell(CAH.getApplicant().getSSN());
					datatable.addCell(iwrb.getLocalizedString("ssn", "Socialnumber"));
					datatable.addCell(CAH.getCampusApplication().getSpouseSSN());
					datatable.addCell(iwrb.getLocalizedString("legal_residence", "Legal Residence"));
					datatable.addCell(CAH.getApplicant().getLegalResidence());
					datatable.addCell(iwrb.getLocalizedString("school", "School"));
					datatable.addCell(CAH.getCampusApplication().getSpouseSchool());
					datatable.addCell(iwrb.getLocalizedString("residence", "Residence"));
					datatable.addCell(CAH.getApplicant().getResidence());
					datatable.addCell(iwrb.getLocalizedString("studytrack", "Study Track"));
					datatable.addCell(CAH.getCampusApplication().getSpouseStudyTrack());
					datatable.addCell(iwrb.getLocalizedString("po", "PO"));
					datatable.addCell(CAH.getApplicant().getLegalResidence());
					datatable.addCell(iwrb.getLocalizedString("study_begins", "Study begins"));
					String studybegin =
						CAH.getCampusApplication().getSpouseStudyBeginMonth()
							+ " "
							+ CAH.getCampusApplication().getSpouseStudyBeginYear();
					datatable.addCell(studybegin);
					datatable.addCell(iwrb.getLocalizedString("phone", "Residence phone"));
					datatable.addCell(CAH.getApplicant().getResidencePhone());
					datatable.addCell(iwrb.getLocalizedString("study_ends", "Study ends"));
					String studyend =
						CAH.getCampusApplication().getSpouseStudyEndMonth()
							+ " "
							+ CAH.getCampusApplication().getSpouseStudyEndYear();
					datatable.addCell(studybegin);
					datatable.addCell(iwrb.getLocalizedString("email", "Email"));
					datatable.addCell(CAH.getCampusApplication().getEmail());
					datatable.addCell(iwrb.getLocalizedString("income", "Income"));
					datatable.addCell(CAH.getCampusApplication().getSpouseIncome().toString());
					datatable.addCell(iwrb.getLocalizedString("faculty", "Faculty"));
					datatable.addCell(CAH.getCampusApplication().getFaculty());
					datatable.setDefaultColspan(2);
					datatable.addCell(iwrb.getLocalizedString("children", "Children"));
					datatable.setDefaultColspan(1);
					StringTokenizer st = new StringTokenizer(CAH.getCampusApplication().getChildren(), "\n");
					int size = st.countTokens();
					Vector children = new Vector(size);
					while (st.hasMoreTokens())
					{
						children.add(st.nextToken());
					}
					datatable.addCell(iwrb.getLocalizedString("studytrack", "Study Track"));
					datatable.addCell(CAH.getCampusApplication().getStudyTrack());
					datatable.setDefaultColspan(2);
					datatable.addCell(size >= 1 ? (String) children.get(0) : "");
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("study_begins", "Study begins"));
					studybegin =
						CAH.getCampusApplication().getStudyBeginMonth()
							+ " "
							+ CAH.getCampusApplication().getStudyBeginYear();
					datatable.addCell(studybegin);
					datatable.setDefaultColspan(2);
					datatable.addCell(size >= 2 ? (String) children.get(1) : "");
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("study_ends", "Study ends"));
					studyend =
						CAH.getCampusApplication().getStudyEndMonth()
							+ " "
							+ CAH.getCampusApplication().getStudyEndYear();
					datatable.addCell(studybegin);
					datatable.setDefaultColspan(2);
					datatable.addCell(size >= 3 ? (String) children.get(2) : "");
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("income", "Income"));
					datatable.addCell(CAH.getCampusApplication().getIncome().toString());
					datatable.setDefaultColspan(2);
					datatable.addCell(size >= 4 ? (String) children.get(3) : "");
					datatable.setDefaultColspan(1);
					if (size > 4)
					{
						for (int j = 4; j < size; j++)
						{
							datatable.addCell("");
							datatable.addCell("");
							datatable.setDefaultColspan(2);
							datatable.addCell((String) children.get(j));
						}
					}
					datatable.setDefaultColspan(2);
					datatable.addCell(iwrb.getLocalizedString("applied", "Applied"));
					datatable.addCell(iwrb.getLocalizedString("requests", "Requests"));
					datatable.setDefaultColspan(1);
					Vector applied = CAH.getApplied();
					size = applied.size();
					datatable.addCell("1");
					ApartmentTypeHome aptHome =(ApartmentTypeHome)IDOLookup.getHome(ApartmentType.class);
					if (size >= 1)
					{
						Applied A = (Applied) applied.get(0);
						datatable.addCell(aptHome.findByPrimaryKey(A.getApartmentTypeId()).getName());
					}
					else
						datatable.addCell("");
					datatable.addCell(iwrb.getLocalizedString("housingfrom", "Housing from"));
					datatable.addCell(CAH.getCampusApplication().getHousingFrom().toString());
					datatable.addCell("2");
					if (size >= 2)
					{
						Applied A = (Applied) applied.get(1);
						datatable.addCell(aptHome.findByPrimaryKey(A.getApartmentTypeId()).getName());
					}
					else
						datatable.addCell("");
					datatable.addCell(iwrb.getLocalizedString("wantfurniture", "Wants furniture"));
					datatable.addCell(CAH.getCampusApplication().getWantFurniture() ? "X" : "");
					datatable.addCell("3");
					if (size >= 3)
					{
						Applied A = (Applied) applied.get(2);
						datatable.addCell(aptHome.findByPrimaryKey(A.getApartmentTypeId()).getName());
					}
					else
						datatable.addCell("");
					datatable.addCell(iwrb.getLocalizedString("onwaitinglist", "On waitinglist"));
					datatable.addCell(CAH.getCampusApplication().getOnWaitinglist() ? "X" : "");
					document.add(datatable);
					document.newPage();
				}
			}
			else
				System.err.println("nothing to file");
			document.close();
			try
			{
				fos.close();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			returner = true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			returner = false;
		}
		return returner;
	}
	private static String getStatus(String status, IWResourceBundle iwrb)
	{
		String r = "";
		char c = status.charAt(0);
		switch (c)
		{
			case 'S' :
				r = iwrb.getLocalizedString("submitted", "Submitted");
				break;
			case 'A' :
				r = iwrb.getLocalizedString("approved", "Approved");
				break;
			case 'R' :
				r = iwrb.getLocalizedString("rejected", "Rejected");
				break;
		}
		return r;
	}
}
