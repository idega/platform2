package is.idega.travel.presentation;

import com.idega.presentation.IWContext;
import com.idega.block.login.presentation.Login;
import com.idega.presentation.text.Text;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class LoginPage extends TravelManager {

  public LoginPage() {
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    insertLogin(iwc);
  }

  private void insertLogin(IWContext iwc) {
    add(Text.NON_BREAKING_SPACE);
    add(getLoginObject(iwc));
  }

  protected static Login getLoginObject(IWContext iwc) {
    Login login = new Login();
      login.setColor(TravelManager.textColor);
      login.setUserTextColor(TravelManager.BLACK);
      login.setPasswordTextColor(TravelManager.BLACK);
    return login;
  }
}