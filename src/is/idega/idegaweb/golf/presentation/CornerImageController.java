package is.idega.idegaweb.golf.presentation;

import java.sql.SQLException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Image;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class CornerImageController extends GolferBlock implements LinkParameters{

  public CornerImageController() {

  }

  public void chooseView(IWContext iwc){

    if (iwc.isParameterSet(sTopMenuParameterName)) {

      String[] chosenParameterValue;
       chosenParameterValue = iwc.getParameterValues(sTopMenuParameterName);

      //GOLFBAG
      if (chosenParameterValue[0].equals(sInfoParameterValue)) {
        Image iWelcomeLogo = iwrb.getImage("/golferpage/upplysingar.gif");
        add(iWelcomeLogo);
      }

      //RESULTS HOME
      else if ((chosenParameterValue[0].equals(sRecordParameterValue)) || (chosenParameterValue[0].equals(homeResultsParameterValue))) {
        Image iWelcomeLogo = iwrb.getImage("/golferpage/velkomin.gif");
        add(iWelcomeLogo);
      }

      //RESULTS ABROAD
      else if (chosenParameterValue[0].equals(abroadResultsParameterValue)) {
        Image iWelcomeLogo = iwrb.getImage("/golferpage/velkomin.gif");
        add(iWelcomeLogo);
      }

      //STATISTICS
      else if (chosenParameterValue[0].equals(sStatisticsParameterValue)) {
        Image iStatisticsLogo = iwrb.getImage("/golferpage/tolfraedi.gif");
        add(iStatisticsLogo);
      }

      //PICTURES
      else if (chosenParameterValue[0].equals(sPicturesParameterValue)) {
        Image iWelcomeLogo = iwrb.getImage("/golferpage/velkomin.gif");
        add(iWelcomeLogo);
      }

      //HOME
      else if (chosenParameterValue[0].equals(sHomeParameterValue)) {
        Image iWelcomeLogo = iwrb.getImage("/golferpage/velkomin.gif");
        add(iWelcomeLogo);
      }

      //SUPPORTERS
      else if ((chosenParameterValue[0].equals(sInterviewsParameterValue)) || (chosenParameterValue[0].equals(sSubmitParameterValue))) {
        Image iInterviewsLogo = iwrb.getImage("/golferpage/velkomin.gif");
        add(iInterviewsLogo);
      }
    }
    else{
        Image iWelcomeLogo = iwrb.getImage("/golferpage/velkomin.gif");
        add(iWelcomeLogo);
    }
  }

  public void main(IWContext iwc) throws SQLException{
    super.main(iwc);
    chooseView(iwc);
  }
}
