/*

 * $Id: CampusAdminPage.java,v 1.4 2004/05/24 14:21:40 palli Exp $

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

    addTopLeft(new CampusMenu());

    addMainTitle(new Title());

    addTabs(new CampusTabber());



  }

}

