package is.idega.travel.presentation;

import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import is.idega.travel.presentation.TravelManager;

import com.idega.idegaweb.IWResourceBundle;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TravelWindow extends Window {

  private Table table = new Table(3,2);
  protected Page jPage;

  protected Text text = new Text();
  protected IWResourceBundle iwrb;

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public static String IW_BUNDLE_IDENTIFIER="is.idega.travel";


  public TravelWindow() {
  }

  public void add(PresentationObject mo) {
    table.add(mo,2,2);
  }

  public void add(String s) {
    table.add(s,2,2);
  }


  public void main(IWContext iwc) {
    setTemplate(iwc);
    super.add(table);
  }

  protected void close(boolean reloadParent) {
    if (reloadParent)
      jPage.setParentToReload();
    jPage.close();

  }

  private void setTemplate(IWContext iwc) {
    table.setWidth("100%");
    table.setBorder(0);
    table.setCellpadding(0);
    table.setCellspacing(0);

    jPage = super.getPage(iwc);
      jPage.setAllMargins(0);

    table.setColor(1,1,TravelManager.DARKBLUE);
    table.setColor(2,1,TravelManager.DARKBLUE);
    table.setColor(3,1,TravelManager.DARKBLUE);

    Text header1 = new Text("idega");
      header1.setFontColor(TravelManager.LIGHTORANGE);
      header1.setBold();
      header1.setFontSize(Text.FONT_SIZE_12_HTML_3);
    Text header2 = new Text("Web");
      header2.setFontColor("WHITE");
      header2.setBold();
      header2.setFontSize(Text.FONT_SIZE_12_HTML_3);

    table.add(header1,2,1);
    table.add(header2,2,1);

    text.setFontColor(TravelManager.backgroundColor);
    text.setFontSize(Text.FONT_SIZE_10_HTML_2);

    iwrb = super.getResourceBundle(iwc);

  }

}