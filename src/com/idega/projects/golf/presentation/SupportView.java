package com.idega.projects.golf.presentation;

import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.Image;
import java.lang.String;
import java.sql.SQLException;
import com.idega.jmodule.object.textObject.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.projects.golf.entity.GolferPageData;
import com.idega.projects.golf.HandicapOverview;
import com.idega.jmodule.text.presentation.TextReader;
import com.idega.projects.golf.entity.Member;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public class SupportView extends GolferJModuleObject implements LinkParameters{

  public SupportView() {
  }

  //STUÐNINGSAÐILAR
  private void setSupportView(ModuleInfo modinfo) throws SQLException{
    //Image iInterviewsLogo = iwrb.getImage("/golferpage/velkomin.gif");
    //this.addLeftLogo(iInterviewsLogo);

    Table interviewsTable = new Table(3,1);
    interviewsTable.setWidth("100%");
    interviewsTable.setCellpadding(0);
    interviewsTable.setCellspacing(0);
    Image dotLineBackgroundImage;
    dotLineBackgroundImage = iwb.getImage("shared/brotalina.gif");
    interviewsTable.setBackgroundImage(2,1,dotLineBackgroundImage);
    interviewsTable.setWidth(1,1,"410");
    interviewsTable.setWidth(2,1,"1");
    interviewsTable.add(Text.emptyString(),2,1);
    interviewsTable.setVerticalAlignment(1,1,"top");

    TextReader supportText = new TextReader(this.golferPageData.getSupportesID());
    supportText.setTextStyle(Text.FONT_FACE_VERDANA);
    supportText.setAlignment("center");
    supportText.setHeadlineColor("FF6000");
    supportText.setHeadlineSize(2);
    interviewsTable.setVerticalAlignment(3,1,"top");
    interviewsTable.setAlignment( 3, 1, "center");
    interviewsTable.add(supportText,3,1);
    Member member = new Member(member_id);

    String fullName = member.getFirstName()+" "+member.getMiddleName()+" "+member.getLastName();

    /**@todo Set the golfers (eignarfall) name in the database till then I'll use as a sessionattribute.
     * */

    //String name =

    GolferFriendsSigningSheet golferFriendsSigningSheet = new GolferFriendsSigningSheet(this.golferPageData.getSupportPreSigningID(),
      (String) modinfo.getSessionAttribute("golferName"), sTopMenuParameterName,sInterviewsParameterValue,
      sSubmitParameterValue, fullName);
    interviewsTable.add(golferFriendsSigningSheet,1,1);
    add(interviewsTable);
  }

  public void main(ModuleInfo modinfo) throws SQLException{
    super.main(modinfo);
    if (modinfo.getParameter(LinkParameters.sTopMenuParameterName).equalsIgnoreCase(LinkParameters.sInterviewsParameterValue)) {
      setSupportView(modinfo);
    }
  }
}