package is.idega.travel.presentation;

import com.idega.jmodule.object.*;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TravelWindow extends JModuleObject {

  private Table table = new Table(3,2);

  public TravelWindow() {
  }

  public void add(ModuleObject mo) {
    table.add(mo,2,2);
  }


  public void main(ModuleInfo modinfo) {

    super.add(table);
  }
}