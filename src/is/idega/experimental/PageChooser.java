package is.idega.experimental;

import com.idega.presentation.ui.*;


/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 1.0
 */

public class PageChooser extends AbstractChooser {

  public PageChooser() {
  }

  public Class getChooserWindowClass() {
    return PageChooserWindow.class;
  }

}