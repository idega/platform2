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



public class CampusAnnounceMents extends PresentationObjectContainer {



  public CampusAnnounceMents() {

  }



  public void main(IWContext iwc){



    NewsReader news = new NewsReader(2);

      //news.setLayout(news.NEWS_SITE_LAYOUT);

      news.setLayout(news.NEWS_SITE_LAYOUT);

      news.setNumberOfLetters(500);

      news.getHeadlineProxy().setFontSize(2);

      news.getHeadlineProxy().setFontColor("#232D44");

      news.getHeadlineProxy().setMarkupAttribute("style","text-decoration: none");

      news.getTextProxy().setFontSize(1);

      news.setWidth("100%");

      news.setShowOnlyDates(true);

      news.setHeadlineAsLink(true);

      news.setHeadlineImageURL("/shared/news_icon.gif");

      news.setShowMoreButton(true);

    add(news);



  }

}

