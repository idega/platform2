package is.idega.idegaweb.campus.presentation;

import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.data.ICPage;
import com.idega.core.data.ICTreeNode;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;

/**
 * Title: Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * @version 1.0
 */

public class CampusTab extends CampusBlock {

  Link tabLink;
  Image UpImage;
  Image DownImage;
  String style;
  int pageId = -1;

  int upWidth;
  int downWidth;
  int upHeight;
  int downHeight;
  Table container;


  public CampusTab() {
    container = new Table(1,1);
    container.setCellpadding(0);
    container.setCellspacing(0);
    tabLink = new Link();
  }

  public void setLocalizedText(String localeString,String text){
    tabLink.setLocalizedText(localeString,text);
  }

  public void setDownImage(Image image){
    this.DownImage = image;
  }

  public void setUpImage(Image image){
    this.UpImage = image;
  }


  public void setPage(ICPage page){
    pageId = page.getID();
  }

  public void setStyle(String style){
    this.style = style;
  }

  public void setDownWidth(int width){
    this.downWidth = width;
  }
  public void setDownHeight(int height){
    this.downHeight = height;
  }

  public void setUpWidth(int width){
    this.upWidth = width;
  }
  public void setUpHeight(int height){
    this.upHeight = height;
  }

  public void main(IWContext iwc)throws Exception{
    
    BuilderService bservice = getBuilderService(iwc);
    int sessId=bservice.getCurrentPageId(iwc);
    boolean upTab = false;
    if(pageId == sessId)
      upTab = true;
    else{
      ICTreeNode node = bservice.getPageTree(sessId,iwc.getCurrentUserId());
      if(node.getParentNode()!=null && node.getParentNode().getNodeID()== pageId)
        upTab = true;
    }

    if(upTab){
      if(UpImage!=null){
        container.setBackgroundImage(UpImage);
        container.setWidth(upWidth);
        container.setHeight(upHeight);
      }
      else{
        container.setBackgroundImage(getBundle().getImage("/shared/tabup.gif"));
        container.setWidth(77);
        container.setHeight(22);
      }
    }
    else if(DownImage !=null){
      container.setBackgroundImage(DownImage);
      container.setWidth(downWidth);
      container.setHeight(downHeight);
    }
    else{
      container.setBackgroundImage(getBundle().getImage("/shared/tabdn.gif"));
      container.setWidth(76);
      container.setHeight(16);
    }

    if(pageId > 0){
      tabLink.setPage(pageId);

    }

    if(style != null)
      tabLink.setFontStyle(style);
    container.add(tabLink,1,1);
    container.setAlignment(1,1,"center");
    container.setVerticalAlignment(1,1,"top");
    add(container);
  }

  public Object clone() {
    CampusTab obj = null;
    try {
      obj = (CampusTab)super.clone();
      obj.container = (Table)this.container.clone();

    }
    catch(Exception ex) {
      ex.printStackTrace(System.err);
    }
    return obj;
  }


}
