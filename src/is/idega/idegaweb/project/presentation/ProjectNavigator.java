package is.idega.idegaweb.project.presentation;

import is.idega.idegaweb.project.business.ProjectBusiness;
import is.idega.idegaweb.project.business.ProjectNavigatorState;

import java.util.Iterator;
import java.util.List;

import com.idega.builder.business.BuilderLogic;
import com.idega.builder.dynamicpagetrigger.data.PageLink;
import com.idega.core.accesscontrol.business.AccessController;
import com.idega.data.IDOLegacyEntity;
import com.idega.event.GenericState;
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

public class ProjectNavigator extends Block implements IFrameContainer{

  ProjectNavigatorContent PFcontent = null;
  boolean UseIFrame = true;
  boolean UseIFrameLastValue = false;
  boolean nameSet = false;
  IFrame iframe = null;
  int IFrameWithSubtraction = 16;
  public static final String _PRM_PROJECT_ID = "ip_pid";



  public ProjectNavigator(){
    super();
    PFcontent = new ProjectNavigatorContent();
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
    ProjectNavigator obj = (ProjectNavigator)super.clone();
    if(PFcontent != null){
      obj.PFcontent = (ProjectNavigatorContent)this.PFcontent.clone();
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
        iframe.setSrc(BuilderLogic.getIFrameContentURL(iwc,this.getICObjectInstanceID()));
//      }
//      catch (NumberFormatException ex) {
//        int ibPageId = BuilderLogic.getInstance().getCurrentIBXMLPage(iwc).getPopulatedPage().getPageID();
//        iframe.setSrc(BuilderLogic.getIFrameContentURL(iwc, this.getICObjectInstanceID()));
//      }
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



  public class ProjectNavigatorContent extends EntityNavigationList implements IFrameContent{

    int targetInstanceId = 0;
    int parentPageId = 0;
    String url = null;
    String targetName = null;
    PresentationObject ownerInstance = null;
    boolean isInIFrame = true;
    String listenerString = null;
    ProjectBusiness business = null;

    public ProjectNavigatorContent() {
      super();
    }

    public String getBundleIdentifier(){
      return ProjectBusiness.IW_PROJECT_IDENTIFIER;
    }

    public void initDefaultState(){
      ProjectNavigatorState defState = new ProjectNavigatorState(this);
      defState.setSelectedElementID(-1);
      //defState.addFilter(1,1);
      //defState.addFilter(3,7);
      this.setDefaultState(defState);
    }

    public void setOwnerInstance(PresentationObject obj){
      ownerInstance = obj;
    }

    public PresentationObject getOwnerInstance(){
      return ownerInstance;
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

    public List getEntityList(IWContext iwc) throws Exception{
      ProjectNavigatorState state = (ProjectNavigatorState)this.getState(iwc);

      /*String[] filters = state.getFilters();
      if(filters != null){
        System.err.println(this.getClassName()+" - filter begin");
        for (int i = 0; i < filters.length; i+=2) {
          System.err.println("cattype: "+filters[i]);
          System.err.println("cat: "+filters[i+1]);
        }
        System.err.println(" - filter end");
      }else{
        System.err.println(this.getClassName()+" - filter is null");
      }*/

      if(business == null){
        business = ProjectBusiness.getInstance();
      }
      List l = business.getProjectDPTPageLinks(state.getSelectedCategories());

      // remove projects one has no permission for
      if(l != null){
        Iterator iter = l.iterator();
        while (iter.hasNext()) {
          PageLink item = (PageLink)iter.next();
          boolean remove = !iwc.getAccessController().hasPermission(AccessController.PERMISSION_KEY_VIEW,AccessController.CATEGORY_PAGE_INSTANCE,Integer.toString(item.getPageId()),iwc);
          if(remove){
            iter.remove();
          }
        }
              }
      return l;
    }




    public void initColumns(IWContext iwc) throws Exception{
      super.addLinkEntityColumn(com.idega.builder.dynamicpagetrigger.data.PageLinkBMPBean._COLUMNNAME_DEFAULT_LINK_TEXT);
    }

    public void setListeners(String listenerString){
      this.listenerString = listenerString;
    }

    protected void addParameters(IWContext iwc, IDOLegacyEntity item, Link link){
      super.addParameters(iwc,item,link);
      if(item != null){
        link.addParameter(ProjectNavigator._PRM_PROJECT_ID,((PageLink)item).getReferencedDataId());
        link.addParameter(com.idega.builder.business.BuilderLogic.IB_PAGE_PARAMETER,((PageLink)item).getPageId());
      }
      link.addIWPOListener(this.getOwnerInstance());

      if(listenerString != null){
        link.addIWPOListener(listenerString);
      }

      if(isInIFrame()){
        link.setTarget(Link.TARGET_PARENT_WINDOW);
        //link.setURL(IWMainApplication.BUILDER_SERVLET_URL);
        link.setURL(iwc.getIWMainApplication().getBuilderServletURI());
      }
    }

    public void main(IWContext iwc) throws Exception {
      super.main(iwc);
      this.url = null;
    }

    public GenericState getStateInstance(IWContext iwc){
      return new ProjectNavigatorState(this, iwc);
    }

    public String changeState(PresentationObject source, IWContext iwc){
      ProjectNavigatorState oldState = (ProjectNavigatorState)this.getState(iwc);
      if(this.equals(source)){
        String selected = iwc.getParameter(_SELECTED_ENTITY_ID);
        if(selected != null){
          try {
            oldState.setSelectedElementID(Integer.parseInt(selected));
          }
          catch (NumberFormatException ex) {
            //
          }
        }
      }
      if(source instanceof ProjectFilter){
        String cat = iwc.getParameter(ProjectFilter._PRM_PROJECT_CATEGORY_ID);
        String cattype = iwc.getParameter(ProjectFilter._PRM_CAT_TYPE_ID);
        if(cat != null && cattype != null){
          try {
            oldState.addFilter(Integer.parseInt(cattype), Integer.parseInt(cat));
          }
          catch (NumberFormatException ex) {
            //
          }
        }
      }
      return oldState.getStateString();
    }

    public synchronized Object clone(){
      ProjectNavigatorContent obj = (ProjectNavigatorContent)super.clone();

      obj.business = ProjectBusiness.getInstance();

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

  }

}















/*
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
      *//*
      return business.getProjectDPTPageLinks();
    }

    public void initColumns(IWContext iwc) throws Exception{
      super.addLinkEntityColumn(com.idega.builder.dynamicpagetrigger.data.PageLinkBMPBean._COLUMNNAME_DEFAULT_LINK_TEXT);
    }

    protected void addParameters(IWContext iwc, IDOLegacyEntity item, Link link){
      link.addParameter(com.idega.builder.business.BuilderLogic.IB_PAGE_PARAMETER,((PageLink)item).getPageId());
      link.setTarget(Link.TARGET_PARENT_WINDOW);
    }

  }

*/

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
    super.addLinkEntityColumn(com.idega.builder.dynamicpagetrigger.data.PageLinkBMPBean._COLUMNNAME_DEFAULT_LINK_TEXT);
  }

  protected void addParameters(IWContext iwc, IDOLegacyEntity item, Link link){
    link.addParameter(com.idega.builder.business.BuilderLogic.IB_PAGE_PARAMETER,((PageLink)item).getPageId());
    link.setTarget(Link.TARGET_PARENT_WINDOW);
  }
*/
/*
}
*/
