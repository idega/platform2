package is.idega.travel.presentation;

import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.*;
import com.idega.projects.nat.business.*;

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
  private Page jPage;

  protected Text text = new Text();
  protected IWResourceBundle iwrb;

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public static String IW_BUNDLE_IDENTIFIER="is.idega.travel";


  public TravelWindow() {
  }

  public void add(ModuleObject mo) {
    table.add(mo,2,2);
  }

  public void add(String s) {
    table.add(s,2,2);
  }


  public void main(ModuleInfo modinfo) {
    setTemplate(modinfo);
    super.add(table);
  }

  protected void close(boolean reloadParent) {
    if (reloadParent)
      jPage.setParentToReload();
    jPage.close();
  }

  private void setTemplate(ModuleInfo modinfo) {
    table.setWidth("100%");
    table.setBorder(0);
    table.setCellpadding(0);
    table.setCellspacing(0);

    jPage = super.getPage(modinfo);
      jPage.setAllMargins(0);

    table.setColor(1,1,NatBusiness.DARKBLUE);
    table.setColor(2,1,NatBusiness.DARKBLUE);
    table.setColor(3,1,NatBusiness.DARKBLUE);

    Text header1 = new Text("idega");
      header1.setFontColor(NatBusiness.LIGHTORANGE);
      header1.setBold();
      header1.setFontSize(Text.FONT_SIZE_12_HTML_3);
    Text header2 = new Text("Web");
      header2.setFontColor("WHITE");
      header2.setBold();
      header2.setFontSize(Text.FONT_SIZE_12_HTML_3);

    table.add(header1,2,1);
    table.add(header2,2,1);

    text.setFontColor(NatBusiness.textColor);
    text.setFontSize(Text.FONT_SIZE_10_HTML_2);

    iwrb = super.getResourceBundle(modinfo);

  }

}