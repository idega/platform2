package is.idega.idegaweb.campus.presentation;


import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.IWContext;
import com.idega.util.idegaTimestamp;
import com.idega.block.login.presentation.Login;
import com.idega.block.application.presentation.ReferenceNumber;
import com.idega.block.application.business.ReferenceNumberHandler;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Window;
import com.idega.presentation.text.Text;
import com.idega.presentation.text.Link;
import com.idega.idegaweb.IWBundle;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="aron@idega.is">Aron Birkir</a>
 * @version 1.0
 */

public class CampusLogin extends PresentationObjectContainer {
	private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
	private IWBundle iwb;
  public CampusLogin() {
  }

	public String getBundleIdentifier(){
		return IW_BUNDLE_IDENTIFIER;
	}

  public void main(IWContext iwc){
		iwb = getBundle(iwc);

    Login login = new Login();
    login.setLayout(Login.LAYOUT_STACKED);
    login.setUserTextColor("#000000");
    login.setPasswordTextColor("#000000");
    login.setHeight("110");
    login.setWidth("100");
    login.setUserTextSize(1);
    login.setPasswordTextSize(1);
    login.setStyle("font-family: Verdana; font-size: 8pt; border: 1 solid #000000");
    login.setInputLength(14);
    login.setSubmitButtonAlignment("right");
    login.addHelpButton();

    ReferenceNumber ref = new ReferenceNumber();
    ref.setLayout(ReferenceNumber.LAYOUT_STACKED);
    ref.setReferenceTextColor("#000000");
    ref.setHeight("75");
    ref.setWidth("100");
    ref.setReferenceTextSize(1);
    ref.setStyle("font-family: Verdana; font-size: 8pt; border: 1 solid #000000");
    ref.setInputLength(14);
    ref.setSubmitButtonAlignment("right");
    ref.addHelpButton();

    Window idegaWindow = new Window("Idega","http://www.idega.is");
      idegaWindow.setMenubar(true);
      idegaWindow.setResizable(true);
      idegaWindow.setScrollbar(true);
      idegaWindow.setToolbar(true);
      idegaWindow.setTitlebar(true);
      idegaWindow.setStatus(true);
      idegaWindow.setHeight(600);
      idegaWindow.setWidth(800);
    Link idegaLink = new Link(new TitleIcons(TitleIcons.IDEGALOGO),idegaWindow);

		Table T = new Table();
		T.setCellpadding(0);
		T.setCellspacing(0);
		T.setWidth("100%");
		T.add(login,1,1);
		T.add(getDivider(),1,2);
		T.add(ref,1,3);
		T.add(getDivider(),1,4);
		T.add(idegaLink,1,5);
    add(T);
  }

	public Table getDivider() {
    Table dividerTable = new Table(1,1);
      dividerTable.setCellpadding(0);
      dividerTable.setCellspacing(0);
      dividerTable.setAlignment("center");

    Image divider  = iwb.getImage("/template/line.gif",99,3);
    //Image divider = new Image("/pics/line.gif","",99,3);
      divider.setAlignment("center");
      divider.setVerticalSpacing(8);

    dividerTable.add(divider);

    return dividerTable;
  }
}
