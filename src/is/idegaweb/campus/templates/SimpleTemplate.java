/*
 * $Id: SimpleTemplate.java,v 1.1 2001/08/13 10:14:23 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.templates;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.data.*;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public abstract class SimpleTemplate extends JSPModule implements JspPage{

  private Table content,MainTable,HeaderTable;

  private int BORDER = 0;
  private Image Header = new Image("/pics/template/headerblack.gif");
  private Image Tiler = new Image("/pics/template/hbtiler.gif");
  private String sAlignment = "center";

  public void initializePage(){
    super.initializePage();
    Page thisPage = getPage();
    thisPage.setMarginHeight(0);
    thisPage.setMarginWidth(0);
    thisPage.setLeftMargin(0);
    thisPage.setTopMargin(0);
    thisPage.setAlinkColor("black");
    thisPage.setVlinkColor("black");
    thisPage.setLinkColor("black");
    thisPage.setHoverColor("#4D6476");
    thisPage.setStyleSheetURL("/style/style.css");
    setPage(thisPage);
    template();

    thisPage.add(content);
  }
  public void setBorder(int iBorder){
    BORDER = iBorder;
  }

  public void template() {
    ModuleInfo modinfo = getModuleInfo();
    content = new Table(1,2);
    content.setAlignment(sAlignment);
    content.setCellpadding(0);
    content.setCellspacing(0);
    content.setWidth("100%");
    content.setHeight("100%");
    content.setBorder(BORDER);
    content.setHeight(1,"54");
    content.setHeight(2,"100%");
    content.setVerticalAlignment(1,2,"top");
    this.initTables();
    content.add(getHeaderTable(),1,1);
    content.add(getMainTable(),1,2);
  }

  public void initTables(){
    initMainTable();
    initHeaderTable();
  }
  public void initMainTable(){
    MainTable = new Table(1,1);
    MainTable.setBorder(BORDER);
    MainTable.setCellpadding(4);
    MainTable.setCellspacing(0);
  }

  public Table getMainTable(){
    return MainTable;
  }

  public void initHeaderTable(){
    HeaderTable = new Table(1,1);
    HeaderTable.setBorder(BORDER);
    HeaderTable.setCellpadding(0);
    HeaderTable.setCellspacing(0);
    HeaderTable.setHeight("54");
    HeaderTable.setWidth("100%");
    HeaderTable.setAlignment(1,1,"left");
    HeaderTable.add(Header,1,1);
    HeaderTable.setBackgroundImage(Tiler);
  }
  public Table getHeaderTable(){
    return HeaderTable;
  }

  /** Insert the default logos
   *  integer parameters: 1 for FaceLogo; 2 for Both
   */
  public boolean isAdmin() {
    if (getSessionAttribute("member_access") != null) {
      if (getSessionAttribute("member_access").equals("admin")) {
        return true;
      }
      else {
        return false;
      }
    }
    else {
      return false;
    }
  }
  public void add(ModuleObject objectToAdd){
    MainTable.add(objectToAdd,1,1);
  }

}

