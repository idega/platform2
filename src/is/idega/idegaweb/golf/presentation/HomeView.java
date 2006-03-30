package is.idega.idegaweb.golf.presentation;

import java.sql.SQLException;
import com.idega.block.news.presentation.NewsReader;
import com.idega.block.text.presentation.TextReader;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class HomeView extends GolferBlock {

  public HomeView() {
  }

  public void setHomeView(IWContext iwc){

  /**@todo  Set the correct stylesheet here*/
  //  this.setStyleSheetURL("/style/GolferPageView.css");

    Table homeTable = new Table(3,1);
    homeTable.setCellpadding(0);
    homeTable.setCellspacing(0);
    Image dotLineBackgroundImage;
    dotLineBackgroundImage = iwb.getImage("shared/brotalina.gif");
    homeTable.setBackgroundImage(2,1,dotLineBackgroundImage);
    homeTable.setWidth(1,1,"375");
    homeTable.setWidth(2,1,"1");
    homeTable.add(Text.emptyString(),2,1);

    NewsReader news = new NewsReader();

   // news.setConnectionAttributes("golfer_page", golferPageData.getNewsReaderID());
    //news.setNewsEditorURL("/golfers/newsIndex.jsp");
//    news.setNumberOfDays(4);
    news.setHeadlineImageURL("/idegaweb/bundles/golf.bundle/resources/shared/bullet.gif");
    news.setWidth("100%");
    //news.setNewsReaderURLAsSamePage(iwc);
    news.setNumberOfExpandedNews(8);
    news.setNumberOfDisplayedNews(8);
    news.setNumberOfLetters(200);
    news.getTextProxy().setFontSize(1);
    news.getHeadlineProxy().setFontSize(1);
    //news.setCollectionImage("collection.gif");
    news.showNewsCollectionButton(false);
    news.setShowImages(false);
    news.setShowOnlyDates(true);
    news.setHeadlineAsLink(true);
    homeTable.add(news,3,1);
    homeTable.setVerticalAlignment(3,1,"top");

    Image golferImage = new Image();

    /**@todo*/
    golferImage = iwb.getImage("shared/mynd.jpg");
    Table pictureTable = new Table(1,2);
    pictureTable.setCellpadding(10);
    pictureTable.setCellspacing(0);
    pictureTable.add(golferImage,1,1);
    TextReader text = new TextReader(golferPageData.getProfilerID());
      text.setEnableDelete(false);
    text.setWidth("100%");
    text.setTableTextSize(1);
    text.setHeadlineSize(1);
    text.setTextSize(1);
    pictureTable.add(text,1,2);
    homeTable.add(pictureTable,1,1);
    homeTable.setVerticalAlignment(1,1,"top");
    add(homeTable);
  }

  public void main(IWContext iwc) throws SQLException{
    super.main(iwc);
      setHomeView(iwc);
  }
}
