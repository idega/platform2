package is.idega.idegaweb.project.presentation;

import is.idega.idegaweb.project.business.ProjectBusiness;

import java.rmi.RemoteException;
import java.util.List;

import com.idega.builder.business.BuilderLogic;
import com.idega.core.builder.business.BuilderService;
import com.idega.core.builder.business.BuilderServiceFactory;
import com.idega.data.IDOLegacyEntity;

import com.idega.presentation.Block;
import com.idega.presentation.IFrameContainer;
import com.idega.presentation.IFrameContent;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.IFrame;


/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */
public class ProjectFilter extends Block implements IFrameContainer{

  ProjectFilterContent PFcontent = null;
  boolean UseIFrame = true;
  boolean UseIFrameLastValue = false;
  boolean nameSet = false;
  IFrame iframe = null;
  int IFrameWithSubtraction = 16;
  public static final String _PRM_PROJECT_CATEGORY_ID = EntityNavigationList._SELECTED_ENTITY_ID;
  public static final String _PRM_CAT_TYPE_ID = "ip_cat_type_id";

  public ProjectFilter(){
    super();
    PFcontent = new ProjectFilterContent();
    PFcontent.setOwnerInstance(this);
    iframe = new IFrame();
    this.setName("project_filter");
    nameSet = false;
    iframe.setBorder(0);
    this.setWidth("180");
    this.setHeight("180");
  }

  public PresentationObject getIFrameContent(){
    return PFcontent;
  }

  public String getBundleIdentifier(){
    return ProjectBusiness.IW_PROJECT_IDENTIFIER;
  }

  public void configIFrameContent(){
    if(UseIFrame){
      //if(!UseIFrameLastValue){
        String scrolling = iframe.getScrolling();
        if(!( scrolling != null && !scrolling.equals(IFrame.SCROLLING_YES))){
          try {
            PFcontent.setWidth(Integer.toString(Integer.parseInt(iframe.getWidth())-IFrameWithSubtraction));
          }
          catch (NumberFormatException ex) {
            PFcontent.setWidth("100%");
          }
        }
      //}
    } else {
      PFcontent.setWidth(iframe.getWidth());
    }
  }


  public synchronized Object clone(){
    ProjectFilter obj = (ProjectFilter)super.clone();
    if(PFcontent != null){
      obj.PFcontent = (ProjectFilterContent)this.PFcontent.clone();
      obj.PFcontent.setOwnerInstance(obj);
    }
    obj.UseIFrame = this.UseIFrame;
    obj.UseIFrameLastValue = this.UseIFrameLastValue;
    obj.nameSet = this.nameSet;
    if(iframe != null){
      obj.iframe = (IFrame)this.iframe.clone();
    }
    obj.IFrameWithSubtraction = this.IFrameWithSubtraction;

    return obj;
  }


  public void main(IWContext iwc) throws Exception {
    this.empty();
    PFcontent.setOwnerInstance(this);
    if(UseIFrame){
      if(!nameSet){
        iframe.setName(iframe.getName()+"_"+this.getICObjectInstanceID());
      }
//      if(!UseIFrameLastValue){
//        String scrolling = iframe.getScrolling();
//        if(!( scrolling != null && !scrolling.equals(IFrame.SCROLLING_YES))){
//          try {
//            PFcontent.setWidth(Integer.toString(Integer.parseInt(iframe.getWidth())-IFrameWithSubtraction));
//          }
//          catch (NumberFormatException ex) {
//            PFcontent.setWidth("100%");
//          }
//        }
//      }
//      try {
//        int ibPageId = Integer.parseInt(iwc.getParameter(BuilderLogic.IB_PAGE_PARAMETER));
        iframe.setSrc(BuilderLogic.getInstance().getIFrameContentURL(iwc,this.getICObjectInstanceID()));
/*      }
      catch (NumberFormatException ex) {
        //int ibPageId = BuilderLogic.getInstance().getCurrentIBXMLPage(iwc).getPopulatedPage().getPageID();
        iframe.setSrc(BuilderLogic.getIFrameContentURL(iwc,this.getICObjectInstanceID()));
      }
 */
      this.add(iframe);
      UseIFrameLastValue=UseIFrame;
    } else {
      if(UseIFrameLastValue){
        PFcontent.setWidth(iframe.getWidth());
      }
      this.add(PFcontent);
    }
    UseIFrameLastValue=UseIFrame;
  }

  public void addListener(String listenerKey){
    PFcontent.setListeners(listenerKey);
  }

  public void setBorder(int border){
    IFrameWithSubtraction += ((border - iframe.getBorder())*2);
    iframe.setBorder(border);
  }

  public void setName(String name){
    iframe.setName(name);
    PFcontent.setName(name);
    super.setName(name);
    nameSet = true;
  }

  public void setWidth(String width){
    iframe.setWidth(width);
    //PFcontent.setWidth(width);
    configIFrameContent();
  }

  public void setHeight(String height){
    iframe.setHeight(height);
  }

