/*
 * $Id: TextControl.java,v 1.7 2004/05/24 14:21:40 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.campus.presentation;


import java.sql.SQLException;
import java.util.Hashtable;

import com.idega.block.text.data.TxText;
import com.idega.block.text.presentation.TextReader;
import com.idega.data.genericentity.Member;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;

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

	 private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";


  public TextControl(){
    MiddleColor = "#9FA9B3";
    LightColor = "#D7DADF";
    DarkColor = "#27324B";
  }

	public String getBundleIdentifier(){
	  return IW_BUNDLE_IDENTIFIER;
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
      int numberOfTexts = 14;

    try {
      for ( int a = 5; a < numberOfTexts; a++ ) {
        try {
          TxText text = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).findByPrimaryKeyLegacy(a);
        }
        catch (SQLException e) {
          TxText text = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).createLegacy();
            text.setID(a);
            text.setDefaultValues();
            text.insert();
        }

        TextReader texti = new TextReader(a);
          texti.setWidth("100%");
          texti.setEnableDelete(false);

          T.add(texti,1,1);
          if ( a + 1 < numberOfTexts ) {
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
        TxText text = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).findByPrimaryKeyLegacy(action);
      }
      catch (SQLException e) {
        TxText text = ((com.idega.block.text.data.TxTextHome)com.idega.data.IDOLookup.getHomeLegacy(TxText.class)).createLegacy();
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

      isAdmin = iwc.hasEditPermission(this);

    /** @todo fixa Admin*/
    control(iwc);
  }
}// class PriceCatalogueMaker


