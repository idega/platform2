
package com.idega.projects.campus.templates;

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
 * Title:        Campus Template
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */

public abstract class MainTemplate extends JSPModule implements JspPage{

  private Table MainTable,HeaderTable,SubHeaderTable,TitleTable,RightTable;
  private Table LeftTable,TabTable;
  private Table content;
  private Image LeftHeaderImage,RightHeaderImage;
  private Image HeaderTiler,TitleTiler,SubHeaderTiler;
  private Image Logo,LowerLogo;
  private Image MenuTitle,MainTitle,RightTitle;
  private int BORDER = 0;
  private String sAlignment = "center";
  private Image Face = new Image("/pics/template/face.gif");
  private Image BottomLogo = new Image("/pics/template/bottomlogo.gif");


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
    Content();
    thisPage.add(content);
  }

  public void setBorder(int iBorder){
    BORDER = iBorder;
  }

  private void initTilers(){
    HeaderTiler = new Image("/pics/template/headertiler.gif");
    SubHeaderTiler = new Image("/pics/template/subtiler.gif");
    TitleTiler = new Image("/pics/template/titletiler.gif");
  }

  private void initLogos(){
      Face.setWidth(130);
      Face.setHeight(79);
      BottomLogo.setWidth(135);
      BottomLogo.setHeight(88);
  }

 public void template() {

    ModuleInfo modinfo = getModuleInfo();

    content = new Table(1,4);
    content.setAlignment(sAlignment);
    content.setCellpadding(0);
    content.setCellspacing(0);
    content.setWidth("100%");
    content.setHeight("100%");
    content.setBorder(BORDER);
    content.setHeight(1,"54");
    content.setHeight(2,"79");
    content.setHeight(3,"18");
    content.setHeight(4,"100%");
    //content.setWidth(1,"100%");
    content.setVerticalAlignment(1,3,"top");
    this.initTables();


    content.add(getHeaderTable(),1,1);
    content.add(getSubHeaderTable(),1,2);
    content.add(getTitleTable(),1,3);
    content.add(getMainTable(),1,4);
  }

  public void Content(){
    this.InsertTilers();
    this.InsertTopLogo();

  }

  public void initTables(){
    initMainTable();
    initHeaderTable();
    initSubHeaderTable();
    initTitleTable();
    initTabTable();
  }


  public void initMainTable(){
    MainTable = new Table(3,1);
    MainTable.setBorder(BORDER);
    MainTable.setVerticalAlignment(2,1,"top");
    MainTable.setCellpadding(0);
    MainTable.setCellspacing(0);
    MainTable.setHeight("100%");
    MainTable.setWidth("100%");
    MainTable.setWidth(1,"130");
    MainTable.setWidth(2,"100%");
    MainTable.setWidth(3,"130");
    MainTable.add(getRightTable(),3,1);
    MainTable.add(getLeftTable(),1,1);
  }
  public Table getMainTable(){
    return MainTable;
  }
  public void initHeaderTable(){
    HeaderTable = new Table(3,1);
    HeaderTable.setBorder(BORDER);
    HeaderTable.setCellpadding(0);
    HeaderTable.setCellspacing(0);
    HeaderTable.setHeight("54");
    HeaderTable.setWidth("100%");
    HeaderTable.setWidth(1,"130");
    HeaderTable.setWidth(2,"100%");
    HeaderTable.setWidth(3,"130");
    HeaderTable.setAlignment(1,1,"left");
    HeaderTable.setAlignment(3,1,"right");
  }
  public Table getHeaderTable(){
    return HeaderTable;
  }
  public void initSubHeaderTable(){
    SubHeaderTable = new Table(3,1);
    SubHeaderTable.setBorder(BORDER);
    SubHeaderTable.setCellpadding(0);
    SubHeaderTable.setCellspacing(0);
    SubHeaderTable.setHeight("79");
    SubHeaderTable.setWidth("100%");
    SubHeaderTable.setWidth(3,"100%");
    SubHeaderTable.setWidth(1,"130");
    SubHeaderTable.setAlignment(2,1,"left");
    //SubHeaderTable.setAlignment(3,1,"right");
  }

  public Table getSubHeaderTable(){
    return SubHeaderTable;
  }

  public void initTitleTable(){
    TitleTable = new Table(3,1);
    TitleTable.setBorder(BORDER);
    TitleTable.setCellpadding(0);
    TitleTable.setCellspacing(0);
    TitleTable.setHeight("17");
    TitleTable.setWidth("100%");
    TitleTable.setWidth(1,"130");
    TitleTable.setWidth(2,"100%");
    TitleTable.setWidth(3,"135");
    TitleTable.setAlignment(1,1,"left");
    TitleTable.setAlignment(1,1,"left");
    TitleTable.setAlignment(1,1,"right");
  }

  public Table getTitleTable(){
    return TitleTable;
  }

  public Table getLeftTable(){
    LeftTable = new Table(1,2);
    LeftTable.setHeight("100%");
    LeftTable.setHeight(1,2,"100%");
    LeftTable.setWidth("130");
    LeftTable.setCellpadding(0);
    LeftTable.setCellspacing(0);
    ///LeftTable.setWidth(1,2,"100%");
    LeftTable.setVerticalAlignment(1,1,"top");
    LeftTable.setBorder(BORDER);
    return LeftTable;
  }

  private Table getRightTable(){
    RightTable = new Table(1,3);
    RightTable.setWidth("135");
    RightTable.setHeight("100%");
    RightTable.setHeight(2,"100%");
    RightTable.setCellpadding(0);
    RightTable.setCellspacing(0);
    RightTable.setHeight(1,3,"88");
    RightTable.setVerticalAlignment(1,1,"top");
    RightTable.setVerticalAlignment(1,3,"bottom");
    RightTable.setBorder(BORDER);
    return RightTable;
  }

  private void initTabTable(){
    TabTable = new Table(1,2);
    TabTable.setBorder(BORDER);
    TabTable.setWidth("100%");
    TabTable.setHeight("79");
    TabTable.setHeight(2,"22");
    TabTable.setAlignment(1,2,"right");
    TabTable.setCellpadding(0);
    TabTable.setCellspacing(0);
    TabTable.setVerticalAlignment(1,2,"bottom");
  }

  private Table getTabTable(){
    return TabTable;
  }

  public void InsertTilers(){
    this.initTilers() ;
    HeaderTable.setBackgroundImage(HeaderTiler);
    SubHeaderTable.setBackgroundImage(SubHeaderTiler);
    TitleTable.setBackgroundImage(TitleTiler);
  }

  public void InsertTopLogo(){
    int num = (int) (Math.random() * 14) ;
    Image image = new Image("/pics/template/faces/face"+(num)+".jpg");
    image.setWidth(130) ;
    image.setHeight(79) ;
    this.addLogo(image) ;
  }
  public void InsertBottomLogo(){
    this.addLowerLogo(BottomLogo);
  }

  /** Insert the default logos
   *  integer parameters: 1 for FaceLogo; 2 for Both
   */
  public void InsertDefaultLogos(int count){
    this.addLogo(Face);
    if(count == 2)
      this.addLowerLogo(BottomLogo);
  }

  public void InsertBanners() {
    Image Header = new Image("/pics/template/header.gif");
    Header.setHeight(54);
    Header.setWidth(130);
    HeaderTable.add(Header,1,1);

    Image RightHeader = new Image("/pics/template/header2.gif");
    RightHeader.setHeight(54);
    RightHeader.setWidth(250);
    HeaderTable.add(RightHeader,3,1);

    Image Boxes = new Image("/pics/template/boxes.gif");
    Boxes.setWidth(300);
    Boxes.setHeight(79);
    SubHeaderTable.add(Boxes,2,1);
  }

  public void InsertTitles(){
    MenuTitle = new Image("/pics/template/menutitle.gif");
    this.addMenuTitle(MenuTitle) ;
    MainTitle = new Image("/pics/template/maintitle.gif");
    this.addMainTitle(MainTitle) ;
    RightTitle = new Image("/pics/template/logintitle.gif");
    this.addRightTitle(RightTitle);
  }

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

  /** Width: 130 pixel
   *  Height: 79 pixel
   */
  public void setFaceLogo(Image FaceLogo){
    Face = FaceLogo;
  }

  /**
   *
   */
  public void setLeftAlignment(String align){
    MainTable.setAlignment(1,1,align);
  }
  /**
   *
   */
  public void setCenterAlignment(String align){
    MainTable.setAlignment(2,1,align);
  }
  /** Adds a ModuleObject to the middle section
   *
   */
  public void add(ModuleObject objectToAdd){
    MainTable.add(Text.getBreak(),2,1);
    MainTable.add(objectToAdd,2,1);
  }
  /** Adds a ModuleObject to the center of the left side
   *
   */
  public void addLeft(ModuleObject objectToAdd){
    LeftTable.add(objectToAdd,1,2);
  }
  /** Adds a ModuleObject to the center of the right side
   *
   */
  public void addRight(ModuleObject objectToAdd){
    RightTable.add(objectToAdd,1,2);
  }
  /** Adds a ModuleObject to the titlebar on the left side
   *
   */
  public void addMenuTitle(ModuleObject objectToAdd){
    TitleTable.add(objectToAdd,1,1);
  }
  /** Adds a ModuleObject to the titlebar in the middle
   *
   */
  public void addMainTitle(ModuleObject objectToAdd){
    TitleTable.add(objectToAdd,2,1);
  }
  /** Adds a ModuleObject to the titlebar on the right side
   *
   */
  public void addRightTitle(ModuleObject objectToAdd){
    TitleTable.add(objectToAdd,3,1);
  }
  /** Adds a ModuleObject to the top of left side
   *
   */
  public void addTopLeft(ModuleObject objectToAdd){
    LeftTable.add(objectToAdd,1,1);
  }
  /** Adds a ModuleObject to the top of right side
   *
   */
  public void addTopRight(ModuleObject objectToAdd){
    RightTable.add(objectToAdd,1,1);
  }
  /** Adds a ModuleObject to the upper logo area on the left
   *
   */
  public void addLogo(ModuleObject objectToAdd){
    SubHeaderTable.add(objectToAdd,1,1);
  }
  /** Adds a ModuleObject to the lower logo area on the right
   *
   */
  public void addLowerLogo(ModuleObject objectToAdd){
    RightTable.add(objectToAdd,1,3);
  }
  /** Adds a ModuleObject into tab area
   *
   */
  public void addTabs(ModuleObject objectToAdd){
    TabTable.add(objectToAdd,1,2);
    SubHeaderTable.add(TabTable,3,1);
  }


}
