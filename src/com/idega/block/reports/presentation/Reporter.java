package com.idega.block.reports.presentation;

import com.idega.block.reports.data.*;
import com.idega.block.reports.business.*;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Collections;
import java.util.List;
import com.idega.data.EntityFinder;
import com.idega.presentation.Editor;
import com.idega.presentation.Table;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.Script;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Image;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.block.IWBlock;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 2.0
 */


public class Reporter extends ReportPresentation implements IWBlock{

  private final String sAction = "rep_reporter_action";
  private String sActPrm = "";
  private int iAction = 0;
  private static final String prefix = "rep_reporter_";
	private static final String prmCategoryId = prefix+"cat";
  private String sLastOrder = "0";
  private int iCategoryId = -1;
	private boolean sqlEditAdmin = true;
	boolean newobjinst = false;


  private final static String IW_BUNDLE_IDENTIFIER="com.idega.block.reports";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
	private IWBundle core ;


  public Reporter(){
   super();
  }
  public Reporter(int iCategory){
		this();
    iCategoryId = iCategory;

  }

	public void setReportCategoryId(int iCategory){
	  iCategoryId = iCategory;
	}

  public void setSQLEdit(boolean value){
    sqlEditAdmin = value;
  }

  protected void control(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    core = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);

    Table T = new Table();
    T.setWidth("100%");
    T.setCellpadding(0);
    T.setCellspacing(0);
    System.err.println("iCategoryid "+iCategoryId);
    if(iCategoryId <= 0){
      String sCategoryId = iwc.getParameter(prmCategoryId );
      if(sCategoryId != null)
        iCategoryId = Integer.parseInt(sCategoryId);
      else if(getICObjectInstanceID() > 0){
        iCategoryId = ReportFinder.getObjectInstanceCategoryId(getICObjectInstanceID(),true);
        if(iCategoryId <= 0 ){
          newobjinst = true;
        }
      }
    }

    if(isAdmin){
      T.add(getAdminPart(iCategoryId,false,newobjinst,iwc),1,1);
    }

    Form form = new Form();

