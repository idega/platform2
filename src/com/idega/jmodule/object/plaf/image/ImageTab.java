package com.idega.jmodule.object.plaf.image;

import com.idega.jmodule.object.Image;

/**
 * Title:        IW Objects
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public interface ImageTab {
  public Image getTabSelected();
  public Image getTabNotSelected();
}