package is.idega.idegaweb.project.presentation;

import com.idega.builder.business.BuilderLogic;

import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.Table;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.text.Text;
import com.idega.builder.presentation.IBAdminWindow;
import is.idega.idegaweb.project.business.ProjectBusiness;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class IPDeleteProjectWindow extends IBAdminWindow{

  protected final static String _PRM_CONFIRM = "ip_delete_confirm";
  public final static String _PRM_DELETE = ProjectBusiness._PRM_DELETE;
  public final static String _PRM_INSTANCE_ID = "ic_inst_id";
  public final static String _PRM_PAGE_ID = "ip_page_id";
  public int pageId = -1;


  public IPDeleteProjectWindow() {
    setWidth(300);
    setHeight(200);
  }

  public void main(IWContext iwc){

      setTitle("Confirm delete");

      String ib_parent_id = iwc.getParameter(_PRM_INSTANCE_ID);
      //this.setParentToReload();


      boolean doConfirm = (iwc.getParameter(_PRM_CONFIRM)==null);
      if(doConfirm){
        add(getConfirmBox(iwc));
      }
      else{
        int userID = -1;
        try {
          userID = iwc.getUser().getID();
        }
        catch (NullPointerException ex) {
          // not logged on
        }

        ProjectBusiness.getInstance().invalidateProject(iwc,Integer.parseInt(iwc.getParameter(_PRM_INSTANCE_ID)),userID);
        String page = iwc.getParameter(_PRM_PAGE_ID);
        if(page != null){
          try {
            pageId = Integer.parseInt(page);
          }
          catch (NumberFormatException ex) {
            pageId = BuilderLogic.getStartPageId(iwc);
          }

        }else{
          pageId = BuilderLogic.getStartPageId(iwc);
        }

        this.setOnUnLoad("window.opener.location='"+BuilderLogic.getInstance().getIBPageURL(pageId)+"'");
        this.close();
      }
  }

  public PresentationObject getConfirmBox(IWContext iwc){
    Table t = new Table(1,2);
    Form f = new Form();

    f.maintainParameter(_PRM_DELETE);
    f.maintainParameter(_PRM_INSTANCE_ID);
    f.maintainParameter(_PRM_PAGE_ID);

    f.add(t);
    t.setWidth("100%");
    t.setHeight("150");
//    t.setAlignment(com.idega.idegaweb.IWConstants.CENTER_ALIGNMENT);

    Text confirmText = new Text("Are you sure you want to delete this project?");
    t.add(confirmText,1,1);

    SubmitButton button = new SubmitButton(this._PRM_CONFIRM,"Yes");
    CloseButton closebutton = new CloseButton("Cancel");

    Table innerTable = new Table(2,1);
    innerTable.setAlignment(com.idega.idegaweb.IWConstants.CENTER_ALIGNMENT);
    innerTable.add(button,1,1);
    innerTable.add(closebutton,2,1);
    t.add(innerTable,1,2);

    return f;
  }

}