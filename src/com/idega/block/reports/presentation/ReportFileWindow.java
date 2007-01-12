package com.idega.block.reports.presentation;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Window;



/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class ReportFileWindow extends Window implements Reports{

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.reports";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;


  public ReportFileWindow() {
    setURL("/servlet/MediaServlet");
    setWidth(800);
    setHeight(600);
    setResizable( true);
    setMenubar( true);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }
/*
  public void main(IWContext iwc) throws Exception{
    iwrb = getResourceBundle(iwc);

    //RequestDispatcher dispatcher =  iwc.getServletContext().getRequestDispatcher(iwc.getApplication().getMediaServletURI());
    //dispatcher.forward(iwc.getRequest(),iwc.getResponse());
    /*

    if(iwc.getParameter(ReportWriter.prmReportId)!=null && iwc.getParameter(ReportWriter.prmReportInfoId)!=null){
      StringBuffer url = new StringBuffer("/servlet/MediaServlet?&");
      url.append(ReportWriter.prmReportId).append("=").append(iwc.getParameter(ReportWriter.prmReportId));
      url.append("&").append(ReportWriter.prmReportInfoId).append("=").append(iwc.getParameter(ReportWriter.prmReportInfoId));
      url.append("&").append(ReportWriter.PRM_WRITABLE_CLASS).append("=").append(IWMainApplication.getEncryptedClassName(ReportWriter.class));
      setToRedirect(url.toString());
    }
    else{
      String title = iwrb.getLocalizedString("report_filer","Report Filer");
      setTitle(title);
      addTitle(title);
      addHeaderObject(getLinkTable());

      String prefix = "";
      String fileSeperator = System.getProperty("file.separator");
      String filepath = iwc.getServletContext().getRealPath(fileSeperator+"reports/temp"+fileSeperator);
      String filename = prefix+"temp.";
      String sReportId = iwc.getParameter(PRM_REPORTID );
      if(sReportId!=null){
        Report R = null;
        try{
          R = ((com.idega.block.reports.data.ReportHome)com.idega.data.IDOLookup.getHomeLegacy(Report.class)).findByPrimaryKeyLegacy(Integer.parseInt(sReportId));
        }
        catch(SQLException ex){
          R = null;
          ex.printStackTrace();
        }
        if(R!= null){
          if(iwc.getParameter("type")!=null){
            String type = iwc.getParameter("type");
            if(type.equalsIgnoreCase("xls")){
              filename = filename+type;
              String path = filepath+filename;
              MemoryFileBuffer buf = ReportWriter.writeXLS(R);
              iwc.setSessionAttribute("xls",buf);
              if(buf!=null)
                setToRedirect("/servlet/MediaServlet?&"+MemoryFileBufferWriter.PRM_SESSION_BUFFER+"=xls");
            }
            else if(type.equalsIgnoreCase("pdf")){
              filename = filename+type;
              String path = filepath+filename;
              MemoryFileBuffer buf = ReportWriter.writePDF(R);
              iwc.setSessionAttribute("pdf",buf);
              if(buf !=null)
                setToRedirect("/servlet/MediaServlet?&"+MemoryFileBufferWriter.PRM_SESSION_BUFFER+"=pdf");
            }
          }
        }
        else
          add(formatText(iwrb.getLocalizedString("no_report","No Report")));
      }
      else
         add(formatText(iwrb.getLocalizedString("no_report","No Report")));
    }
    */
//  }

  private PresentationObject getLinkTable(){
    Link L = new Link(this.iwrb.getLocalizedString("close","Close"));
    L.setFontStyle("text-decoration: none");
    L.setFontColor("#FFFFFF");
    L.setBold();
    L.setOnClick("window.close()");
    return L;
  }
}
