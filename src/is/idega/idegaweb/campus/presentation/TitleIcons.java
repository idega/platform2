package is.idega.idegaweb.campus.presentation;



import com.idega.presentation.Block;
import com.idega.presentation.Image;
import com.idega.presentation.IWContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;

public class TitleIcons extends Block {

private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
public final static String IDEGALOGO = "idegalogo";
public final static String MAINMENU = "mainmenu";
public final static String LOGIN = "login";

protected IWResourceBundle iwrb;
protected IWBundle iwb;

private String icon;
private Image image;

  public TitleIcons(){
    this.icon = MAINMENU;
  }

  public TitleIcons(String icon) {
    this.icon = icon;
  }

  public void setIconType(String type){
    icon = type;
  }

  public void main(IWContext iwc) {
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);

    if ( icon.equalsIgnoreCase(MAINMENU) )
      image = iwrb.getImage("/title/mainmenu.gif");
    else if ( icon.equalsIgnoreCase(LOGIN) )
      image = iwrb.getImage("/title/login.gif");
    else if ( icon.equalsIgnoreCase(IDEGALOGO) )
      image = iwb.getImage("idegaweb.gif");
    else
      image = new Image();

    add(image);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

}// class TitleIcons
