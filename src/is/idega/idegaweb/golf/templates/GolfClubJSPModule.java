package is.idega.idegaweb.golf.templates;

import is.idega.idegaweb.golf.templates.page.GolfClubJSPModulePage;

public class GolfClubJSPModule extends GolfMainJSPModule{

  public void initializePage() {
    this.setPage(new GolfClubJSPModulePage());
  }

}
