/*
 * $Id: Staffer.java,v 1.2 2001/10/05 08:05:43 tryggvil Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;

import com.idega.data.genericentity.Group;
import com.idega.data.genericentity.Member;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.jmodule.login.business.*;
import java.sql.SQLException;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class Staffer extends CampusObject implements Tabs{

  private String iObjectName = "Tenant";
  private String LightColor,MiddleColor,DarkColor;
  private int iAct;
  private String sAct;
  private static final String strAction = "tenant_action";
  private Member eMember;
  private boolean isAdmin;
  private final int ACT1 = 1, ACT2 = 2, ACT3 = 3, ACT4 = 4;

  public Staffer(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

  private void control(IWContext iwc){

    try{
      eMember = AccessControl.getMember(iwc);

      if(iwc.getParameter(strAction) == null){
        doAct1(iwc);
      }
      if(iwc.getParameter(strAction) != null){
        sAct = iwc.getParameter(strAction);
        iAct = Integer.parseInt(sAct);

        switch (iAct) {
          case ACT1 : doAct1(iwc);        break;
          case ACT2 : doAct2(iwc);        break;
          case ACT3 : doAct3(iwc);        break;
          case ACT4 : doAct4(iwc);        break;
          default: doAct1(iwc);           break;
        }

      }
    }
    catch(SQLException S){	S.printStackTrace();	}
}

    private void doAct1(IWContext iwc) throws SQLException {

    }

    private void doAct2(IWContext iwc) throws SQLException{

    }

    private void doAct3(IWContext iwc) throws SQLException{

    }

    private void doAct4(IWContext iwc) throws SQLException{

    }

    public PresentationObject getTabs(){
      return makeLinkTable();
    }

    private Table makeLinkTable(){
      Table LinkTable = new Table(1,1);
      LinkTable.setBorder(0);
      LinkTable.setCellpadding(0);
      LinkTable.setCellspacing(0);
      LinkTable.setWidth("100%");

      Link Link1 = new Link(new Image(iAct == ACT1?"/pics/tabs/IS/yfirlit.gif":"/pics/tabs/IS/yfirlit1.gif"));
      Link1.addParameter(strAction,ACT1);
      Link Link2 = new Link(new Image(iAct == ACT2?"/pics/tabs/IS/yfirlit.gif":"/pics/tabs/IS/yfirlit1.gif"));
      Link2.addParameter(strAction,ACT2);
      Link Link3 = new Link(new Image(iAct == ACT3?"/pics/tabs/IS/yfirlit.gif":"/pics/tabs/IS/yfirlit1.gif"));
      Link3.addParameter(strAction,ACT3);
      Link Link4 = new Link(new Image(iAct == ACT4?"/pics/tabs/IS/yfirlit.gif":"/pics/tabs/IS/yfirlit1.gif"));
      Link4.addParameter(strAction,ACT4);
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,1,1);
      LinkTable.add(Link3,1,1);
      LinkTable.add(Link4,1,1);

      return LinkTable;
  }

  public void main(IWContext iwc)  {
    try{
    isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(iwc);
    }
    catch(SQLException sql){ isAdmin = false;}
    /** @todo: fixa Admin*/
    control(iwc);
  }
}// class PriceCatalogueMaker


