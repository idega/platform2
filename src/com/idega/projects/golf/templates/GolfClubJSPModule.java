package com.idega.projects.golf.templates;

import com.idega.projects.golf.templates.page.GolfClubJSPModulePage;

public class GolfClubJSPModule extends GolfMainJSPModule{

  public void initializePage() {
    this.setPage(new GolfClubJSPModulePage());
  }

}
