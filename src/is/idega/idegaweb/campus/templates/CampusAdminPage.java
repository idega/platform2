/*

 * $Id: CampusAdminPage.java,v 1.3 2002/04/06 19:11:15 tryggvil Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.templates;







import is.idega.idegaweb.campus.presentation.*;

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

    addTopLeft(new CampusMenu());

    addMainTitle(new Title());

    addTabs(new CampusTabber());



  }

}

