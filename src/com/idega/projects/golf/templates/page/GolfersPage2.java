package com.idega.projects.golf.templates.page;
import com.idega.jmodule.object.ModuleInfo;
import java.lang.Exception;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.Image;
import com.idega.projects.golf.presentation.*;
import com.idega.projects.golf.business.GolferPageViewController;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolfersPage2 extends GolfersPage {

  public GolfersPage2() {
  }

  public void main(ModuleInfo modinfo) throws Exception{
    super.main(modinfo);
    LinkMenu linkMenu = new LinkMenu();
    addMenuLinks(linkMenu);
    CornerImageController cornerImageController = new CornerImageController();
    addLeftLogo(cornerImageController);

    /**@todo THIS IMAGE COULD BE INSERTED INTO FOR EXAMPLE GolferPageData.java
     *  SO THAT IT WOULDN'T BE HARDCODED
     */
    Image cornerImage = iwrb.getImage("/golferpage/bjorgvinir_logo.gif");
    addCornerLogo(cornerImage);

    this.addLeftTopImage("shared/ping.gif");
    this.addCenterTopImage("shared/footjoy.gif");
    this.addRightTopImage("shared/titleist.gif");

    Image sideBanners;
    sideBanners = iwb.getImage("shared/logo_leftside.gif");
    Table dummytable1 = new Table(1,1);
    dummytable1.setCellpadding(10);
    dummytable1.setWidth("100%");
    dummytable1.setAlignment(1,1,"center");
    dummytable1.setVerticalAlignment(1,1, "top");
    dummytable1.add(sideBanners,1,1);
    this.addLeftBanners(dummytable1);

    FakeSideMenu fakeSideMenu = new FakeSideMenu();
    this.addLeftLink(fakeSideMenu);
    add(GolferPageViewController.getView(modinfo));
  }
}