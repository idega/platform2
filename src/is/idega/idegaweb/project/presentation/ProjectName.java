package is.idega.idegaweb.project.presentation;

import com.idega.presentation.text.Text;
import is.idega.idegaweb.project.business.ProjectBusiness;
import is.idega.idegaweb.project.data.IPProject;
import com.idega.presentation.IWContext;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ProjectName extends Text {

  public ProjectName() {
    super();
  }

  public void main(IWContext iwc) throws Exception {
    int prId = ProjectBusiness.getCurrentProjectId(iwc);
    if(prId != -1){
      try {
        IPProject pr = ((is.idega.idegaweb.project.data.IPProjectHome)com.idega.data.IDOLookup.getHomeLegacy(IPProject.class)).findByPrimaryKeyLegacy(prId);
        this.setText(pr.getName());
      }
      catch (Exception ex) {
        this.setText("!Name not found!");
      }
    }else{
      this.setText("Project name");
    }


  }
}