  public void setToUseIFrame(boolean value){
    UseIFrame = value;
    PFcontent.setIsInIFrame(value);
    UseIFrameLastValue = !value;
    configIFrameContent();
  }

  public void setCategoryTypeId(int id){
    PFcontent.setCategoryTypeId(id);
  }


  public void setSebraColor(String color1, String color2){
    PFcontent.setSebraColor(color1,color2);
  }

  public void setRowColor(String color){
    PFcontent.setRowColor(color);
  }

  public void setLineColor(String color){
    PFcontent.setLineColor(color);
  }

  public void setRowHeight(String rowHeight){
    PFcontent.setRowHeight(rowHeight);
  }

  public void setSelectedColor(String color){
    PFcontent.setSelectedColor(color);
  }

  public void setMinimumNumberOfRows(int number){
    PFcontent.setMinimumNumberOfRows(number);
  }

  public void setIFrameTarget(int instanceId){
    PFcontent.setIFrameTarget(instanceId);
  }

  public String changeState(PresentationObject source, IWContext iwc){
    if(this.equals(source)){
      return PFcontent.changeState(PFcontent,iwc);
    }else{
      return PFcontent.changeState(source,iwc);
    }
  }



  public class ProjectFilterContent extends EntityNavigationList implements IFrameContent{

    int categoryTypeId = 1;
    int targetInstanceId = 0;
    int parentPageId = 0;
    String url = null;
    String targetName = null;
    PresentationObject ownerInstance = null;
    boolean isInIFrame = true;
    String listenerString = null;

    ProjectBusiness business = null;

    public ProjectFilterContent() {
      super();
      this.setAddLinkBefore(true);
    }

    public String getBundleIdentifier(){
      return ProjectBusiness.IW_PROJECT_IDENTIFIER;
    }

    public void setOwnerInstance(PresentationObject obj){
      ownerInstance = obj;
    }

    public PresentationObject getOwnerInstance(){
      return ownerInstance;
    }


    public void setCategoryTypeId(int id){
      categoryTypeId = id;
    }

    public void setIsInIFrame(boolean value){
      this.isInIFrame = value;
    }

    public boolean isInIFrame(){
      return this.isInIFrame;
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

    public void setIFrameTarget(int instanceId){
      targetInstanceId = instanceId;
    }

    public void initColumns(IWContext iwc) throws Exception {
      super.addLinkEntityColumn(is.idega.idegaweb.project.data.IPCategoryBMPBean._COLUMN_NAME);
    }
/*
    public String getURl(IWContext iwc){
      if(url == null){
        if(targetInstanceId != 0){
          url = BuilderLogic.getInstance().getIFrameContentURL(iwc,targetInstanceId);
          try {
            parentPageId = Integer.parseInt(iwc.getParameter(BuilderLogic.IB_PAGE_PARAMETER));
          }
          catch (NumberFormatException ex) {
            parentPageId = BuilderLogic.getInstance().getCurrentIBXMLPage(iwc).getPopulatedPage().getPageID();
          }
          if(parentPageId != 0){
            PresentationObject ob = BuilderLogic.getInstance().getIFrameContent(parentPageId,targetInstanceId,iwc);
            if(ob != null){
              System.err.println("get its name = "+ob.getName());
              targetName = ob.getName();
            }
          }
        }
      }
      return url;
    }
*/
    public void setListeners(String listenerString){
      this.listenerString = listenerString;
    }

    protected void addParameters(IWContext iwc, IDOLegacyEntity item, Link link){
      super.addParameters(iwc,item,link);
      link.addParameter(_PRM_CAT_TYPE_ID, this.categoryTypeId);

      link.addIWPOListener(this.getOwnerInstance());

      if(listenerString != null){
        link.addIWPOListener(listenerString);
      }

      if(isInIFrame()){
        link.setTarget(Link.TARGET_PARENT_WINDOW);
        //link.setURL(IWMainApplication.BUILDER_SERVLET_URL);
        BuilderService bs;
		try {
			bs = BuilderServiceFactory.getBuilderService(iwc);
			link.setURL(bs.getCurrentPageURI(iwc),true,true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }

    }

    public List getEntityList(IWContext iwc) throws Exception{
      if(business == null){
        business = ProjectBusiness.getInstance();
      }
      return business.getCategories(categoryTypeId);
    }

    public synchronized Object clone(){
      ProjectFilterContent obj = (ProjectFilterContent)super.clone();
      obj.business = ProjectBusiness.getInstance();
      obj.categoryTypeId = this.categoryTypeId;
      obj.targetInstanceId = this.targetInstanceId;
      obj.parentPageId = this.parentPageId;
      obj.url = this.url;
      obj.targetName = this.targetName;
      /*
      if(ownerInstance != null){
        obj.ownerInstance = (PresentationObject)this.ownerInstance.clone();
      }
      */
      obj.isInIFrame = this.isInIFrame;

      return obj;
    }

    public void main(IWContext iwc) throws Exception {
      super.main(iwc);
      this.url = null;
    }



  }

}
