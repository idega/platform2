/*
 * $Id: CampusAdminPage.java,v 1.2 2001/10/05 08:05:45 tryggvil Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.templates;


import is.idegaweb.campus.service.*;
import com.idega.presentation.Image;
import com.idega.presentation.text.Text;

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