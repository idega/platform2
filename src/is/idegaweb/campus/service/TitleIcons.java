package is.idegaweb.campus.service;

import com.idega.jmodule.object.JModuleObject;
import com.idega.jmodule.object.Image;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.idegaweb.IWResourceBundle;

public class TitleIcons extends JModuleObject {

private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus";
public final static String MAINMENU = "mainmenu";
public final static String LOGIN = "login";

protected IWResourceBundle iwrb;

private String icon;
private Image image;

  public TitleIcons(String icon) {
    this.icon = icon;
  }

  public void main(ModuleInfo modinfo) {
    iwrb = getResourceBundle(modinfo);
    if ( icon.equalsIgnoreCase(MAINMENU) )
      image = iwrb.getImage("/title/mainmenu.gif");
    else if ( icon.equalsIgnoreCase(LOGIN) )
      image = iwrb.getImage("/title/login.gif");
    else
      image = new Image();

    add(image);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}// class TitleIcons