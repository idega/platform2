package com.idega.block.reports.presentation;



import java.util.Map;

import com.idega.block.reports.business.ReportBusiness;
import com.idega.block.reports.business.ReportFinder;
import com.idega.block.reports.data.Report;
import com.idega.block.reports.data.ReportColumnInfo;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.util.Edit;
import com.lowagie.text.Font;



/**

 * Title:        idegaclasses

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:      idega

 * @author <a href="aron@idega.is">Aron Birkir</a>

 * @version 1.0

 */



public class ReportPDFEditor extends Block implements Reports{



  private final static String prmReportColId = "rcie_repcolid";



  protected IWResourceBundle iwrb;

  protected IWBundle iwb;



  public final static String typeSticker = "sticker";

  public final static String typeColumns = "columns";



  public ReportPDFEditor() {



  }



  public String getBundleIdentifier(){

    return REPORTS_BUNDLE_IDENTIFIER;

  }



  public String getLocalizedNameKey(){

    return "pdf_report";

  }



  public String getLocalizedNameValue(){

    return "PDF Report";

  }



  public void main(IWContext iwc){

    this.iwrb = getResourceBundle(iwc);

    this.iwb = getBundle(iwc);

    if(iwc.isParameterSet(PRM_REPORTID)){

      int reportColId = -1,colnr = -2;

      if(iwc.isParameterSet(prmReportColId)) {
		reportColId = Integer.parseInt(iwc.getParameter(prmReportColId));
	}

      if(iwc.isParameterSet("col_nr")) {
		colnr = Integer.parseInt(iwc.getParameter("col_nr"));
	}

      Report eReport = ReportFinder.getReport(Integer.parseInt(iwc.getParameter(PRM_REPORTID)));

      if(iwc.isParameterSet("savecols")||iwc.isParameterSet("savecols.x")){

        if(colnr >= 0) {
			saveColumnInfo(iwc,eReport,reportColId);
		}

        reportColId = -1;

        colnr = -2;

      }

      else if(iwc.isParameterSet("dcols")){

        deleteColumnInfo(iwc,reportColId);

      }





      Table T = new Table();

      T.add(getReportColumnInfoForm(eReport.getID(),reportColId,colnr));



      add(T);

    }

  }





  private PresentationObject getReportColumnInfoForm(int iReportId,int id,int colnr){

    DataTable T = new DataTable();

    T.addTitle(this.iwrb.getLocalizedString("report_preferences","Report Preferences"));

    T.setTitlesHorizontal(true);

    T.addButton(new SubmitButton(this.iwrb.getLocalizedImageButton("save","Save"),"savecols"));



    int row = 1;

    int col = 1;

    T.add(Edit.formatText(this.iwrb.getLocalizedString("name","Name")),col++,row);

    T.add(Edit.formatText(this.iwrb.getLocalizedString("fontfamily","Font family")),col++,row);

    T.add(Edit.formatText(this.iwrb.getLocalizedString("fontsize","Font size")),col++,row);

    T.add(Edit.formatText(this.iwrb.getLocalizedString("fontstyle","Font style")),col++,row);

    T.add(Edit.formatText(this.iwrb.getLocalizedString("colspan","Span")),col++,row);

    T.add(Edit.formatText(this.iwrb.getLocalizedString("endstring","Ending")),col++,row);

    row++;

    Map map = ReportFinder.mapOfReportColumnInfoByColumnNumber(iReportId);



    Report R = ReportFinder.getReport(iReportId);

    String[] names = R.getHeaders();


    DropdownMenu family = getFamilyDrop("family");

    DropdownMenu size = getSizeDrop("size");

    DropdownMenu style = getStyleDrop("style");

    DropdownMenu span = getColumnDrop("span");

    CheckBox useName = new CheckBox("usename");

    DropdownMenu endString = getEndCharDrop("endstring");



    boolean formAdded = false;

    ReportColumnInfo info;

    int firstRow = row;

    for (int i = 0; i < names.length; i++) {

      info = null;

      col = 1;

      T.add(getFontLink(names[i],i,iReportId),col++,row);

      if(map!=null && map.containsKey(new Integer(i))){

        info = (ReportColumnInfo) map.get(new Integer(i));

      }

      if(i == colnr){

        if(info!=null){

          family.setSelectedElement(String.valueOf(info.getFontFamily()));

          size.setSelectedElement(String.valueOf(info.getFontSize()));

          style.setSelectedElement(String.valueOf(info.getFontStyle()));

          span.setSelectedElement(String.valueOf(info.getColumnSpan()));

          endString.setSelectedElement((info.getEndChar()));

          useName.setChecked(info.getShowName());

          T.add(new HiddenInput(prmReportColId,String.valueOf(info.getID())));

        }

        T.add(new HiddenInput("col_nr",String.valueOf(colnr)));

        T.add(family,col++,row);

        T.add(size,col++,row);

        T.add(style,col++,row);

        T.add(span,col++,row);

        T.add(endString,col++,row);

        formAdded = true;

      }

      else{

        if(info!=null){

          T.add(Edit.formatText(getFontFamily(info.getFontFamily())),col++,row);

          T.add(Edit.formatText(Float.toString(info.getFontSize())),col++,row);

          T.add(Edit.formatText(getStyle(info.getFontStyle())),col++,row);

          T.add(Edit.formatText(info.getColumnSpan()),col++,row);

          T.add(Edit.formatText(getEndStringName(info.getEndChar())),col++,row);



          Link delLi = new Link(this.iwb.getImage("/shared/deletex.gif"));

          delLi.addParameter(prmReportColId,info.getID());

          delLi.addParameter(PRM_REPORTID,iReportId);

          delLi.addParameter("dcols","true");

          T.add(delLi,col++,row);

        }

      }

      row++;

    }

    if(!formAdded && colnr == -1){

      T.add(new HiddenInput("col_nr",String.valueOf(0)));

      T.add(family,col++,firstRow);

      T.add(size,col++,firstRow);

      T.add(style,col++,firstRow);

      T.add(span,col++,firstRow);

      T.add(endString,col++,firstRow);

      formAdded = true;

    }

    T.add(new HiddenInput(PRM_REPORTID,Integer.toString(iReportId)));

    Form F = new Form();

    F.add(T);



    return F;

  }



