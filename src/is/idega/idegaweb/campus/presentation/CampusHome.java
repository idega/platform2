package is.idega.idegaweb.campus.presentation;





import com.idega.block.news.presentation.NewsReader;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObjectContainer;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author

 * @version 1.0

 */



public class CampusHome extends PresentationObjectContainer {



  public CampusHome() {

  }



  public void main(IWContext iwc){



    NewsReader news = new NewsReader(1);

    //news.setICObjectInstanceID(2);

      news.setLayout(news.NEWS_SITE_LAYOUT);

      news.setNumberOfLetters(500);

      news.getHeadlineProxy().setStyle("newsheadline");

      news.getTextProxy().setStyle("bodytext");

      news.setWidth("100%");

      news.setShowOnlyDates(true);

      news.setHeadlineAsLink(true);

      news.setHeadlineImageURL("/shared/news_icon.gif");

      news.setShowMoreButton(true);

      news.alignImageWithHeadline();

    add(news);

  }





}

