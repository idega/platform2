/*
 * $Id: CampusAdminPage.java,v 1.1 2001/08/29 21:18:24 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.templates;


import is.idegaweb.campus.service.*;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.textObject.Text;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class CampusAdminPage extends AdminPage{

  public void Content(){
    InsertTilers();
    InsertTopLogo();
    InsertBanners();
    InsertBottomLogo();

    addMenuTitle(new TitleIcons(TitleIcons.MAINMENU));
    addTopLeft(new Menu());
    addMainTitle(new Title());
    addTabs(new Tabber());

  }
}