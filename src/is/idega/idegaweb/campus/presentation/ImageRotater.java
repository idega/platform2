package is.idega.idegaweb.campus.presentation;







import com.idega.presentation.Block;

import com.idega.presentation.Image;

import com.idega.presentation.IWContext;

import com.idega.idegaweb.IWResourceBundle;

import com.idega.idegaweb.IWBundle;



public class ImageRotater extends Block {



	private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";

	protected IWResourceBundle iwrb;

	protected IWBundle iwb;

	private int width = 130;

	private int height = 79;



  public void main(IWContext iwc) {

    iwrb = getResourceBundle(iwc);

    iwb = getBundle(iwc);



    int num = (int) (Math.random() * 14) ;

    Image image = iwb.getImage("/template/face/face"+(num)+".jpg");

    image.setWidth(width) ;

    image.setHeight(height) ;

    add(image) ;

  }



  public String getBundleIdentifier(){

    return IW_BUNDLE_IDENTIFIER;

  }



}// class TitleIcons