  public void deleteColumnInfo(IWContext iwc,int id){

    ReportBusiness.deleteReportColumnInfo(id);

  }



  private void saveColumnInfo(IWContext iwc,Report report ,int id){

    //System.err.println("saving");

    int family = 0,size = 0,style = 0,colnr = -1,span = 1;;

    boolean useName = iwc.isParameterSet("usename");

    if(iwc.isParameterSet("col_nr")) {
		colnr = Integer.parseInt(iwc.getParameter("col_nr"));
	}

    if(iwc.isParameterSet("family")) {
		family = Integer.parseInt(iwc.getParameter("family"));
	}

    if(iwc.isParameterSet("size")) {
		size = Integer.parseInt(iwc.getParameter("size"));
	}

    if(iwc.isParameterSet("style")) {
		style = Integer.parseInt(iwc.getParameter("style"));
	}

    if(iwc.isParameterSet("span")) {
		span = Integer.parseInt(iwc.getParameter("span"));
	}

    String endString = iwc.getParameter("endstring");

   //endString = getEndStringName(endString);

    ReportColumnInfo info = ((com.idega.block.reports.data.ReportColumnInfoHome)com.idega.data.IDOLookup.getHomeLegacy(ReportColumnInfo.class)).createLegacy();

    if(id > 0) {
		info = ReportFinder.getReportColumnInfo(id);
	}

    info.setColumnNumber(colnr);

    info.setFontFamily(family);

    info.setFontSize(size);

    info.setFontStyle(style);

    info.setEndChar(endString);

    info.setShowName(useName);

    info.setColumnSpan(span);

    info.setReportId(report.getID());

    ReportBusiness.saveReportColumnInfo(info);

  }



  private Link getFontLink(String name,int columnInfoId,int iReportId){

    Link l = new Link(Edit.formatText(name));

    l.addParameter(PRM_REPORTID,iReportId);

    l.addParameter("col_nr",columnInfoId);

    return l;

  }



	private DropdownMenu getFamilyDrop(String name){

	  DropdownMenu drp = new DropdownMenu(name);

		drp.addMenuElement(String.valueOf(Font.HELVETICA),"HELVETICA");

		drp.addMenuElement(String.valueOf(Font.COURIER),"COURIER");

		drp.addMenuElement(String.valueOf(Font.TIMES_ROMAN),"TIMES_NEW_ROMAN");



		return drp;

	}



  private String getFontFamily(int family){

    switch (family) {

      case Font.HELVETICA: return "HELVETICA";

      case Font.COURIER: return "COURIER";

      case Font.TIMES_ROMAN: return "TIMES_NEW_ROMAN";

    }

    return "";

  }



  private DropdownMenu getColumnDrop(String name){

   DropdownMenu drp = new DropdownMenu(name);

		for (int i = 1; i < 4; i++) {

			drp.addMenuElement(String.valueOf(i));

		}



		return drp;

  }



	private DropdownMenu getSizeDrop(String name){

	  DropdownMenu drp = new DropdownMenu(name);

		for (int i = 8; i < 21; i++) {

			drp.addMenuElement(String.valueOf(i));

		}



		return drp;

	}

	private DropdownMenu getStyleDrop(String name){

	  DropdownMenu drp = new DropdownMenu(name);

		drp.addMenuElement(String.valueOf(Font.NORMAL),"NORMAL");

		drp.addMenuElement(String.valueOf(Font.BOLD),"BOLD");

		drp.addMenuElement(String.valueOf(Font.BOLDITALIC),"BOLDITALIC");

		drp.addMenuElement(String.valueOf(Font.ITALIC),"ITALIC");

		drp.addMenuElement(String.valueOf(Font.STRIKETHRU),"STRIKETHRU");

		drp.addMenuElement(String.valueOf(Font.UNDERLINE),"UNDERLINE");



		return drp;

	}

  private String getStyle(int style){

    switch (style) {

      case Font.NORMAL:   return "NORMAL";

      case Font.BOLD:   return "BOLD";

      case Font.BOLDITALIC:   return "BOLDITALIC";

      case Font.ITALIC:   return "ITALIC";

      case Font.STRIKETHRU:   return "STRIKETHRU";

      case Font.UNDERLINE:   return "UNDERLINE";

    }

    return "";



  }



  private DropdownMenu getEndCharDrop(String name){

	  DropdownMenu drp = new DropdownMenu(name);

		drp.addMenuElement("space","Space");

		drp.addMenuElement("newline","Newline");

		drp.addMenuElement("tab","Tab");

		drp.addMenuElement("tabtab","Two tabs");

		return drp;

	}



  private String getEndStringName(String endstring){

    if(endstring.equals("32")) {
		return "space";
	}
	else if(endstring.equals("10")) {
		return "newline";
	}
	else if(endstring.equals("\t")) {
		return "tab";
	}
	else if(endstring.equals("\t\t")) {
		return "tabtab";
	}

    return "";

  }



  private String getEndString(String stringName){

    if(stringName.equals("space")) {
		return "Space";
	}
	else if(stringName.equals("newline")) {
		return "Newline";
	}
	else if(stringName.equals("tab")) {
		return "Tab";
	}
	else if(stringName.equals("tabtab")) {
		return "Double tab";
	}

    return "";

  }







}
