package is.idegaweb.campus.reports;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.block.reports.presentation.Reporter;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2001 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:aron@idega.is">Aron Birkir</a>
 * @version 1.1
 */

public class CampusReporterWindow extends IWAdminWindow {

  public CampusReporterWindow() {
    super();
  }

  public void main(IWContext iwc) throws Exception{
    boolean isAdmin = iwc.hasEditPermission(this);
    Reporter R = new Reporter();
    R.setMainCategoryAttribute("tenants");
    R.setSQLEdit(isAdmin);
    setResizable( true);
    add(R);
    setTitle("Campus Reports");

  }
}
