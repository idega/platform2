package is.idega.idegaweb.golf.presentation;





import is.idega.idegaweb.golf.business.AccessControl;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;



/**

 * Title:        idegaWeb Golf

 * Description:

 * Copyright:    Copyright (c) 2002

 * Company:      idega

 * @author <a href="tryggvi@idega.is">Tryggvi Larusson</a>

 * @version 2.0

 */



public class MiscellenousBox extends Block {



  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.golf";



  public MiscellenousBox(){

  }



  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }



  public boolean isAdminForGolf(IWContext iwc) {

    try {

      return AccessControl.isAdmin(iwc);

    }

    catch (Exception E) {

      E.printStackTrace(System.err);

    }

    finally {

	  }



    return false;

  }





  public void main(IWContext iwc)throws Exception{

      setCacheable("Miscbox",86400000);//24 hours



      //add(getLinks(iwc));

  }





  /*public BoxReader getLinks(IWContext iwc){

          IWResourceBundle iwrb = getBundle(iwc).getResourceBundle(iwc);

          BoxReader box_office= new BoxReader("1",isAdminForGolf(iwc),3);

                  box_office.setBoxBorder(0);

                  box_office.setInnerBoxBorder(0);

                  box_office.setBoxWidth(148);

                  box_office.setNoIcons(true);

                  box_office.setBoxOutline(1);

                  box_office.setOutlineColor("8ab490");

                  box_office.setInBoxColor("FFFFFF");

                  box_office.setNumberOfDisplayed(4);

                  box_office.setNumberOfLetters(60);

                  box_office.setLeftBoxWidth(0);

                  box_office.setRightBoxWidth(0);

                  box_office.setBoxTableColor("8ab490");

                  box_office.setTableAlignment("left");

                  box_office.setBoxPadding(1);

                  box_office.setBoxOnly(true);

                  box_office.setShowCategoryHeadline(true);

                  box_office.setBoxCategoryHeadlineSize(1);

                  box_office.setBoxCategoryHeadlineColor("#FFFFFF");

                  box_office.setLeftHeader(false);

                  box_office.setHeadlineAlign("left");



          return box_office;

  }*/





}
