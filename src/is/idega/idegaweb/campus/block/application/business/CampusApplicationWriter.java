/*

 * $Id: CampusApplicationWriter.java,v 1.7 2004/06/24 15:20:17 aron Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */
package is.idega.idegaweb.campus.block.application.business;
import is.idega.idegaweb.campus.block.application.data.Applied;
import is.idega.idegaweb.campus.block.application.data.CampusApplication;
import is.idega.idegaweb.campus.business.CampusSettings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;

import com.idega.block.application.data.Applicant;
import com.idega.block.application.data.Application;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.ApartmentTypeHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.io.MediaWritable;
import com.idega.io.MemoryFileBuffer;
import com.idega.io.MemoryInputStream;
import com.idega.io.MemoryOutputStream;
import com.idega.presentation.IWContext;
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
public class CampusApplicationWriter implements MediaWritable{
	
	public static final String PRM_COMPLEX_ID = "cmplx_id";
	public static final String PRM_APARTMENT_TYPE_ID = "aprt_type_id";
	public static final String PRM_SUBJECT_ID = "app_sub_id";
	public static final String PRM_APPLICATION_STATUS = "app_status";
	public static final String PRM_CAMPUS_APPLICATION_ID = "cam_app_id";
	private MemoryFileBuffer buffer = null;
	private Collection applicationInfos = null;
	private ApplicationService appService = null;
	
