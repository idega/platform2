package is.idega.idegaweb.campus.presentation;







import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;



/**

 *

 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */

public class Title extends Block{



  private String iObjectName = "Title";

  private String LightColor,MiddleColor,DarkColor;

  private int iAct;

  private String sAct;

  private static final String strAction = TabAction.sAction;

  private boolean isAdmin;

  private Image Title;

  private final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4;

  private final int ACT5 = 5, ACT6 = 6, ACT7 = 7, ACT8 = 8;

  private final int NOACT = 0;

  protected IWResourceBundle iwrb;

  protected IWBundle iwb;

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";





  public Title(){

    MiddleColor = "#9FA9B3";

    LightColor = "#D7DADF";

    DarkColor = "#27324B";

  }



  private void control(IWContext iwc){



    try{



      if(iwc.getParameter(strAction) == null){

        if ( iwc.getSessionAttribute(strAction) != null ) {

          sAct = (String) iwc.getSessionAttribute(strAction);

          try {

            iAct = Integer.parseInt(sAct);

          }

          catch (NumberFormatException e){

            iAct = -1;

          }

        }

        else {

          iAct = NOACT;

        }

      }

      if(iwc.getParameter(strAction) != null){

        sAct = iwc.getParameter(strAction);

        try {

          iAct = Integer.parseInt(sAct);

        }

        catch (NumberFormatException e){

          iAct = -1;

        }

        if ( ((String) iwc.getSessionAttribute(strAction)) != (sAct) ) {

          iwc.setSessionAttribute(strAction,sAct);

        }

      }

      doAct();

    }

    catch(Exception S){	S.printStackTrace();	}

  }





  private void doAct(){

    String TitleUrl;

    String lang = "IS";

    Image image = null;

    switch (iAct) {

      case ACT1:  image = iwrb.getImage("/title/info.gif");             break;

      case ACT2:  image = iwrb.getImage("/title/office.gif");            break;

      case ACT3:  image = iwrb.getImage("/title/application.gif");      break;

      case ACT4:  image = iwrb.getImage("/title/apartment.gif");        break;

      case ACT5:  image = iwrb.getImage("/title/links.gif");            break;

      case ACT6:  image = iwrb.getImage("/title/english.gif");          break;

      //case ACT7:  image = iwrb.getImage("/title/maintitle.gif");         break;

      //case ACT8:  TitleUrl = iwrb.getImage("/title/maintitle.gif";       break;

      //default: image = iwrb.getImage("/title/maintitle.gif");            break;

    }

    if ( image != null ) {

      add(image);

    }

  }



  public String getObjectName(){

      return iObjectName;

  }



  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }



  public void main(IWContext iwc)  {

    iwrb = getResourceBundle(iwc);

    iwb = getBundle(iwc);



    isAdmin = iwc.hasEditPermission(this);



    /** @todo: fixa Admin*/

    control(iwc);

  }

}// class PriceCatalogueMaker





