package is.idega.idegaweb.project.presentation;

import com.idega.presentation.Block;

import com.idega.presentation.text.Link;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.idegaweb.IWResourceBundle;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ProjectCategoryCreationLink extends Block {

  protected Link createProjectCategoryLink = null;
  protected Image optionalImage = null;
  public static final String _PROJECT_BUNDLE_IDENTIFIER = "is.idega.idegaweb.project";


  public ProjectCategoryCreationLink() {
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
      createProjectCategoryLink = new Link(optionalImage);
    } else {
      IWResourceBundle iwrb = this.getResourceBundle(iwc);
      Image tmp = iwrb.getImage("create_category.gif","create category");
      if(tmp != null){
        createProjectCategoryLink = new Link(tmp);
      } else {
        createProjectCategoryLink = new Link("+");
      }
    }

    createProjectCategoryLink.setName("create category");
    createProjectCategoryLink.setWindowToOpen(IPCategoryCreator.class);
    this.add(createProjectCategoryLink);

  }


}