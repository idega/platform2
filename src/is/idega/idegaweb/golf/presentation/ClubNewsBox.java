package is.idega.idegaweb.golf.presentation;

import is.idega.idegaweb.golf.entity.Union;
import is.idega.idegaweb.golf.entity.UnionHome;

import com.idega.block.news.presentation.News;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.HeaderTable;
import com.idega.util.IWTimestamp;

/**
 * Title:        idegaWeb Golf classes
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company: idega software
 * @author <a href="eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */

public class ClubNewsBox extends Block {

  protected final static String IW_BUNDLE_IDENTIFIER="com.idega.idegaweb.golf";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;


  public ClubNewsBox() {
    setCacheable("ClubNewsBox",3600000);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext modinfo) throws Exception{
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    //FIXME Fix so it works with old News data...
/*
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
    myTable.setBorder(0);

    NewsCategoryAttribute[] clubNewsAttr = (NewsCategoryAttribute[]) (com.idega.data.GenericEntity.getStaticInstance(NewsCategoryAttribute.class)).findAll("select * from news_category_attributes where attribute_name ='union_id' and news_category_attributes.news_category_id>3");
    int union_id = 0;
    Text unionText;
    if (clubNewsAttr.length > 0) {

      for (int a = 0; a < 5; a++) {
        unionText = new Text();
        unionText.setFontSize(1);
        unionText.setFontColor("#666666");

        union_id = Integer.parseInt((String)clubNews[a].getColumnValue("news_category_attributes_id"));
        Union union = is.idega.idegaweb.golf.business.GolfCacher.getCachedUnion(union_id);
        unionText.addToText(union.getAbbrevation()+" - ");


        idegaTimestamp stampur = new idegaTimestamp(clubNews[0].getDate());

        String minutes = stampur.getMinute()+"";
        if (stampur.getMinute() < 10) {
          minutes = "0" + stampur.getMinute();
        }

        Text newsDate = new Text(stampur.getDate()+"/"+stampur.getMonth()+"/"+stampur.getYear()+" "+stampur.getHour()+":"+minutes);
        newsDate.setFontSize(1);
        newsDate.setFontColor("#666666");

            Link newsLink = new Link(clubNews[0].getHeadline(),"/clubs/index2.jsp");
              newsLink.addParameter("union_id",""+union_id);
              newsLink.setFontSize(1);

            myTable.add(unionText,1,a+1);
            myTable.add(newsDate,1,a+1);
            myTable.addBreak(1,a+1);
            myTable.add(newsLink,1,a+1);
       }
    }

    headerTable.add(myTable);

    return headerTable;

    News[] news = (News[]) (new News()).findAll("select distinct news_category_id,news_date from news where news_category_id > 3 and news_category_id < 228 and news_category_id != 226 order by news_date desc");

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
    myTable.setBorder(0);

    for (int a = 0; a < 5; a++) {
      if (news.length > a) {
        News[] clubNews = (News[]) (com.idega.data.GenericEntity.getStaticInstance("com.idega.jmodule.news.data.News")).findAllByColumnOrdered("news_category_id",Integer.toString(news[a].getNewsCategoryId()),"news_date desc");
        Text unionText = new Text();
        unionText.setFontSize(1);
        unionText.setFontColor("#666666");

        NewsCategoryAttributes[] newsAttribute = (NewsCategoryAttributes[]) (com.idega.data.GenericEntity.getStaticInstance(NewsCategoryAttributes.class)).findAllByColumn("news_category_id",clubNews[0].getNewsCategoryId());

        int union_id = 0;

        if (newsAttribute.length > 0) {
          union_id = newsAttribute[0].getAttributeId();
          Union union = ((UnionHome) IDOLookup.getHomeLegacy(Union.class)).findByPrimaryKey(union_id);
          unionText.addToText(union.getAbbrevation()+" - ");
        }

        IWTimestamp stampur = new IWTimestamp(clubNews[0].getDate());

        String minutes = stampur.getMinute()+"";
        if (stampur.getMinute() < 10) {
          minutes = "0" + stampur.getMinute();
        }

        Text newsDate = new Text(stampur.getDate()+"/"+stampur.getMonth()+"/"+stampur.getYear()+" "+stampur.getHour()+":"+minutes);
        newsDate.setFontSize(1);
        newsDate.setFontColor("#666666");

            Link newsLink = new Link(clubNews[0].getHeadline(),"/clubs/index2.jsp");
              newsLink.addParameter("union_id",""+union_id);
              newsLink.setFontSize(1);

            myTable.add(unionText,1,a+1);
            myTable.add(newsDate,1,a+1);
            myTable.addBreak(1,a+1);
            myTable.add(newsLink,1,a+1);
          }
        }

        headerTable.add(myTable);

        add(headerTable);
*/
      }

}