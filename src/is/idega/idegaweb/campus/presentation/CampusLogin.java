/*

 * $Id: CampusLogin.java,v 1.10 2004/05/24 14:21:40 palli Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.presentation;



import com.idega.block.application.presentation.ReferenceNumber;
import com.idega.block.login.presentation.Login;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.Window;



/**

 * @author <a href="aron@idega.is">Aron Birkir</a>

 * @version 1.0

 */

public class CampusLogin extends Block {

  private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.campus";

  private IWBundle iwb;



  /**

   *

   */

  public CampusLogin() {

  }



  /**

   *

   */

  public String getBundleIdentifier() {

    return(IW_BUNDLE_IDENTIFIER);

  }



  /**

   *

   */

  public void main(IWContext iwc) {

    iwb = getBundle(iwc);



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

    login.setLoggedOnWindow(true);



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



    Table T = new Table();

    T.setCellpadding(0);

    T.setCellspacing(0);

    //T.setWidth("100%");

    T.add(login,1,1);

    T.add(getDivider(),1,2);

    T.add(ref,1,3);

    T.add(getDivider(),1,4);

    T.add(idegaLink,1,5);

    add(T);

  }



  /**

   *

   */

  public Table getDivider() {

    Table dividerTable = new Table(1,1);

    dividerTable.setCellpadding(0);

    dividerTable.setCellspacing(0);

    dividerTable.setAlignment("left");



    Image divider  = iwb.getImage("/template/line.gif",99,3);

    divider.setAlignment("left");

    divider.setVerticalSpacing(8);



    dividerTable.add(divider);



    return(dividerTable);

  }

}
