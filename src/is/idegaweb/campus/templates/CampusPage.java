/*
 * $Id: CampusPage.java,v 1.2 2001/08/23 23:44:13 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.templates;

import com.idega.block.login.presentation.Login;
import com.idega.block.application.presentation.ReferenceNumber;
import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.textObject.Text;
import com.idega.jmodule.object.textObject.Link;
import is.idegaweb.campus.service.Menu;
import is.idegaweb.campus.service.Title;
import is.idegaweb.campus.service.Tabber;


/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusPage extends MainPage{

  public CampusPage(){
    super();
  }

  public void Content(){
    setAllMargins(0);
    InsertTilers();
    InsertTopLogo();
    InsertBanners();
    InsertBottomLogo();

    Login login = new Login();
    login.setLayout(Login.LAYOUT_STACKED);
    login.setUserTextColor("#000000");
    login.setPasswordTextColor("#000000");
    login.setHeight("110");
    login.setWidth("100");
    login.setUserTextSize(1);
    login.setPasswordTextSize(1);
    login.setStyle("font-family: Verdana; font-size: 8pt; border: 1 solid #000000");
    login.setInputLength(14);
    login.setSubmitButtonAlignment("right");
    login.addHelpButton();

    ReferenceNumber ref = new ReferenceNumber();
    ref.setLayout(ReferenceNumber.LAYOUT_STACKED);
    ref.setReferenceTextColor("#000000");
    ref.setHeight("75");
    ref.setWidth("100");
    ref.setReferenceTextSize(1);
    ref.setStyle("font-family: Verdana; font-size: 8pt; border: 1 solid #000000");
    ref.setInputLength(14);
    ref.setSubmitButtonAlignment("right");

    addMenuTitle(new Image("/pics/titles/IS/menutitle.gif"));
    addRightTitle(new Image("/pics/titles/IS/logintitle.gif"));
    addTopLeft(new Menu());
    addTopRight(login);
    addTopRightDivider();
    addTopRight(ref);
    addTopRightDivider();
    addMainTitle(new Title());
    addTabs(new Tabber());
  }
}