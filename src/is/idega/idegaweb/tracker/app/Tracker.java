package is.idega.idegaweb.tracker.app;

import is.idega.idegaweb.tracker.business.TrackerBusiness;
import is.idega.idegaweb.tracker.presentation.PageCounter;

import com.idega.idegaweb.IWConstants;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.presentation.app.IWApplication;
import com.idega.presentation.ui.CloseButton;


/**
 * Title:        is.idega.idegaweb.tracker.app.Tracker
 * Description: This is the main class of the idegaWeb Tracker application
 * Copyright:    Copyright (c) 2002
 * Company:      idega
 * @author <a href="eiki@idega.is">Eirikur S. Hrafnsson</a>
 * @version 1.0
 */

public class Tracker extends IWApplication {

  public static String _FRAME_NAME_HEADER = "tracker_header";
  public static String _FRAME_NAME_MAIN = "tracker_main";
  public static String _FRAME_NAME_FOOTER = "tracker_footer";

  public Tracker() {
    super("idegaWeb Tracker",800,600);
    super.add(AppHeader.class);
    super.add(AppMainFrame.class);
    super.add(AppFooter.class);
    super.setFrameName(1,_FRAME_NAME_HEADER);
    super.setFrameName(2,_FRAME_NAME_MAIN);
    super.setFrameName(2,_FRAME_NAME_FOOTER);
    this.setSpanPixels(1,25);
    this.setSpanPixels(3,35);
    this.setScrollbar(false);
    this.setScrolling(1,false);
    this.setScrolling(2,true);
    this.setScrolling(3,false);
  }


  public static class AppHeader extends Page{

    public AppHeader(){
      super();
      this.setBackgroundColor("#0E2456");
      this.setAllMargins(0);
    }

    public void main(IWContext iwc) throws Exception {
      Table headerTable = new Table();
      headerTable.setCellpadding(0);
      headerTable.setCellspacing(0);
      headerTable.setVerticalAlignment("middle");
      headerTable.setWidth("100%");
      headerTable.setAlignment(2,1,"right");
      Image idegaweb = iwc.getIWMainApplication().getCoreBundle().getImage("/editorwindow/idegaweb.gif","idegaWeb");
      headerTable.add(idegaweb,1,1);

      this.add(headerTable);
    }

    public String getBundleIdentifier(){
      return TrackerBusiness.IW_BUNDLE_IDENTIFIER ;
    }

  }

  public static class AppFooter extends Page{
    public AppFooter(){
      super();
      this.setBackgroundColor(IWConstants.DEFAULT_LIGHT_INTERFACE_COLOR);
      this.setAllMargins(0);
    }

    public void main(IWContext iwc) throws Exception {
      if(iwc.getParameter("close") != null){
        //
      }else{
        Table footerTable = new Table(2,1);
        footerTable.setCellpadding(0);
        footerTable.setCellspacing(0);
        footerTable.setWidth(2,"20");
        footerTable.setAlignment("right");
        footerTable.setHeight("100%");
        footerTable.setVerticalAlignment(1,1,"middle");

        CloseButton close = new CloseButton();

        footerTable.add(close,1,1);
        this.add(footerTable);
      }
    }

    public String getBundleIdentifier(){
      return TrackerBusiness.IW_BUNDLE_IDENTIFIER ;
    }

  }

  public static class AppMainFrame extends Page{

    public AppMainFrame(){
      super();
    }

   public void main(IWContext iwc) throws Exception {
      PageCounter counter = new PageCounter();
      counter.setShowCurrentPageHits(false);
      counter.setShowCurrentPageSessions(false);
      counter.setShowTotalHits(true);
      counter.setShowTotalSessions(true);
      counter.setUpdateStats(false);
      counter.setShowAgents(true);
      counter.setShowReferers(true);
      counter.setShowPageList(true);

      add(counter);
    }


    public String getBundleIdentifier(){
      return TrackerBusiness.IW_BUNDLE_IDENTIFIER ;
    }

  }


}
