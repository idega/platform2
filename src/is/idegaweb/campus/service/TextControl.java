/*
 * $Id: TextControl.java,v 1.5 2001/08/21 13:53:32 laddi Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idegaweb.campus.service;
//test
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.login.business.AccessControl;
import com.idega.data.genericentity.Member;
import com.idega.data.genericentity.Group;
import com.idega.block.text.presentation.TextReader;
import com.idega.block.text.data.TextModule;
import java.util.Hashtable;
import java.sql.SQLException;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class TextControl extends JModuleObject {

  private String LightColor,MiddleColor,DarkColor;
  private String action;
  public static final String strAction = "text_action";
  private Member eMember;
  private ModuleObject Tabs;
  private CampusObject CampObj;
  private Hashtable PermissionHash;
  private boolean isAdmin;
  private String sAct;
  private int iAct;
  private final int NOACT = 0;


  public TextControl(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

  public ModuleObject getTabs(){
    return Tabs;
  }

  private void control(ModuleInfo modinfo){

    try{


      if(modinfo.getParameter(strAction) == null){
        iAct = NOACT;
      }
      else {
        sAct = modinfo.getParameter(strAction);
        iAct = Integer.parseInt(sAct);
      }

      if ( iAct == NOACT ) {
        doMenu();
      }
      else {
        doText(iAct);
      }

    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void doMenu(){
    Table T = new Table(2,1);
      T.setWidth("100%");
      T.setBorder(0);
      T.setWidth(1,"500");
      T.setAlignment(2,1,"center");
      T.setVerticalAlignment(1,1,"top");
      T.setVerticalAlignment(2,1,"top");

    try {
      for ( int a = 5; a < 13; a++ ) {
        TextModule text = new TextModule(a);
        if ( text.getTextHeadline() == null || text.getIncludeImage() == null ) {
            text.setDefaultValues();
            text.insert();
        }

        TextReader texti = new TextReader(a);
          texti.setWidth("100%");
          texti.setEnableDelete(false);

          T.add(texti,1,1);
          if ( a + 1 < 13 ) {
            T.addBreak(1,1);
          }
      }

      Image textImage = new Image("/pics/text_pictures/info.jpg");
        textImage.setVerticalSpacing(12);
        T.add(textImage,2,1);

    }
    catch (SQLException e) {
      e.printStackTrace(System.err);
    }

    add(T);
  }

  public void doText(int action) {
    try {
      Table T = new Table(2,1);
        T.setWidth("100%");
        T.setBorder(0);
        T.setWidth(1,"500");
        T.setAlignment(2,1,"center");
        T.setVerticalAlignment(1,1,"top");
        T.setVerticalAlignment(2,1,"top");

      TextModule text = new TextModule(action);

      Image textImage = new Image("/pics/text_pictures/picture"+Integer.toString(action)+".jpg");
        textImage.setVerticalSpacing(12);

      if ( text.getTextHeadline() == null || text.getIncludeImage() == null ) {
        text.setDefaultValues();
        text.insert();
      }

      TextReader textReader = new TextReader(action);
        textReader.setEnableDelete(false);
        textReader.setTableWidth("400");

      T.add(textReader,1,1);
      T.add(textImage,2,1);
      add(T);
    }
    catch (Exception e) {
      e.printStackTrace(System.err);
    }
  }

  public void main(ModuleInfo modinfo)  {
    try{
      isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(modinfo);
    }
    catch (SQLException sql) {
      isAdmin = false;
    }
    /** @todo fixa Admin*/
    control(modinfo);
  }
}// class PriceCatalogueMaker


