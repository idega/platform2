/*
 * $Id: CampusAdminTemplate.java,v 1.1 2001/06/28 10:36:02 aron Exp $
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

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public abstract class CampusAdminTemplate extends AdminTemplate{

  public void Content(){
    InsertTilers();
    InsertTopLogo();
    InsertBanners();
    InsertBottomLogo();

    addMenuTitle(new Image("/pics/titles/IS/menutitle.gif"));
    addTopLeft(new Menu());
    addMainTitle(new Title());
    addTabs(new Tabber());

  }
}