package is.idega.idegaweb.travel.presentation;

import com.idega.block.login.presentation.LoginEditor;
import com.idega.presentation.*;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class LoginChanger extends TravelWindow {

  public LoginChanger() {
    super.setTitle("idegaWeb Travel");
    setWidth(200 );
    setHeight(300 );
    super.setScrollbar(false);
  }

  public void main(IWContext iwc) {
    super.main(iwc);

    LoginEditor LE = new LoginEditor();
    Table T = new Table(1,1);
    T.setAlignment(1,1,"center");
    T.setAlignment("center");
    T.add(LE,1,1);
    add(T);
  }
}
