package is.idega.idegaweb.golf.presentation;

import is.idega.idegaweb.golf.entity.Union;

import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.ui.HeaderTable;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWBundle;
import com.idega.util.idegaTimestamp;

import com.idega.jmodule.news.data.News;
import com.idega.jmodule.news.data.NewsCategory;
import com.idega.jmodule.news.data.NewsCategoryAttributes;

/**
 * Title:        idegaWeb Golf
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:      idega
 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>
 * @version 2.0
 */

public class ClubNewsBox extends Block {


  private final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";

  public ClubNewsBox() {
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc)throws Exception{
      setCacheable("NewsBox",3600000);//60*60*1000 1 hour
      add(clubNews(iwc));
  }

  protected HeaderTable clubNews(IWContext iwc) throws Exception{

    IWResourceBundle iwrb = getBundle(iwc).getResourceBundle(iwc);
    HeaderTable headerTable = new HeaderTable();
    headerTable.setWidth(148);
    headerTable.setBorderColor("#8ab490");
    headerTable.setHeaderText(iwrb.getLocalizedString("clubNews","Club news"));
    headerTable.setHeadlineSize(1);
    headerTable.setRightHeader(false);
    headerTable.setHeadlineAlign("left");

    Table myTable = new Table(1,5);
    myTable.setWidth("100%");
    myTable.setCellpadding(2);
    myTable.setCellspacing(2);



    News[] news = (News[]) (new News()).findAll("select distinct news_category_id from news where news_category_id > 3 and news_category_id < 233 and news_category_id != 226 and news_category_id != 228 order by news_date desc");


    myTable.setBorder(0);

    for (int a = 0; a < 5; a++) {
    try {
      if (news.length > a) {
        News[] clubNews = (News[]) (com.idega.data.GenericEntity.getStaticInstance("com.idega.jmodule.news.data.News")).findAllByColumnOrdered("news_category_id",Integer.toString(news[a].getNewsCategoryId()),"news_date desc");
        Text unionText = new Text();
        unionText.setFontSize(1);
        unionText.setFontColor("#666666");

        NewsCategoryAttributes[] newsAttribute = (NewsCategoryAttributes[]) (com.idega.data.GenericEntity.getStaticInstance(NewsCategoryAttributes.class)).findAllByColumn("news_category_id",clubNews[0].getNewsCategoryId());

        int union_id = 0;

        if (newsAttribute.length > 0) {
          union_id = newsAttribute[0].getAttributeId();
          Union union = ((is.idega.idegaweb.golf.entity.UnionHome)com.idega.data.IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKeyLegacy(union_id);
          unionText.addToText(union.getAbbrevation()+" - ");
        }

        idegaTimestamp stampur = new idegaTimestamp(clubNews[0].getDate());

        String minutes = stampur.getMinute()+"";
        if (stampur.getMinute() < 10) {
          minutes = "0" + stampur.getMinute();
        }

        Text newsDate = new Text(stampur.getDate()+"/"+stampur.getMonth()+"/"+stampur.getYear()+" "+stampur.getHour()+":"+minutes);
        newsDate.setFontSize(1);
        newsDate.setFontColor("#666666");


            Text text = new Text(clubNews[0].getHeadline());
            text.setFontSize(1);
            Link newsLink = new Link(text,"/clubs/index2.jsp");
            newsLink.addParameter("union_id",""+union_id);

            myTable.add(unionText,1,a+1);
            myTable.add(newsDate,1,a+1);
            myTable.addBreak(1,a+1);
            myTable.add(newsLink,1,a+1);
          }


        }
        catch (Exception ex) {
          ex.printStackTrace(System.err);
        }

        }

        headerTable.add(myTable);



        return headerTable;

      }

}
