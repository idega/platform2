package is.idega.idegaweb.project.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.Table;
import com.idega.presentation.ui.IFrame;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import is.idega.idegaweb.project.business.ProjectBusiness;
import is.idega.idegaweb.project.data.IPProject;
import com.idega.data.GenericEntity;
import com.idega.builder.dynamicpagetrigger.data.PageLink;
import com.idega.presentation.ui.IFrame;
import com.idega.idegaweb.IWMainApplication;
import com.idega.builder.business.BuilderLogic;
import com.idega.presentation.IFrameContainer;


import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Enumeration;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ProjectNavigator extends Block implements IFrameContainer{

  ProjectNavigatorContent PNcontent = null;
  boolean UseIFrame = true;
  boolean UseIFrameLastValue = false;
  boolean nameSet = false;
  IFrame iframe = null;
  int IFrameWithSubtraction = 16;


  public ProjectNavigator(){
    super();
    PNcontent = new ProjectNavigatorContent();
    iframe = new IFrame();
    this.setName("project_navigator");
    nameSet = false;
    iframe.setBorder(0);
    this.setWidth("180");
    this.setHeight("180");
  }

  public PresentationObject getIFrameContent(){
    return PNcontent;
  }

  public void main(IWContext iwc) throws Exception {
    this.empty();
    if(UseIFrame){
      if(!nameSet){
        iframe.setName(iframe.getName()+"_"+this.getICObjectInstanceID());
      }
      if(!UseIFrameLastValue){
        String scrolling = iframe.getScrolling();
        if(!( scrolling != null && !scrolling.equals(IFrame.SCROLLING_YES))){
          try {
            PNcontent.setWidth(Integer.toString(Integer.parseInt(iframe.getWidth())-IFrameWithSubtraction));
          }
          catch (NumberFormatException ex) {
            PNcontent.setWidth("100%");
          }
        }
      }
      try {
        int ibPageId = Integer.parseInt(iwc.getParameter(BuilderLogic.IB_PAGE_PARAMETER));
        iframe.setSrc(BuilderLogic.getIFrameContentURL(this.getICObjectInstanceID(),ibPageId));
      }
      catch (NumberFormatException ex) {
        int ibPageId = BuilderLogic.getInstance().getCurrentIBXMLPage(iwc).getPopulatedPage().getPageID();
        iframe.setSrc(BuilderLogic.getIFrameContentURL(this.getICObjectInstanceID(),ibPageId));
      }
      this.add(iframe);
      UseIFrameLastValue=UseIFrame;
    } else {
      if(UseIFrameLastValue){
        PNcontent.setWidth(iframe.getWidth());
      }
      this.add(PNcontent);
    }
    UseIFrameLastValue=UseIFrame;
  }

  public void setBorder(int border){
    IFrameWithSubtraction += ((border - iframe.getBorder())*2);
    iframe.setBorder(border);
  }

  public void setName(String name){
    iframe.setName(name);
    PNcontent.setName(name);
    super.setName(name);
    nameSet = true;
  }

  public void setWidth(String width){
    iframe.setWidth(width);
    PNcontent.setWidth(width);
  }

  public void setHeight(String height){
    iframe.setHeight(height);
  }

  public void setToUseIFrame(boolean value){
    UseIFrame = value;
    UseIFrameLastValue = !value;
  }



  public void setSebraColor(String color1, String color2){
    PNcontent.setSebraColor(color1,color2);
  }

  public void setRowColor(String color){
    PNcontent.setRowColor(color);
  }

  public void setLineColor(String color){
    PNcontent.setLineColor(color);
  }

  public void setRowHeight(String rowHeight){
    PNcontent.setRowHeight(rowHeight);
  }

  public void setSelectedColor(String color){
    PNcontent.setSelectedColor(color);
  }

  public void setMinimumNumberOfRows(int number){
    PNcontent.setMinimumNumberOfRows(number);
  }

  public void setIFrameTarget(int instanceId, int pageId){
    PNcontent.setIFrameTarget(instanceId,pageId);
  }



  public class ProjectNavigatorContent extends EntityNavigationList {

    int targetInstanceId = 0;
    int parentPageId = 0;

    public ProjectNavigatorContent() {
      super();
    }



    public void setSebraColor(String color1, String color2){
      super.setSebraColor(color1,color2);
    }

    public void setRowColor(String color){
      super.setRowColor(color);
    }

    public void setLineColor(String color){
      super.setLineColor(color);
    }

    public void setWidth(String width){
      super.setWidth(width);
    }

    public void setRowHeight(String rowHeight){
      super.setRowHeight(rowHeight);
    }

    public void setSelectedColor(String color){
      super.setSelectedColor(color);
    }

    public void setMinimumNumberOfRows(int number){
      super.setMinimumNumberOfRows(number);
    }

    public void setIFrameTarget(int instanceId, int pageId){
      targetInstanceId = instanceId;
      parentPageId = pageId;
    }

    public List getEntityList(IWContext iwc) throws Exception{
      /*
      Enumeration e = iwc.getParameterNames();
      System.err.println(this.getName()+" parameters");
      while (e.hasMoreElements()) {
        String prm = (String)e.nextElement();
        System.err.println("prm "+prm+" = "+iwc.getParameter(prm));
      }
      */
      return business.getProjectDPTPageLinks();
    }

    public void initColumns(IWContext iwc) throws Exception{
      super.addLinkEntityColumn(PageLink._COLUMNNAME_DEFAULT_LINK_TEXT);
    }

    protected void addParameters(IWContext iwc, GenericEntity item, Link link){
      link.addParameter(com.idega.builder.business.BuilderLogic.IB_PAGE_PARAMETER,((PageLink)item).getPageId());
      link.setTarget(Link.TARGET_PARENT_WINDOW);
    }

  }



/*
public class ProjectNavigator extends EntityNavigationList {

  public ProjectNavigator() {
    super();
    setWidth("164");
    setMinimumNumberOfRows(12);
  }

  public void setSebraColor(String color1, String color2){
    super.setSebraColor(color1,color2);
  }

  public void setRowColor(String color){
    super.setRowColor(color);
  }

  public void setLineColor(String color){
    super.setLineColor(color);
  }

  public void setWidth(String width){
    super.setWidth(width);
  }

  public void setRowHeight(String rowHeight){
    super.setRowHeight(rowHeight);
  }

  public void setSelectedColor(String color){
    super.setSelectedColor(color);
  }

  public void setMinimumNumberOfRows(int number){
    super.setMinimumNumberOfRows(number);
  }





  public List getEntityList(IWContext iwc) throws Exception{
    return business.getProjectDPTPageLinks();
  }

  public void initColumns(IWContext iwc) throws Exception{
    super.addLinkEntityColumn(PageLink._COLUMNNAME_DEFAULT_LINK_TEXT);
  }

  protected void addParameters(IWContext iwc, GenericEntity item, Link link){
    link.addParameter(com.idega.builder.business.BuilderLogic.IB_PAGE_PARAMETER,((PageLink)item).getPageId());
    link.setTarget(Link.TARGET_PARENT_WINDOW);
  }
*/

}

