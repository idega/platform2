package is.idega.idegaweb.golf.presentation;

import java.sql.SQLException;

import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class LinkMenu extends GolferBlock implements LinkParameters{

  public LinkMenu() {
  }

  private void setLinkMenu(){
    Table topTable = new Table(7,1);
    topTable.setHeight("101");

    Image iInfo = iwrb.getImage("/golferpage/navbar_01.gif");
    Image iRecord = iwrb.getImage("/golferpage/navbar_03.gif");
    Image iInterviews = iwrb.getImage("/golferpage/navbar_02.gif");
    Image iStatistics = iwrb.getImage("/golferpage/navbar_04.gif");
    Image iPictures = iwrb.getImage("/golferpage/navbar_05.gif");
    Image iHome = iwrb.getImage("/golferpage/navbar_06.gif");
    Image iMenuBackground = iwb.getImage("/shared/menuBackground.gif");

    Link lInfo = new Link(iInfo);
    Link lRecord = new Link(iRecord);
    Link lInterviews = new Link(iInterviews);
    Link lStatistics = new Link(iStatistics);
    Link lPictures = new Link(iPictures);
    Link lHome = new Link(iHome);

    /*lHome.addParameter("text_id","753");
    lHome.addParameter("module_object","is.idega.idegaweb.golf.block.text.presentation.TextReader");*/
    topTable.setBackgroundImage(iMenuBackground);
    //topTable.setBorder(1);

    topTable.setCellpadding(0);
    topTable.setCellspacing(0);

    topTable.add(lInfo,1,1);
    topTable.add(lRecord,2,1);
    topTable.add(lInterviews,3,1);
    topTable.add(lStatistics,4,1);
    //topTable.add(lPictures,5,1);
    topTable.add(lHome,6,1);

    //topTable.add(iRecord,2,1);
    //topTable.add(iInterviews,3,1);
    //topTable.add(iStatistics,4,1);
    topTable.add(iPictures,5,1);

    topTable.setWidth(1,1,"20");
    topTable.setWidth(2,1,"20");
    topTable.setWidth(3,1,"20");
    topTable.setWidth(4,1,"20");
    topTable.setWidth(5,1,"20");
    topTable.setWidth(6,1,"20");

    topTable.setWidth("100%");
    topTable.setAlignment("left");

    topTable.setVerticalAlignment(1,1,"bottom");
    topTable.setVerticalAlignment(2,1,"bottom");
    topTable.setVerticalAlignment(3,1,"bottom");
    topTable.setVerticalAlignment(4,1,"bottom");
    topTable.setVerticalAlignment(5,1,"bottom");
    topTable.setVerticalAlignment(6,1,"bottom");

    topTable.setCellpadding(0);
    topTable.setCellspacing(0);

    lInfo.addParameter(sTopMenuParameterName, sInfoParameterValue);
    lRecord.addParameter(sTopMenuParameterName, sRecordParameterValue);
    lInterviews.addParameter(sTopMenuParameterName, sInterviewsParameterValue);
    lInterviews.addParameter(sTopMenuParameterName, sSubmitParameterValue);
    lStatistics.addParameter(sTopMenuParameterName, sStatisticsParameterValue);
    lPictures.addParameter(sTopMenuParameterName, sPicturesParameterValue);
    lHome.addParameter(sTopMenuParameterName, sHomeParameterValue);

    add(topTable);
  }

  public void main(IWContext iwc) throws SQLException{
    super.main(iwc);
    setLinkMenu();
  }
}