    if(iwc.getParameter(sAction) != null){
      sActPrm = iwc.getParameter(sAction);
      try{
        iAction = Integer.parseInt(sActPrm);
        switch(iAction){
          case ACT1:  break;
          case ACT2:  break;
          case ACT3: doChange(iwc); break;
          case ACT4: doDelete(iwc); break;
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    T.add(doMain(iwc),1,2);
		form.add(T);
    add(form);
  }

  private PresentationObject getAdminPart(int iCategoryId,boolean enableDelete,boolean newObjInst,IWContext iwc){
    Table T = new Table(3,1);
    T.setCellpadding(2);
    T.setCellspacing(2);
    IWBundle core = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
    if(iCategoryId > 0){
      Link ne = new Link(core.getImage("/shared/create.gif","create"));
      ne.setWindowToOpen(ReportSQLEditorWindow.class);
      ne.addParameter(ReportSQLEditorWindow.prmCategoryId,iCategoryId);
      //ne.addParameter(ReportSQLEditorWindow.prmReportId,"-1");
      T.add(ne,1,1);
      T.add(T.getTransparentCell(iwc),1,1);

      Link text = new Link(core.getImage("/shared/text.gif","text"));
      text.setWindowToOpen(ReportItemWindow.class);
      text.addParameter(ReportItemWindow.prmCategoryId,iCategoryId);
      //text.addParameter(ReportItemWindow.prmItems,"true");
      T.add(text,1,1);
      T.add(T.getTransparentCell(iwc),1,1);

      Link change = new Link(core.getImage("/shared/edit.gif","edit"));
      change.setWindowToOpen(ReportEditorWindow.class);
      change.addParameter(ReportEditorWindow.prmCategoryId,iCategoryId);
      //change.addParameter(ReportEditorWindow.prmObjInstId,getICObjectInstanceID());
      T.add(change,1,1);

      if ( enableDelete ) {
        T.add(T.getTransparentCell(iwc),1,1);
        Link delete = new Link(core.getImage("/shared/delete.gif"));
        delete.setWindowToOpen(ReportEditorWindow.class);
        delete.addParameter(ReportEditorWindow.prmDelete,iCategoryId);
        T.add(delete,3,1);
      }
    }
    if(newObjInst){
      Link newLink = new Link(core.getImage("/shared/create.gif"));
      newLink.setWindowToOpen(ReportEditorWindow.class);
      if(newObjInst)
        newLink.addParameter(ReportEditorWindow.prmObjInstId,getICObjectInstanceID());

      T.add(newLink,2,1);
    }
    T.setWidth("100%");
    return T;
  }

  private PresentationObject doMain(IWContext iwc){
    PresentationObject obj = (getCategoryReports(iCategoryId,sqlEditAdmin));
    TextFontColor = DarkColor;
    fontBold = true;
    return obj;
  }

  private PresentationObject getCategoryReports(int iCategoryId,boolean bEdit){
    ReportCategory RC = ReportFinder.getCategory(iCategoryId) ;

    Table T = new Table();

      T.setCellpadding(2);
      T.setCellspacing(1);
      T.setBorder(0);
      T.setWidth("100%");
      //Link lEdit =  new Link(core.getImage("/shared/create.gif"));
      //lEdit.setWindowToOpen(ReportSQLEditorWindow.class);
      //lEdit.addParameter(ReportSQLEditorWindow.prmCategoryId,iCategoryId);

      if(RC !=null){
        T.add(formatText(RC.getName()),2,1);
        T.add(formatText(RC.getInfo()),3,1);
      }

      TextFontColor = "#FFFFFF";
      int a = 2;

      T.setColumnAlignment(1,"right");
      T.setWidth(3,"50%");
      //T.add(lEdit,1,2);
      T.add(formatText(iwrb.getLocalizedString("name","Name")),2,2);
      T.add(formatText(iwrb.getLocalizedString("info","Info")),3,2);
      String prm = prefix+"chk";
      if(bEdit){
        T.setWidth(4,"40");
        T.setColumnAlignment(4,"center");
        T.add(formatText(iwrb.getLocalizedString("delete","Delete")),4,2);

      }
      TextFontColor = "#000000";
      List L = ReportEntityHandler.listOfReports(iCategoryId);
      if(L!=null){

        int len = L.size();

        for (int i = 0; i < len; i++) {
          Report R = (Report) L.get(i);
          int col = 1;
          int row = i+3;
          if(sqlEditAdmin){
            T.add(getAdminLink(R.getID(),iCategoryId),col,row);
          }
          T.add(getLink(R.getID()),col,row);

          col++;
          T.add(formatText(R.getName()),col++,row);
          T.add(formatText(R.getInfo()),col++,row);
          if(bEdit)
            T.add(getCheckBox(prm+i,R.getID()),col++,row);
        }
        T.add(new HiddenInput(this.sAction,String.valueOf(this.ACT4)));
        if(bEdit){
          SubmitButton deleteButtton = new SubmitButton(core.getImage("/shared/delete.gif"));//new Image("/reports/pics/delete.gif"));
          T.add(new HiddenInput(this.sAction,String.valueOf(this.ACT4)));
          T.add(deleteButtton,4,len+3);
          HiddenInput countHidden = new HiddenInput(prefix+"count",String.valueOf(len));
          T.add(countHidden);
        }
        T.setRowColor(len+3,WhiteColor);
      }

      T.setHorizontalZebraColored(this.LightColor,this.MiddleColor);
      T.setRowColor(1,WhiteColor);
      T.setRowColor(2,DarkColor);

      T.setColumnColor(1,WhiteColor);
      T.setWidth("100%");
      T.setWidth(1,"40");
      T.setWidth(2,"150");
      //T.setWidth(3,"50%");
      T.setColumnAlignment(1,"center");

    return T;
  }

  private CheckBox getCheckBox(String name,int id){
    CheckBox chk = new CheckBox(name,String.valueOf(id));
    setStyle(chk);
    return chk;
  }

  private Link getLink(int id){
    Link L = new Link(core.getImage("/shared/view.gif"));// Image("/reports/pics/view.gif"));
    L.setWindowToOpen(ReportViewWindow.class);
    L.addParameter(ReportViewWindow.prmReportId,id);
    return L;
  }
  private Link getAdminLink(int id,int catid){
    Link L = new Link(core.getImage("/shared/edit.gif"));//new Image("/reports/pics/edit.gif"));
    L.setWindowToOpen(ReportSQLEditorWindow.class);
    L.addParameter(ReportSQLEditorWindow.prmReportId,id);
    L.addParameter(ReportSQLEditorWindow.prmCategoryId,catid);
    return L;
  }

  protected void doChange(IWContext iwc) throws SQLException{

  }
  protected void doUpdate(IWContext iwc) throws SQLException{

  }

	public boolean deleteBlock(int iObjectInstanceId){
	  return ReportBusiness.deleteBlock(iObjectInstanceId);
	}

  protected void doDelete(IWContext iwc) throws SQLException{
    String prm = prefix+"chk";
    int count = Integer.parseInt(iwc.getParameter(prefix+"count"));
    for (int i = 0; i < count; i++) {
      if(iwc.getParameter(prm+i)!=null){
        String sId = iwc.getParameter(prm+i);
        try{
          int id = Integer.parseInt(sId);
					ReportBusiness.deleteReport(id);
        }
        catch(NumberFormatException ex){}
      }
    }
  }

	public void main(IWContext iwc){

    isAdmin = iwc.hasEditPermission(this);
		isAdmin = true;
    control(iwc);
  }

	public synchronized Object clone() {
    Reporter obj = null;
    try {
      obj = (Reporter)super.clone();

	    obj.iCategoryId = iCategoryId;


    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }


}