	/* (non-Javadoc)
	 * @see com.idega.io.MediaWritable#getMimeType()
	 */
	public String getMimeType() {
		return "application/pdf";
	}
	/* (non-Javadoc)
	 * @see com.idega.io.MediaWritable#init(javax.servlet.http.HttpServletRequest, com.idega.presentation.IWContext)
	 */
	public void init(HttpServletRequest req, IWContext iwc) {
		// TODO Auto-generated method stub
		try {
			appService = (ApplicationService)IBOLookup.getServiceInstance(iwc,ApplicationService.class);
			// one application
			if(iwc.getParameter(PRM_CAMPUS_APPLICATION_ID)!=null){
			   Integer id = Integer.valueOf(iwc.getParameter(PRM_CAMPUS_APPLICATION_ID));
			   CampusApplication application = appService.getCampusApplicationHome().findByPrimaryKey(id);
			   applicationInfos = new ArrayList();
			   applicationInfos.add(application);
			}
			// applications in subject and with given status
			else if(iwc.getParameter(PRM_APPLICATION_STATUS)!=null && iwc.getParameter(PRM_SUBJECT_ID)!=null){
			    String status = iwc.getParameter(PRM_APPLICATION_STATUS);
			    Integer subid = Integer.valueOf(iwc.getParameter(PRM_SUBJECT_ID));
			    applicationInfos = appService.getCampusApplicationHome().findBySubjectAndStatus(subid,status,null,-1,-1);
			}
			// applications in a given waitinglist specified by apartment type and complex id
			else if(iwc.getParameter(PRM_APARTMENT_TYPE_ID)!=null && iwc.getParameter(PRM_COMPLEX_ID)!=null){
			    Integer typeid = Integer.valueOf(iwc.getParameter(PRM_APARTMENT_TYPE_ID));
			    Integer cplxid = Integer.valueOf(iwc.getParameter(PRM_COMPLEX_ID));
			    applicationInfos = appService.getCampusApplicationHome().findByApartmentTypeAndComplex(typeid,cplxid);
			}
			if(applicationInfos!=null && !applicationInfos.isEmpty())
				writePDF(iwc.getIWMainApplication().getBundle(CampusSettings.IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc.getCurrentLocale()));
		} catch (IBOLookupException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FinderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/* (non-Javadoc)
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
		}
		else
			System.err.println("buffer is null");

	}
	public boolean writePDF(IWResourceBundle iwrb)
	{
		boolean returner = false;
		try
		{
			buffer = new MemoryFileBuffer();
			MemoryOutputStream ous = new MemoryOutputStream(buffer);
			
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			PdfWriter.getInstance(document, ous);
			document.addAuthor("Idegaweb Campus");
			document.addSubject("");
			document.open();
			if (applicationInfos != null)
			{
				int len = applicationInfos.size();
				//System.err.println("listsize:"+len);
				//CampusApplicationHolder CAH;
				CampusApplication campusApplication;
				Application application;
				Applicant applicant;
				
				for (Iterator iter = applicationInfos.iterator(); iter.hasNext();) {
					 //CAH = (CampusApplicationHolder) iter.next();
					campusApplication = (CampusApplication) iter.next();
					application = campusApplication.getApplication();
					applicant = application.getApplicant();
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
					datatable.addCell(application.getSubmitted().toString());
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("changed", "Status change"));
					datatable.setDefaultColspan(3);
					datatable.addCell(application.getStatusChanged().toString());
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("status", "Status"));
					datatable.setDefaultColspan(3);
					datatable.addCell(getStatus(application.getStatus(), iwrb));
					datatable.setDefaultColspan(2);
					datatable.addCell(iwrb.getLocalizedString("applicant", "Applicant"));
					datatable.setDefaultColspan(2);
					datatable.addCell(iwrb.getLocalizedString("spouse", "Spouse"));
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("name", "Name"));
					datatable.addCell(applicant.getFullName());
					datatable.addCell(iwrb.getLocalizedString("name", "Name"));
					datatable.addCell(campusApplication.getSpouseName());
					datatable.addCell(iwrb.getLocalizedString("ssn", "Socialnumber"));
					datatable.addCell(applicant.getSSN());
					datatable.addCell(iwrb.getLocalizedString("ssn", "Socialnumber"));
					datatable.addCell(campusApplication.getSpouseSSN());
					datatable.addCell(iwrb.getLocalizedString("legal_residence", "Legal Residence"));
					datatable.addCell(applicant.getLegalResidence());
					datatable.addCell(iwrb.getLocalizedString("school", "School"));
					datatable.addCell(campusApplication.getSpouseSchool());
					datatable.addCell(iwrb.getLocalizedString("residence", "Residence"));
					datatable.addCell(applicant.getResidence());
					datatable.addCell(iwrb.getLocalizedString("studytrack", "Study Track"));
					datatable.addCell(campusApplication.getSpouseStudyTrack());
					datatable.addCell(iwrb.getLocalizedString("po", "PO"));
					datatable.addCell(applicant.getLegalResidence());
					datatable.addCell(iwrb.getLocalizedString("study_begins", "Study begins"));
					String studybegin =
						campusApplication.getSpouseStudyBeginMonth()
							+ " "
							+ campusApplication.getSpouseStudyBeginYear();
					datatable.addCell(studybegin);
					datatable.addCell(iwrb.getLocalizedString("phone", "Residence phone"));
					datatable.addCell(applicant.getResidencePhone());
					datatable.addCell(iwrb.getLocalizedString("study_ends", "Study ends"));
					String studyend =
						campusApplication.getSpouseStudyEndMonth()
							+ " "
							+ campusApplication.getSpouseStudyEndYear();
					datatable.addCell(studybegin);
					datatable.addCell(iwrb.getLocalizedString("email", "Email"));
					datatable.addCell(campusApplication.getEmail());
					datatable.addCell(iwrb.getLocalizedString("income", "Income"));
					if(campusApplication.getSpouseIncome()!=null)
						datatable.addCell(campusApplication.getSpouseIncome().toString());
					else 
						datatable.addCell("");
					datatable.addCell(iwrb.getLocalizedString("faculty", "Faculty"));
					datatable.addCell(campusApplication.getFaculty());
					datatable.setDefaultColspan(2);
					datatable.addCell(iwrb.getLocalizedString("children", "Children"));
					datatable.setDefaultColspan(1);
					Vector children = new Vector();
					int size = 0;
					if(campusApplication.getChildren()!=null){
					StringTokenizer st = new StringTokenizer(campusApplication.getChildren(), "\n");
					size = st.countTokens();
					children = new Vector(size);
					while (st.hasMoreTokens())
					{
						children.add(st.nextToken());
					}
					}
					datatable.addCell(iwrb.getLocalizedString("studytrack", "Study Track"));
					datatable.addCell(campusApplication.getStudyTrack());
					datatable.setDefaultColspan(2);
					datatable.addCell(size >= 1 ? (String) children.get(0) : "");
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("study_begins", "Study begins"));
					studybegin =
						campusApplication.getStudyBeginMonth()
							+ " "
							+ campusApplication.getStudyBeginYear();
					datatable.addCell(studybegin);
					datatable.setDefaultColspan(2);
					datatable.addCell(size >= 2 ? (String) children.get(1) : "");
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("study_ends", "Study ends"));
					studyend =
						campusApplication.getStudyEndMonth()
							+ " "
							+ campusApplication.getStudyEndYear();
					datatable.addCell(studybegin);
					datatable.setDefaultColspan(2);
					datatable.addCell(size >= 3 ? (String) children.get(2) : "");
					datatable.setDefaultColspan(1);
					datatable.addCell(iwrb.getLocalizedString("income", "Income"));
					datatable.addCell(campusApplication.getIncome().toString());
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
					Vector applied = new Vector(campusApplication.getApplied());
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
					datatable.addCell(campusApplication.getHousingFrom().toString());
					datatable.addCell("2");
					if (size >= 2)
					{
						Applied A = (Applied) applied.get(1);
						datatable.addCell(aptHome.findByPrimaryKey(A.getApartmentTypeId()).getName());
					}
					else
						datatable.addCell("");
					datatable.addCell(iwrb.getLocalizedString("wantfurniture", "Wants furniture"));
					datatable.addCell(campusApplication.getWantFurniture() ? "X" : "");
					datatable.addCell("3");
					if (size >= 3)
					{
						Applied A = (Applied) applied.get(2);
						datatable.addCell(aptHome.findByPrimaryKey(A.getApartmentTypeId()).getName());
					}
					else
						datatable.addCell("");
					datatable.addCell(iwrb.getLocalizedString("onwaitinglist", "On waitinglist"));
					datatable.addCell(campusApplication.getOnWaitinglist() ? "X" : "");
					document.add(datatable);
					document.newPage();
				}
			}
			else
				System.err.println("nothing to file");
			document.close();
			try
			{
				ous.close();
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
	private String getStatus(String status, IWResourceBundle iwrb)
	{
		if(appService!=null)
			try {
				return appService.getStatus(status,iwrb);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		return status;
	}
	
}
