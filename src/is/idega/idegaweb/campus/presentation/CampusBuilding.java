/*

 * $Id: CampusBuilding.java,v 1.5 2004/05/24 14:21:40 palli Exp $

 *

 * Copyright (C) 2001 Idega hf. All Rights Reserved.

 *

 * This software is the proprietary information of Idega hf.

 * Use is subject to license terms.

 *

 */

package is.idega.idegaweb.campus.presentation;





import is.idega.idegaweb.campus.block.building.presentation.ApartmentFreezer;
import is.idega.idegaweb.campus.block.building.presentation.ApartmentSerie;
import is.idega.idegaweb.campus.block.phone.presentation.CampusPhones;

import com.idega.block.building.presentation.BuildingEditorWindow;
import com.idega.block.building.presentation.BuildingStatistics;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.IFrame;



/**

 *

 * @author <a href="mailto:aron@idega.is">aron@idega.is</a>

 * @version 1.0

 */

public class CampusBuilding extends Block {



  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";

  public final static String FRAME_NAME = "cbu_rightFrame";

  protected IWResourceBundle iwrb;

  protected IWBundle iwb;



  public CampusBuilding() {

  }



  public void main(IWContext iwc){

    iwrb = getResourceBundle(iwc);

    iwb = getBundle(iwc);

    Table myTable = new Table(2,2);

      myTable.setBorderColor("#000000");

      myTable.mergeCells(2,1,2,2);

      myTable.setWidth(1,"130");

      myTable.setWidth(2,"100%");

      myTable.setCellpadding(3);

      myTable.setWidth("100%");

      myTable.setHeight("100%");

      myTable.setColumnAlignment(1,"left");



      myTable.setBorder(0);

      myTable.setVerticalAlignment(1,1,"top");

      myTable.setVerticalAlignment(2,1,"top");

      myTable.setVerticalAlignment(1,2,"top");

/*

    IFrame iFrame = new IFrame("menuFrame");

      iFrame.setSrc(CampusBuildingMenu.class);

      iFrame.setWidth(120);

      iFrame.setHeight(200);

      iFrame.setBorder(IFrame.FRAMEBORDER_OFF);

      iFrame.setScrolling(IFrame.SCROLLING_YES);

      iFrame.setStyle("border: 1 solid #000000");

      //iFrame.setAlignment(IFrame.ALIGN_LEFT);

      myTable.add(iFrame,1,1);

*/

    myTable.add(getLinkTable(),1,1);

    IFrame iFrame2 = new IFrame(FRAME_NAME);

      iFrame2.setWidth("100%");

      iFrame2.setHeight("100%");

      iFrame2.setBorder(IFrame.FRAMEBORDER_OFF);

      iFrame2.setScrolling(IFrame.SCROLLING_YES);

      iFrame2.setAlignment(IFrame.ALIGN_LEFT);

      iFrame2.setStyle("border: 1 solid #000000");

			iFrame2.setSrc(BuildingStatistics.class);

      myTable.add(iFrame2,2,1);



    add(myTable);

  }



  public PresentationObject getLinkTable(){

      Table T = new Table();

      Link Build = new Link(new Text(iwrb.getLocalizedString("building_editor","Building Editor")));

      Build.setWindowToOpen(BuildingEditorWindow.class);



      T.add(Build,1,1);



      T.add(getLink( ApartmentFreezer.class,iwrb.getLocalizedString("apartment_freeze","Freeze apartments"),CampusBuilding.FRAME_NAME),1,2);

      T.add(getLink(ApartmentSerie.class,iwrb.getLocalizedString("apartment_serie","Apartment serie"),CampusBuilding.FRAME_NAME),1,3);

      T.add(getLink(CampusPhones.class,iwrb.getLocalizedString("apartment_phones","Phones"),CampusBuilding.FRAME_NAME),1,4);

      return T;

  }



   public Link getLink(Class cl,String name,String target){

    Link L = new Link(name,cl);

    L.setTarget(target );

    L.setFontSize(2);

    return L;

  }



  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }



}

