/*

 * $Id: CampusPage.java,v 1.4 2004/05/24 14:21:40 palli Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.templates;



import is.idega.idegaweb.campus.presentation.CampusMenu;
import is.idega.idegaweb.campus.presentation.CampusTabber;
import is.idega.idegaweb.campus.presentation.Title;
import is.idega.idegaweb.campus.presentation.TitleIcons;

import com.idega.block.application.presentation.ReferenceNumber;
import com.idega.block.login.presentation.Login;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Window;







/**

 *

 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */

public class CampusPage extends MainPage{



  protected IWBundle iwb;

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";





  public CampusPage(){

    super();

  }



  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }



  public void main(IWContext iwc) throws Exception{

    super.main(iwc);

    iwb = getBundle(iwc);



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

    ref.addHelpButton();



    Window idegaWindow = new Window("Idega","http://www.idega.is");

      idegaWindow.setMenubar(true);

      idegaWindow.setResizable(true);

      idegaWindow.setScrollbar(true);

      idegaWindow.setToolbar(true);

      idegaWindow.setTitlebar(true);

      idegaWindow.setStatus(true);

      idegaWindow.setHeight(600);

      idegaWindow.setWidth(800);

    Link idegaLink = new Link(new TitleIcons(TitleIcons.IDEGALOGO),idegaWindow);





    addMenuTitle(new TitleIcons(TitleIcons.MAINMENU));

    addRightTitle(new TitleIcons(TitleIcons.LOGIN));

    addTopLeft(new CampusMenu());

    addTopRight(login);

    addTopRightDivider();

    addTopRight(ref);

    addTopRightDivider();

    addTopRight(idegaLink);

    addMainTitle(new Title());

    addTabs(new CampusTabber());

  }



}

