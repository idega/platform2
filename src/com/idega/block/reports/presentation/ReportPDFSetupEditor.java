package com.idega.block.reports.presentation;

import java.util.Iterator;
import java.util.List;

import com.idega.block.reports.business.ReportBusiness;
import com.idega.block.reports.business.ReportFinder;
import com.idega.block.reports.data.Report;
import com.idega.block.reports.data.ReportInfo;
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
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.util.Edit;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class ReportPDFSetupEditor extends Block implements Reports{

  private final static String prmReportInfoId = "rcie_repinfoid";

  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public final static String typeSticker = "sticker";
  public final static String typeColumns = "columns";

  public ReportPDFSetupEditor() {

  }

  public String getBundleIdentifier(){
    return REPORTS_BUNDLE_IDENTIFIER;
  }

  public String getLocalizedNameKey(){
    return "pdf_setup";
  }

  public String getLocalizedNameValue(){
    return "PDF Setup";
  }

  public void main(IWContext iwc){
    this.iwrb = getResourceBundle(iwc);
    this.iwb = getBundle(iwc);
    if(iwc.isParameterSet(PRM_REPORTID)){
      int reportInfoId = -1;
      if(iwc.isParameterSet(prmReportInfoId)) {
		reportInfoId = Integer.parseInt(iwc.getParameter(prmReportInfoId));
	}
      Report eReport = ReportFinder.getReport(Integer.parseInt(iwc.getParameter(PRM_REPORTID)));
      if(iwc.isParameterSet("saveinfo")||iwc.isParameterSet("saveinfo.x")){
        saveReportInfo(iwc,eReport,reportInfoId);
        reportInfoId = -1;
      }
      else if(iwc.isParameterSet("dinfo")){
        deleteInfo(iwc,reportInfoId);
      }

      Table T = new Table();
      T.add(getReportInfoForm(typeSticker,reportInfoId,eReport.getID()),1,1);
      T.add(getReportInfoForm(typeColumns,reportInfoId,eReport.getID()),1,2);

      add(T);
    }
  }

  private PresentationObject getReportInfoForm(String type,int id, int iReportId){
    DataTable T = new DataTable();
    T.setWidth("100%");
    T.setTitlesHorizontal(true);
    T.addTitle(this.iwrb.getLocalizedString(type,type));
    T.addButton(new SubmitButton(this.iwrb.getLocalizedImageButton("save","Save"),"saveinfo"));
    int row = 1;
    int col = 1;
    T.add(Edit.formatText(this.iwrb.getLocalizedString("name","Name")),col++,row);
    T.add(Edit.formatText(this.iwrb.getLocalizedString("landscape","Landscape")),col++,row);
    if(type.equalsIgnoreCase(typeSticker)){
      T.add(Edit.formatText(this.iwrb.getLocalizedString("width","Width")),col++,row);
      T.add(Edit.formatText(this.iwrb.getLocalizedString("height","Heigth")),col++,row);
      T.add(Edit.formatText(this.iwrb.getLocalizedString("border","border")),col++,row);
    }
    else if(type.equalsIgnoreCase(typeColumns)){
      T.add(Edit.formatText(this.iwrb.getLocalizedString("columns","Columns")),col++,row);
    }
    T.add(Edit.formatText(this.iwrb.getLocalizedString("papersize","Papersize")),col++,row);
    row++;
    List L = ReportFinder.listOfReportInfo(type);
    ReportInfo rinfo;
    TextInput name = new TextInput("iname");
    CheckBox landscape = new CheckBox("landscape","true");
    IntegerInput width = new IntegerInput("width");
    width.setLength(5);
    IntegerInput height = new IntegerInput("height");
    height.setLength(5);
    DropdownMenu size = getPaperSizeDrop("pagesize");
    DropdownMenu columns = getColumnDrop("columns");
    DropdownMenu border = getBorderDrop("border");
    boolean formAdded = false;
    if(L!=null){
      Iterator iter = L.iterator();
      while(iter.hasNext()){
        col = 1;
        rinfo = (ReportInfo) iter.next();
        if(id == rinfo.getID()){
          name.setContent(rinfo.getName());

          landscape.setChecked(rinfo.getLandscape());
          if(rinfo.getWidth()>0) {
			width.setContent(String.valueOf((int)rinfo.getWidth()));
		}
          if(rinfo.getHeight()>0) {
			height.setContent(String.valueOf((int)rinfo.getHeight()));
		}
          if(rinfo.getPagesize()!=null) {
			size.setSelectedElement(rinfo.getPagesize());
		}
          border.setSelectedElement(String.valueOf(rinfo.getBorder()));
          T.add(name,col++,row);
          T.add(landscape,col++,row);
          if(type.equalsIgnoreCase(typeSticker)){
            T.add(width,col++,row);
            T.add(height,col++,row);
            T.add(border,col++,row);
          }
          else if(type.equalsIgnoreCase(typeColumns)){
            T.add(columns,col++,row);
          }
          T.add(size,col++,row);
          T.add(new HiddenInput(prmReportInfoId,String.valueOf(rinfo.getID())));
          formAdded = true;
        }
        else {
          Link Li = new Link(Edit.formatText(rinfo.getName()));
          Li.addParameter(prmReportInfoId,rinfo.getID());
          Li.addParameter(PRM_REPORTID,iReportId);
          T.add(Li,col++,row);

          if(rinfo.getLandscape()) {
			T.add(Edit.formatText(this.iwrb.getLocalizedString("landscape","Landscape")),col++,row);
		}
		else {
			T.add(Edit.formatText(this.iwrb.getLocalizedString("portrait","Portrait")),col++,row);
		}
          if(type.equalsIgnoreCase(typeSticker)){
            T.add(Edit.formatText(String.valueOf((int)rinfo.getWidth())),col++,row);
            T.add(Edit.formatText(String.valueOf((int)rinfo.getHeight())),col++,row);
            T.add(Edit.formatText(getBorder(rinfo.getBorder())),col++,row);
          }
          else if(type.equalsIgnoreCase(typeColumns)){
            T.add(Edit.formatText(rinfo.getColumns()),col++,row);
          }
          T.add(Edit.formatText(rinfo.getPagesize()),col++,row);

          Link delLi = new Link(this.iwb.getImage("/shared/deletex.gif"));
          delLi.addParameter(prmReportInfoId,rinfo.getID());
          delLi.addParameter(PRM_REPORTID,iReportId);
          delLi.addParameter("dinfo","true");
          T.add(delLi,col++,row);
        }
        row++;
      }
    }
    if(!formAdded){
      col = 1;
      T.add(name,col++,row);
      T.add(landscape,col++,row);
      if(type.equalsIgnoreCase(typeSticker)){
        T.add(width,col++,row);
        T.add(height,col++,row);
        T.add(border,col++,row);
      }
      else if(type.equalsIgnoreCase(typeColumns)){
        T.add(columns,col++,row);
      }
      T.add(size,col++,row);
    }
    else{
      Link li = new Link(this.iwrb.getLocalizedImageButton("new","New"));
      li.addParameter(PRM_REPORTID,iReportId);
       T.addButton(li);

    }
    T.add(new HiddenInput(PRM_REPORTID,Integer.toString(iReportId)));
    T.add(new HiddenInput("info_type",type));
    Form F = new Form();
    F.add(T);
    return F;
  }

  private void deleteInfo(IWContext iwc,int colid){
    ReportBusiness.deleteReportInfo(colid);
  }



  private void saveReportInfo(IWContext iwc,Report report ,int id){
    String name = iwc.getParameter("iname");
    if(name !=null && name.length() > 0){
      boolean landscape = iwc.isParameterSet("landscape");
      int width = 0,height = 0,columns = 0,border = -1;
      if(iwc.isParameterSet("width")) {
		width = Integer.parseInt(iwc.getParameter("width"));
	}
      if(iwc.isParameterSet("height")) {
		height = Integer.parseInt(iwc.getParameter("height"));
	}
      if(iwc.isParameterSet("columns")) {
		columns = Integer.parseInt(iwc.getParameter("columns"));
	}
      if(iwc.isParameterSet("border")) {
		border = Integer.parseInt(iwc.getParameter("border"));
	}
      String paperSize = iwc.getParameter("pagesize");
      String infotype = iwc.getParameter("info_type");
      ReportInfo info = ((com.idega.block.reports.data.ReportInfoHome)com.idega.data.IDOLookup.getHomeLegacy(ReportInfo.class)).createLegacy();
      if(id > 0) {
		info = ReportFinder.getReportInfo(id);
	}
      info.setName(name);
      info.setWidth(width);
      info.setHeight(height);
      info.setColumns(columns);
      info.setBorder(border);
      info.setType(infotype);
      info.setLandscape(landscape);
      info.setPagesize(paperSize);
      ReportBusiness.saveReportInfo(info);
    }

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

  private DropdownMenu getBorderDrop(String name){
	  DropdownMenu drp = new DropdownMenu(name);
		drp.addMenuElement(String.valueOf(Rectangle.BOX),"BOX"); //15

    drp.addMenuElement(String.valueOf(Rectangle.TOP),"TOP");// 1
    drp.addMenuElement(String.valueOf(Rectangle.BOTTOM),"BOTTOM");// 2
    drp.addMenuElement(String.valueOf(Rectangle.LEFT),"LEFT"); // 4
		drp.addMenuElement(String.valueOf(Rectangle.RIGHT),"RIGHT");// 8

    drp.addMenuElement(String.valueOf(Rectangle.TOP+Rectangle.BOTTOM),"TOP+BOTTOM");// 3
    drp.addMenuElement(String.valueOf(Rectangle.TOP+Rectangle.LEFT),"TOP+LEFT"); // 5
    drp.addMenuElement(String.valueOf(Rectangle.TOP+Rectangle.RIGHT),"TOP+RIGHT"); // 9
    drp.addMenuElement(String.valueOf(Rectangle.BOTTOM+Rectangle.RIGHT),"BOTTOM+RIGHT"); // 10
    drp.addMenuElement(String.valueOf(Rectangle.BOTTOM+Rectangle.LEFT),"BOTTOM+LEFT"); // 6

    drp.addMenuElement(String.valueOf(Rectangle.TOP+Rectangle.RIGHT+Rectangle.LEFT),"TOP+RIGHT+LEFT");// 13
    drp.addMenuElement(String.valueOf(Rectangle.TOP+Rectangle.RIGHT+Rectangle.BOTTOM),"TOP+RIGHT+BOTTOM");// 11
    drp.addMenuElement(String.valueOf(Rectangle.TOP+Rectangle.LEFT+Rectangle.BOTTOM),"TOP+LEFT+BOTTOM");// 7
    drp.addMenuElement(String.valueOf(Rectangle.BOTTOM+Rectangle.RIGHT+Rectangle.LEFT),"BOTTOM+RIGHT+LEFT");// 14

		return drp;
	}
  private String getBorder(int border){
    switch (border) {
      case Rectangle.BOX:   return "BOX";
      case Rectangle.TOP:   return "TOP";
      case Rectangle.BOTTOM:   return "BOTTOM";
      case Rectangle.LEFT:   return "LEFT";
      case Rectangle.RIGHT:   return "RIGHT";
      case Rectangle.TOP+Rectangle.BOTTOM:   return "TOP+BOTTOM";
      case Rectangle.TOP+Rectangle.LEFT:   return "TOP+LEFT";
      case Rectangle.TOP+Rectangle.RIGHT:   return "TOP+RIGHT";
      case Rectangle.BOTTOM+Rectangle.RIGHT:   return "BOTTOM+RIGHT";
      case Rectangle.BOTTOM+Rectangle.LEFT:   return "BOTTOM+LEFT";
      case Rectangle.TOP+Rectangle.RIGHT+Rectangle.LEFT:   return "TOP+RIGHT+LEFT";
      case Rectangle.TOP+Rectangle.RIGHT+Rectangle.BOTTOM:   return "TOP+RIGHT+BOTTOM";
      case Rectangle.TOP+Rectangle.LEFT+Rectangle.BOTTOM:   return "TOP+LEFT+BOTTOM";
      case Rectangle.BOTTOM+Rectangle.RIGHT+Rectangle.LEFT:   return "BOTTOM+RIGHT+LEFT";
    }
    return "";

  }
  private DropdownMenu getPaperSizeDrop(String name){
    String[] pageSizes = ReportFinder.pageSizes;
    DropdownMenu drp = new DropdownMenu(name);
		for (int i = 0; i< pageSizes.length; i++) {
			drp.addMenuElement(pageSizes[i]);
		}
    return drp;
  }
}
