package com.idega.projects.golf.templates;

import com.idega.projects.golf.templates.page.GolfersPage2;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class GolferTemplate2 extends GolferTemplate {

  public GolferTemplate2() {
  }

  public void initializePage(){
    setPage(new GolfersPage2());
  }
}