package is.idega.idegaweb.project.presentation;

import com.idega.presentation.Block;

import com.idega.presentation.text.Link;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.idegaweb.IWResourceBundle;
import is.idega.idegaweb.project.business.ProjectBusiness;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ProjectInvalidationLink extends Block {

  protected Link modifyProjectLink = null;
  protected Image optionalImage = null;
  public static final String _PROJECT_BUNDLE_IDENTIFIER = "is.idega.idegaweb.project";


  public ProjectInvalidationLink() {
    super();
  }

  public String getBundleIdentifier(){
    return _PROJECT_BUNDLE_IDENTIFIER;
  }

  public void setImage(Image image){
    optionalImage = image;
  }


  public void main(IWContext iwc) throws Exception {
    //IWBundle core = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
    this.empty();


    if(optionalImage != null){
      modifyProjectLink = new Link(optionalImage);
    } else {
      IWResourceBundle iwrb = this.getResourceBundle(iwc);
      Image tmp = iwrb.getImage("delete_project.gif","delete project");
      if(tmp != null){
        modifyProjectLink = new Link(tmp);
      } else {
        modifyProjectLink = new Link("*");
      }
    }

    modifyProjectLink.setName("delete project");
    modifyProjectLink.setWindowToOpen(IPDeleteProjectWindow.class);
    modifyProjectLink.addParameter(IPDeleteProjectWindow._PRM_DELETE,"true");
    modifyProjectLink.addParameter(IPDeleteProjectWindow._PRM_INSTANCE_ID, ProjectBusiness.getCurrentProjectId(iwc));
    this.add(modifyProjectLink);

  }


}