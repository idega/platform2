package com.idega.projects.golf.templates.page;

import com.idega.jmodule.object.Page;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.banner.presentation.*;
import com.idega.jmodule.sidemenu.presentation.Sidemenu;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.textObject.Link;
import com.idega.jmodule.news.presentation.NewsReader;
import java.util.Vector;
import java.sql.SQLException;
import com.idega.projects.golf.HandicapOverview;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.text.presentation.TextReader;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolfersPage extends Page {

  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
  protected boolean isAdmin, isTopPictureSet;
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private Table Maintable, innerLeftTable, topBannerTable;

    public GolfersPage() {
    this("");
  }

  public GolfersPage(String title){
    super(title);
    setAllMargins(0);
    this.setBackgroundColor("FFFFFF");
    this.setAlinkColor("FF6000");
    this.setVlinkColor("black");
    this.setLinkColor("FF6000");
    this.setHoverColor("#FF9310");
    this.setTextDecoration("none");
    this.setStyleSheetURL("/style/GolferPageView.css");

    Maintable = new Table(3,5);
    Maintable.mergeCells(3,3,3,4);
    Maintable.mergeCells(2,3,2,4);
    Maintable.mergeCells(2,1,2,2);
    Maintable.mergeCells(1,5,3,5);
    Maintable.mergeCells(1,3,1,4);

    Maintable.setWidth("100%");
    Maintable.setWidth(1,1,"120");
    Maintable.setWidth(1,2,"120");
    Maintable.setWidth(1,3,"120");

    Maintable.setAlignment(1,5,"center");
    Maintable.setAlignment(1,3,"center");
    Maintable.setVerticalAlignment(1,3,"top");
    Maintable.setVerticalAlignment(1,4,"top");
    Maintable.setVerticalAlignment(3,3,"top");
    Maintable.setVerticalAlignment(3,4,"top");

    Maintable.add(Text.emptyString(),2,1);
    Maintable.add(Text.emptyString(),2,3);
    Maintable.setCellpadding(0);
    Maintable.setCellspacing(0);
    Maintable.addBreak(1,5);
    innerLeftTable = new Table(1,2);
    innerLeftTable.setCellpadding(0);
    innerLeftTable.setCellspacing(0);
    Maintable.add(innerLeftTable,1,3);
    topBannerTable = new Table(3,1);
    topBannerTable.setAlignment(1,1,"left");
    topBannerTable.setAlignment(2,1,"center");
    topBannerTable.setAlignment(3,1,"right");
    topBannerTable.setWidth("100%");
    Maintable.add(topBannerTable,3,1);
    super.add(Maintable);
  }


  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public boolean isAdmin(){
    return isAdmin;
  }


  public void add(ModuleObject objectToAdd){
    Maintable.add(objectToAdd,3,3);
  }

  public void add(String stringToAdd){
    Maintable.add(stringToAdd,3,3);
  }

  public void addFooter(ModuleObject objectToAdd){
    Maintable.add(objectToAdd,1,5);
  }

  public void addFooter(String stringToAdd){
    Maintable.add(stringToAdd,1,5);
  }

  public void addLeftTopBanner(String stringToAdd){
    topBannerTable.add( stringToAdd, 1, 1);
  }

  public void addLeftTopBanner(ModuleObject objectToAdd){
    topBannerTable.add(objectToAdd,1,1);
  }

  public void addRightTopBanner(String stringToAdd){
    topBannerTable.add( stringToAdd,3,1);
  }

  public void addRightTopBanner(ModuleObject objectToAdd){
    topBannerTable.add(objectToAdd,3,1);
  }

  public void addCenterTopBanner(ModuleObject objectToAdd){
    topBannerTable.add(objectToAdd,2,1);
  }

  public void addCenterTopBanner(String stringToAdd){
    topBannerTable.add( stringToAdd,2,1);
  }

  public void addLeftLogo(ModuleObject objectToAdd){
    Maintable.add(objectToAdd,1,2);
  }

  public void addLeftLogo(String stringToAdd){
    Maintable.add( stringToAdd, 1, 2);
  }

  public void addLeftBanners(ModuleObject objectToAdd){
    innerLeftTable.add(objectToAdd,1,2);
  }

  public void addLeftBanners(String stringToAdd){
    innerLeftTable.add( stringToAdd, 1, 2);
  }

  public void addLeftLink(ModuleObject objectToAdd){
    innerLeftTable.add(objectToAdd,1,1);
  }

  public void addLeftLink(String stringToAdd){
    innerLeftTable.add(stringToAdd,1,1);
  }

  public void addCornerLogo(ModuleObject objectToAdd){
    Maintable.add(objectToAdd,1,1);
  }

  public void addCornerLogo(String stringToAdd){
    Maintable.add(stringToAdd,1,1);
  }

  public void addMenuLinks(ModuleObject objectToAdd){
    Maintable.add(objectToAdd,3,2);
  }

  public void addMenuLinks(String stringToAdd){
    Maintable.add(stringToAdd,3,2);
  }

  public void addSideBannerImage(String sideBannerImageIWBundleUrl){
    Image sideBannerImage;
    sideBannerImage = iwb.getImage(sideBannerImageIWBundleUrl);
    addLeftBanners(sideBannerImage);
  }

  public void addRightTopImage(String rightTopImageIWBundleUrl){
    Image rightTopImage;
    rightTopImage = iwb.getImage(rightTopImageIWBundleUrl);
    addRightTopBanner(rightTopImage);
  }

  public void addCenterTopImage(String centerTopImageIWBundleUrl){
    Image centerTopImage;
    centerTopImage = iwb.getImage(centerTopImageIWBundleUrl);
    addCenterTopBanner(centerTopImage);
  }

  public void addLeftTopImage(String leftTopImageIWBundleUrl){
    Image leftTopImage;
    leftTopImage = iwb.getImage(leftTopImageIWBundleUrl);
    addLeftTopBanner(leftTopImage);
  }

  public void addCornerLogoImage(String cornerLogoImageUrlInBundle,
    int cornerLogoImageWidth, int cornerLogoImageHeight){
    if (cornerLogoImageUrlInBundle != null) {
      Image imageToAdd = iwrb.getImage(cornerLogoImageUrlInBundle, cornerLogoImageWidth, cornerLogoImageHeight);
      Maintable.add(imageToAdd,1,1);
    }
  }
  public void addCornerLogoImage(String cornerLogoImageUrlInBundle){
    Image imageToAdd = iwrb.getImage(cornerLogoImageUrlInBundle);
    Maintable.add(imageToAdd,1,1);
  }

  public void main(ModuleInfo modinfo) throws Exception{
    try {
      isAdmin =  com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException E) {    }
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    Image dotLineBackgroundImage;
    dotLineBackgroundImage = iwb.getImage("shared/brotalina.gif");
    Maintable.setBackgroundImage(2,3,dotLineBackgroundImage);
    Maintable.setWidth(2,3,"1");

    Image footerImage;
    footerImage = iwrb.getImage("golferpage/index_23.gif");
    this.addFooter(footerImage);

  }

}