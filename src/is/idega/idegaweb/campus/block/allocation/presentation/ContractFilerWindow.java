package is.idega.idegaweb.campus.block.allocation.presentation;

import com.idega.presentation.ui.Window;
import com.idega.presentation.IWContext;


	public class ContractFilerWindow extends Window{
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

 public ContractFilerWindow() {
	  setResizable(true);
		setMenubar(true);
  }

  protected void control(IWContext iwc){

		add(new ContractFiler());
  }

  public void main(IWContext iwc) throws Exception {
    control(iwc);
  }
}