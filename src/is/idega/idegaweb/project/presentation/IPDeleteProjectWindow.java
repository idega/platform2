package is.idega.idegaweb.project.presentation;

import com.idega.builder.business.BuilderLogic;

import com.idega.presentation.PresentationObject;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.presentation.Table;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.text.Text;
import com.idega.idegaweb.presentation.IWAdminWindow;
import is.idega.idegaweb.project.business.ProjectBusiness;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWConstants;

/**
 * Title:        idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class IPDeleteProjectWindow extends IWAdminWindow{

  protected final static String _PRM_CONFIRM = "ip_delete_confirm";
  public final static String _PRM_DELETE = ProjectBusiness._PRM_DELETE;
  public final static String _PRM_INSTANCE_ID = "ic_inst_id";
  public final static String _PRM_PAGE_ID = "ip_page_id";
  public int pageId = -1;


  public IPDeleteProjectWindow() {
    setWidth(250);
    setHeight(135);
  }

  public String getBundleIdentifier(){
    return ProjectBusiness.IW_PROJECT_IDENTIFIER;
  }

  public void main(IWContext iwc){
      setTitle("Confirm delete");

      String ib_parent_id = iwc.getParameter(_PRM_INSTANCE_ID);
      //this.setParentToReload();

      //this.debugParameters(iwc);


      boolean doConfirm = !(iwc.getParameter(_PRM_CONFIRM)!=null || iwc.getParameter(_PRM_CONFIRM+".x")!=null);
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
            pageId = BuilderLogic.getInstance().getStartPageId(iwc);
          }

        }else{
          pageId = BuilderLogic.getInstance().getStartPageId(iwc);
        }

        this.setOnUnLoad("window.opener.location='"+BuilderLogic.getInstance().getIBPageURL(iwc,pageId)+"'");
        this.close();
      }
  }

  public PresentationObject getConfirmBox(IWContext iwc){
    Table t = new Table(1,2);
    Form f = new Form();

    IWBundle iwb = this.getBundle(iwc);
    IWResourceBundle iwrb = iwb.getResourceBundle(iwc);


    f.maintainParameter(_PRM_DELETE);
    f.maintainParameter(_PRM_INSTANCE_ID);
    f.maintainParameter(_PRM_PAGE_ID);

    f.add(t);
    t.setWidth("100%");
    t.setAlignment(1,1,IWConstants.CENTER_ALIGNMENT);
    t.setVerticalAlignment(1,1,IWConstants.MIDDLE_ALIGNMENT);
    //t.setHeight("100");
    t.setHeight(1,"70");
    t.setHeight(2,"20");
    t.setAlignment(IWConstants.CENTER_ALIGNMENT);

    Text confirmText = new Text(iwrb.getLocalizedString("confirm_invalidate_project","Are you sure you want to delete this project?"));
    this.formatText(confirmText,true);
    t.add(confirmText,1,1);

    SubmitButton button = new SubmitButton(iwrb.getLocalizedImageButton("yes", "YES"), _PRM_CONFIRM);
    CloseButton closebutton = new CloseButton(iwrb.getLocalizedImageButton("cancel", "CANCEL"));

    Table innerTable = new Table(3,1);
    innerTable.setAlignment(IWConstants.CENTER_ALIGNMENT);
    innerTable.add(button,1,1);
    innerTable.add(closebutton,3,1);
    innerTable.setHeight(20);
    innerTable.setWidth(2,"5");
    innerTable.setCellpadding(0);
    innerTable.setCellspacing(0);
    t.add(innerTable,1,2);

//    t.setBorder(1);
//    innerTable.setBorder(1);

    return f;
  }

}
