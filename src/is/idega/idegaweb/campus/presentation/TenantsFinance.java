package is.idega.idegaweb.campus.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.block.finance.presentation.AccountViewer;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class TenantsFinance extends Block {


  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    AccountViewer fin = new AccountViewer();
    add(fin);
  }

}