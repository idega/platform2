/*
 * $Id: CampusTemplate.java,v 1.4 2001/07/13 16:00:55 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.templates;

import com.idega.block.login.presentation.Login;
import com.idega.jmodule.poll.moduleobject.BasicPollVoter;
import com.idega.jmodule.quote.presentation.QuoteReader;
import is.idegaweb.campus.service.*;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.textObject.Text;
import javax.servlet.http.*;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public abstract class CampusTemplate extends MainTemplate{

  public void Content(){
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
      login.setUserText("Nafn:");
      login.setPasswordText("Lykilorð:");

    addMenuTitle(new Image("/pics/titles/IS/menutitle.gif"));
    addRightTitle(new Image("/pics/titles/IS/logintitle.gif"));
    addTopLeft(new Menu());
    addTopRight(login);
    addTopRightDivider();
    addMainTitle(new Title());
    addTabs(new Tabber());
  }
}