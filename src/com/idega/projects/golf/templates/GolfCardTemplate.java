package com.idega.projects.golf.templates;

import com.idega.projects.golf.templates.page.GolfCardPage;

public class GolfCardTemplate extends GolfMainJSPModule{

  public void initializePage() {
    this.setPage(new GolfCardPage());
  }

}