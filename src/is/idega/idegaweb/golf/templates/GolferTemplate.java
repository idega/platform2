package is.idega.idegaweb.golf.templates;

import com.idega.jmodule.JSPModule;
import is.idega.idegaweb.golf.templates.page.GolfersPage;
import javax.servlet.jsp.JspPage;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Image;

/**
 * Title:        idegaWeb Classes
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <a href="bjarni@idega.is">Bjarni Viljhalmsson</a>
 * @version 1.0
 */

public abstract class GolferTemplate extends JSPModule implements JspPage{
  public void initializePage(){
    setPage(new GolfersPage());
  }

  public GolfersPage getGolferPage(){
    return (GolfersPage) this.getPage();
  }

  public void add(PresentationObject objectToAdd){
    getGolferPage().add(objectToAdd);
  }

  public void add(String stringToAdd){
    getGolferPage().add(stringToAdd);
  }

  public void addFooter(PresentationObject objectToAdd){
    getGolferPage().addFooter(objectToAdd);
  }

  public void addFooter(String stringToAdd){
    getGolferPage().addFooter(stringToAdd);
  }

  public void addLeftTopBanner(String stringToAdd){
    getGolferPage().addLeftTopBanner( stringToAdd);
  }

  public void addLeftTopBanner(PresentationObject objectToAdd){
    getGolferPage().addLeftTopBanner(objectToAdd);
  }

  public void addRightTopBanner(String stringToAdd){
    getGolferPage().addRightTopBanner( stringToAdd);
  }

  public void addRightTopBanner(PresentationObject objectToAdd){
    getGolferPage().addRightTopBanner(objectToAdd);
  }

  public void addCenterTopBanner(PresentationObject objectToAdd){
    getGolferPage().addCenterTopBanner(objectToAdd);
  }

  public void addCenterTopBanner(String stringToAdd){
    getGolferPage().addCenterTopBanner( stringToAdd);
  }

  public void addLeftLogo(PresentationObject objectToAdd){
    getGolferPage().addLeftLogo( objectToAdd);
  }

  public void addLeftLogo(String stringToAdd){
    getGolferPage().addLeftLogo( stringToAdd);
  }

  public void addLeftBanners(PresentationObject objectToAdd){
    getGolferPage().addLeftBanners(objectToAdd);
  }

  public void addLeftBanners(String stringToAdd){
    getGolferPage().addLeftBanners( stringToAdd);
  }

  public void addLeftLink(PresentationObject objectToAdd){
    getGolferPage().addLeftLink(objectToAdd);
  }

  public void addLeftLink(String stringToAdd){
    getGolferPage().addLeftLink(stringToAdd);
  }

  public void addCornerLogo(PresentationObject objectToAdd){
    getGolferPage().addCornerLogo(objectToAdd);
  }

  public void addCornerLogo(String stringToAdd){
    getGolferPage().addCornerLogo(stringToAdd);
  }

  public void addMenuLinks(PresentationObject objectToAdd){
    getGolferPage().addMenuLinks(objectToAdd);
  }

  public void addMenuLinks(String stringToAdd){
    getGolferPage().addMenuLinks(stringToAdd);
  }

  public void addSideBannerImage(String sideBannerImageIWBundleUrl){
    getGolferPage().addSideBannerImage(sideBannerImageIWBundleUrl);
  }

  public void addRightTopImage(String rightTopImageIWBundleUrl){
    getGolferPage().addRightTopImage(rightTopImageIWBundleUrl);
  }

  public void addCenterTopImage(String centerTopImageIWBundleUrl){
    getGolferPage().addCenterTopImage(centerTopImageIWBundleUrl);
  }

  public void addLeftTopImage(String leftTopImageIWBundleUrl){
    getGolferPage().addLeftTopImage(leftTopImageIWBundleUrl);
  }

  private void addCornerLogoImage(String cornerLogoImageUrlInBundle, int cornerLogoImageWidth, int cornerLogoImageHeight){
    getGolferPage().addCornerLogoImage(cornerLogoImageUrlInBundle,cornerLogoImageWidth,cornerLogoImageHeight);
  }
  private void addCornerLogoImage(String cornerLogoImageUrlInBundle){
    getGolferPage().addCornerLogoImage(cornerLogoImageUrlInBundle);
  }
}
