package is.idega.idegaweb.golf.templates;

import is.idega.idegaweb.golf.templates.page.GolfCardPage;

public class GolfCardTemplate extends GolfMainJSPModule{

  public void initializePage() {
    this.setPage(new GolfCardPage());
  }

}