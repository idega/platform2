package is.idega.idegaweb.campus.block.application.presentation;



import is.idega.idegaweb.campus.block.application.business.CampusApplicationFinder;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationHolder;
import is.idega.idegaweb.campus.block.application.business.CampusApplicationWriter;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.CreateException;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Window;



	public class ApplicationFilerWindow extends Window{

/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author

 * @version 1.0

 */



 public ApplicationFilerWindow() {

	  setResizable(true);

		setMenubar(true);

  }



  protected void control(IWContext iwc){



		//IWMainApplication iwma = iwc.getApplication()

		String identifier = "is.idega.idegaweb.campus.block.allocation";

		IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(identifier).getResourceBundle(iwc);

		String fileSeperator = System.getProperty("file.separator");

		String filepath = iwc.getServletContext().getRealPath(fileSeperator+"allocation/files"+fileSeperator);

		String filename = "temp.pdf";

		String path = filepath+filename;



    if(iwc.getParameter("cam_app_id")!=null){

      int id = Integer.parseInt(iwc.getParameter("cam_app_id"));

      CampusApplicationHolder CAH = CampusApplicationFinder.getApplicantInfo(id);

      Vector V = new Vector(1);

      V.add(CAH);

      boolean filewritten = CampusApplicationWriter.writePDF(V,iwrb,path);

      if(filewritten)

        setToRedirect("/servlet/pdf?&dir="+path,1);



    }

    else if(iwc.getParameter("app_status")!=null && iwc.getParameter("app_sub_id")!=null){

      String status = iwc.getParameter("app_status");

      int subid = Integer.parseInt(iwc.getParameter("app_sub_id"));

      Collection L = CampusApplicationFinder.listOfApplicationHoldersInSubject(subid,status);

      boolean filewritten = CampusApplicationWriter.writePDF(L,iwrb,path);

      if(filewritten)

        setToRedirect("/servlet/pdf?&dir="+path,1);

    }

    else if(iwc.getParameter("aprt_type_id")!=null && iwc.getParameter("cmplx_id")!=null){

      int typeid = Integer.parseInt(iwc.getParameter("aprt_type_id"));

      int cplxid = Integer.parseInt(iwc.getParameter("cmplx_id"));

     Collection L = null;;
	try {
		 L = CampusApplicationFinder.listOfCampusApplicationHoldersInWaitinglist(typeid, cplxid);
	}
	catch (RemoteException e) {
		e.printStackTrace();
	}
	catch (CreateException e) {
		e.printStackTrace();
	}
		
	boolean filewritten = false;
	if(L!=null)
       filewritten = CampusApplicationWriter.writePDF(L,iwrb,path);

      if(filewritten)

        setToRedirect("/servlet/pdf?&dir="+path,1);



    }

  }



  public void main(IWContext iwc) throws Exception {

    control(iwc);

  }

}
