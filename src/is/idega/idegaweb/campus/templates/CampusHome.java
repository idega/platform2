/*

 * $Id: CampusHome.java,v 1.2 2002/04/06 19:11:15 tryggvil Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.templates;





import com.idega.presentation.IWContext;

import com.idega.block.news.presentation.NewsReader;

import com.idega.util.idegaTimestamp;



/**

 *

 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */

public class CampusHome extends MainTemplate{



  public void initializePage(){

    setPage(new CampusHomePage());

  }



  private class CampusHomePage extends CampusPage{



    public void main(IWContext iwc){



      CampusPage P = this;

      idegaTimestamp stamp= idegaTimestamp.RightNow();

      int daysIn = -150;

      stamp.addDays(daysIn);//dagar inni



      NewsReader news = new NewsReader(1);

    //news.setICObjectInstanceID(2);

      news.setLayout(news.NEWS_SITE_LAYOUT);

      news.setNumberOfLetters(500);

      news.getHeadlineProxy().setStyle("newsheadline");

      news.getTextProxy().setStyle("bodytext");

      news.setWidth("100%");

      news.setShowOnlyDates(true);

      news.setHeadlineAsLink(true);

      news.setHeadlineImageURL("/pics/news_icon.gif");

      news.setShowMoreButton(true);

      news.alignImageWithHeadline();

      P.add(news);

    }

  }



}

