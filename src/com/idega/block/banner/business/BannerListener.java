package com.idega.block.banner.business;

import com.idega.business.IWEventListener;
import com.idega.presentation.IWContext;
import com.idega.block.banner.business.BannerBusiness;

/**
 * Title:        Poll vote handling
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2001 - idega team - <a href="mailto:laddi@idega.is">Þórhallur Helgason</a>
 * @version 1.0
 */

public class BannerListener implements IWEventListener{

  public BannerListener() {
  }

  public void actionPerformed(IWContext iwc){
    String mode = iwc.getParameter(BannerBusiness.PARAMETER_MODE);
    if ( mode == null )
      mode = "";

    if ( mode.equalsIgnoreCase(BannerBusiness.PARAMETER_CLICKED) ) {
      String adID = iwc.getParameter(BannerBusiness.PARAMETER_AD_ID);
      String URL = null;
      if ( adID != null ) {
        URL = BannerBusiness.updateHits(Integer.parseInt(adID));
      }
      if ( URL != null ) {
        iwc.sendRedirect(URL);
      }
    }
  }
}