/*
 * $Id: TextControl.java,v 1.2 2001/11/08 19:00:27 aron Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;


import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.presentation.IWContext;
import com.idega.jmodule.login.business.AccessControl;
import com.idega.data.genericentity.Member;
import com.idega.data.genericentity.Group;
import com.idega.block.text.presentation.TextReader;
import com.idega.block.text.data.TxText;
import java.util.Hashtable;
import java.sql.SQLException;
import java.io.IOException;
import com.idega.idegaweb.IWBundle;

/**
 *
 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>
 * @version 1.0
 */
public class TextControl extends Block {

  private String LightColor,MiddleColor,DarkColor;
  private String action;
  public static final String strAction = "text_action";
  private Member eMember;
  private PresentationObject Tabs;
  private Hashtable PermissionHash;
  private boolean isAdmin;
  private String sAct;
  private int iAct;
  private final int NOACT = 0;
	private IWBundle iwb;


  public TextControl(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

  public PresentationObject getTabs(){
    return Tabs;
  }

  private void control(IWContext iwc){
		iwb = getBundle(iwc);
    try{


      if(iwc.getParameter(strAction) == null){
        iAct = NOACT;
      }
      else {
        sAct = iwc.getParameter(strAction);
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
        try {
          TxText text = new TxText(a);
        }
        catch (SQLException e) {
          TxText text = new TxText();
            text.setID(a);
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

      Image textImage = iwb.getImage("/text_pictures/info.jpg");
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

      Image textImage = iwb.getImage("/text_pictures/picture"+Integer.toString(action)+".jpg");
        textImage.setVerticalSpacing(12);

      try {
        TxText text = new TxText(action);
      }
      catch (SQLException e) {
        TxText text = new TxText();
          text.setID(action);
          //text.setDefaultValues();
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

  public void main(IWContext iwc)  {
    try{
      isAdmin = com.idega.jmodule.login.business.AccessControl.isAdmin(iwc);
    }
    catch (SQLException sql) {
      isAdmin = false;
    }
    /** @todo fixa Admin*/
    control(iwc);
  }
}// class PriceCatalogueMaker


