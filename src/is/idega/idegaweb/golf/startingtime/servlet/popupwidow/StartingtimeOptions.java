package is.idega.idegaweb.golf.startingtime.servlet.popupwidow;

import com.idega.jmodule.JSPWindowModule;
import is.idega.idegaweb.golf.startingtime.presentation.TeetimeOptions;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class StartingtimeOptions extends JSPWindowModule {

  public StartingtimeOptions() {
    setPage(new TeetimeOptions());
  }
}