
package com.idega.projects.campus.templates;


import com.idega.projects.campus.templates.MainTemplate;
import com.idega.jmodule.login.presentation.Login;
import com.idega.jmodule.poll.moduleobject.BasicPollVoter;
import com.idega.jmodule.quote.presentation.QuoteReader;
import com.idega.projects.campus.service.*;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.textObject.Text;


/**
 * Title:        Campus Template
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega multimedia
 * @author       <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */


public abstract class CampusTemplate extends MainTemplate{

  public void Content(){
    InsertTilers();
    InsertTopLogo();
    InsertBanners();

    Login login = new Login();
    login.setVertical();
    login.setTryAgainImageUrl("/pics/IS/inn.gif");
    login.setUserTextColor("#000000");
    login.setLoggedOnTextColor("#000000");
    login.setPasswordTextColor("#000000");
    login.setHeight("100");
    login.setWidth("135");
    login.setLoginImageUrl("/pics/IS/inn.gif");
    login.setLogOutImageUrl("/pics/IS/ut.gif");
    login.setUserTextSize("1");
    login.setPasswordTextSize("1");
    login.setStyle("font-family: Verdana; font-size: 8pt; border: 1 solid #000000");
    login.setInputLength(10);

    BasicPollVoter poll = new BasicPollVoter("/poll/results.jsp");
      poll.setWidth(135);

    QuoteReader quote = new QuoteReader();
      quote.setClean();
      quote.setQuoteWidth("135");

    addMenuTitle(new Image("/pics/titles/IS/menutitle.gif"));
    addRightTitle(new Image("/pics/titles/IS/logintitle.gif"));
    addTopLeft(new Menu());
    addTopRight(login);
//    addRight(quote);
    addRight(new Image("/pics/titles/IS/poll.gif"));

    addRight(poll);
    addRight(Text.getBreak());
    addRight(new Image("/pics/titles/IS/calendar.gif"));
    addMainTitle(new Title());
    addTabs(new Tabber());

  }
}