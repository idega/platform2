package is.idega.idegaweb.project.presentation;

import is.idega.idegaweb.project.business.ProjectBusiness;
import is.idega.idegaweb.project.data.IPCategory;
import is.idega.idegaweb.project.data.IPCategoryType;
import is.idega.idegaweb.project.data.IPProject;

import java.util.Iterator;
import java.util.List;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class IPProjectCreator extends IWAdminWindow {

  protected final static String _PRM_SUBMIT = "ip_submit";
  public final static String _PRM_UPDATE = ProjectBusiness._PRM_UPDATE;
  protected final static String projectNameFieldName = "ip_p_name";
  protected final static String projectNumberFieldName = "ip_p_number";
  protected final static String projectDescriptionFieldName = "ip_p_description";
  protected final static String projectCategoryFieldName = "ip_p_category";
  public final static String _PRM_INSTANCE_ID = "ic_inst_id";

  private Form myForm;

  private TextInput projectNameField;
  private TextInput projectNumberField;
  private TextArea projectDescriptionField;
  private DropdownMenu categoryFieldTemplate;

  private Text textTemplate;
  private Text projectNameText;
  private Text projectNumberText;
  private Text projectDescriptionText;
  private Text categoryText;


  private SubmitButton okButton;
  private CloseButton cancelButton;

  private String rowHeight = "30";

  private ProjectBusiness business = null;

  public IPProjectCreator() {
    super();
    this.setHeight(400);
    this.setWidth(400);
    this.setResizable(true);

    myForm = new Form();

    initializeFields();
    initializeTexts();

  }

  public void initializeFields(){
    projectNameField = new TextInput(projectNameFieldName);
    projectNameField.setLength(20);

    projectNumberField = new TextInput(projectNumberFieldName);
    projectNumberField.setLength(20);

    projectDescriptionField = new TextArea(projectDescriptionFieldName);
    projectDescriptionField.setHeight(6);
    projectDescriptionField.setWidth(40);

    categoryFieldTemplate = new DropdownMenu(projectCategoryFieldName);

    okButton = new SubmitButton(_PRM_SUBMIT,"   OK   ");
    cancelButton = new CloseButton(" Cancel ");

  }

  public void initializeTexts(){
    textTemplate = new Text();

    projectNameText = (Text)textTemplate.clone();
    projectNameText.setText("Name: ");
    projectNumberText = (Text)textTemplate.clone();
    projectNumberText.setText("Project number: ");
    categoryText = (Text)textTemplate.clone();
    categoryText.setText("Project categories: ");
    projectDescriptionText = (Text)textTemplate.clone();
    projectDescriptionText.setText("Description: ");
  }

  public PresentationObject lineUpElements(IWContext iwc, IPProject project) throws Exception {

    Table frameTable = new Table(1,2);
    frameTable.setAlignment("center");
    frameTable.setVerticalAlignment("middle");
    frameTable.setCellpadding(0);
    frameTable.setCellspacing(0);

    // nameTable begin
    Table nameTable = new Table(1,8);
    nameTable.setCellpadding(0);
    nameTable.setCellspacing(0);
    nameTable.setHeight(1,rowHeight);
    nameTable.setHeight(2,rowHeight);

    nameTable.add(projectNameText,1,1);
    if(project != null){
      projectNameField.setContent(project.getName());
    }
    nameTable.add(projectNameField,1,2);
    nameTable.add(projectNumberText,1,3);
    if(project != null){
      projectNumberField.setContent(project.getProjectNumber());
    }
    nameTable.add(projectNumberField,1,4);
    nameTable.add(projectDescriptionText,1,7);
    if(project != null){
      projectDescriptionField.setContent(project.getDescription());
    }
    nameTable.add(projectDescriptionField,1,8);
    // nameTable end
    if(business == null){
      business = ProjectBusiness.getInstance();
    }

    List catTypes = business.getCategoryTypes();
    if(catTypes != null){
      Table categoryTable = new Table(2,catTypes.size());
      categoryTable.setCellpadding(0);
      categoryTable.setCellspacing(0);
      categoryTable.setWidth("100%");
      categoryTable.setColumnAlignment(2,"right");
      Iterator iter = catTypes.iterator();
      int index = 1;
      categoryFieldTemplate.addMenuElement(-1,"No category");
      while (iter.hasNext()) {
        IPCategoryType item = (IPCategoryType)iter.next();

        categoryTable.add(item.getName(),1,index);
        DropdownMenu menu = (DropdownMenu)categoryFieldTemplate.clone();

        List categories = business.getCategories(item.getID());
        if(categories != null && categories.size()>0){
          Iterator iter2 = categories.iterator();
          while (iter2.hasNext()) {
            IPCategory item2 = (IPCategory)iter2.next();
            menu.addMenuElement(item2.getID(),item2.getName());
          }
        }
        if(project != null){
          try {
            IPCategory c = business.getProjectCategory(item.getID(),project.getID());
            if(c != null){
              menu.setSelectedElement(Integer.toString(c.getID()));
            } else {
              menu.setSelectedElement("-1");
            }
          }
          catch (Exception ex) {
            ex.printStackTrace();
            menu.setSelectedElement("-1");
          }

        }
        categoryTable.add(menu,2,index);

        index++;
      }
      nameTable.add(categoryText,1,5);
      nameTable.add(categoryTable,1,6);
    }

    // buttonTable begin
    Table buttonTable = new Table(3,1);
    buttonTable.setCellpadding(0);
    buttonTable.setCellspacing(0);
    buttonTable.setHeight(1,rowHeight);
    buttonTable.setWidth(2,"5");

    buttonTable.add(okButton,1,1);
    buttonTable.add(cancelButton,3,1);
    // buttonTable end


    frameTable.add(nameTable,1,1);


    frameTable.add(buttonTable,1,2);
    frameTable.setAlignment(1,2,"right");

    return frameTable;
  }


  public void main(IWContext iwc) throws Exception {
    myForm.empty();
    myForm.maintainParameter(_PRM_INSTANCE_ID);
    myForm.maintainParameter(_PRM_UPDATE);
    this.add(myForm);
    business = ProjectBusiness.getInstance();

    if(iwc.getParameter(_PRM_SUBMIT) != null){
       if(iwc.getParameter(_PRM_UPDATE) != null && !iwc.getParameter(_PRM_UPDATE).equalsIgnoreCase("false")){
          boolean succeeded= false;

          try {
            String instID = iwc.getParameter(_PRM_INSTANCE_ID);
            int instanceId = -1;
            if(instID != null && !instID.equals("")){
              instanceId = Integer.parseInt(instID);
            }
            String name = iwc.getParameter(projectNameFieldName);
            if(name == null){
              //error
            } else if(name.equals("")){
              name = "Untitled project";
            }
            String pNumber = iwc.getParameter(projectNumberFieldName);
            String description = iwc.getParameter(projectDescriptionFieldName);
            String parent = null;//iwc.getParameter(projectNameFieldName);
            Integer parentId = null;
            if(parent != null && !parent.equals("")){
              parentId = new Integer(parent);
            }
            String[] catIds = iwc.getParameterValues(projectCategoryFieldName);
            int[] categoryIds = null;
            if(catIds != null && catIds.length > 0){
              categoryIds = new int[catIds.length];
              for (int i = 0; i < catIds.length; i++) {
                try {
                  categoryIds[i] = Integer.parseInt(catIds[i]);
                }
                catch (NumberFormatException ex) {
                  if(catIds[i].equals("")){
                    categoryIds[i] = 0;
                  }else{
                    ex.printStackTrace();
                  }
                }
              }
            }

            business.updateIPProject(instanceId,name,pNumber,description,parentId,categoryIds);

            boolean triggerPage = true;
            if(triggerPage){
              business.changeNameOfPageLink(instanceId,name);
            }

            succeeded = true;
          }
          catch (Exception ex) {
            succeeded = false;
            ex.printStackTrace();
          }

          if(succeeded){
            this.close();
            this.setParentToReload();
          }
       } else {
          boolean succeeded= false;
          try {
            String name = iwc.getParameter(projectNameFieldName);
            if(name == null){
              //error
            } else if(name.equals("")){
              name = "Untitled project";
            }
            String pNumber = iwc.getParameter(projectNumberFieldName);
            String description = iwc.getParameter(projectDescriptionFieldName);
            String parent = null;//iwc.getParameter(projectNameFieldName);
            Integer parentId = null;
            if(parent != null && !parent.equals("")){
              parentId = new Integer(parent);
            }
            String[] catIds = iwc.getParameterValues(projectCategoryFieldName);
            int[] categoryIds = null;
            if(catIds != null && catIds.length > 0){
              categoryIds = new int[catIds.length];
              for (int i = 0; i < catIds.length; i++) {
                try {
                  categoryIds[i] = Integer.parseInt(catIds[i]);
                }
                catch (NumberFormatException ex) {
                  if(catIds[i].equals("")){
                    categoryIds[i] = 0;
                  }else{
                    ex.printStackTrace();
                  }
                }
              }
            }

            int projectId = business.createIPProject(name,pNumber,description,parentId,categoryIds);

            boolean triggerPage = true;
            if(triggerPage){
              business.createPageLink(iwc,projectId,name);
            }

            succeeded = true;
          }
          catch (Exception ex) {
            succeeded = false;
            ex.printStackTrace();
          }
          if(succeeded){
            this.close();
            this.setParentToReload();
          }
       }
    } else {
      IPProject project = null;
      if(iwc.getParameter(_PRM_UPDATE) != null && !iwc.getParameter(_PRM_UPDATE).equalsIgnoreCase("false")){
        project = ((is.idega.idegaweb.project.data.IPProjectHome)com.idega.data.IDOLookup.getHomeLegacy(IPProject.class)).findByPrimaryKeyLegacy(Integer.parseInt(iwc.getParameter(_PRM_INSTANCE_ID)));
      }

      myForm.add(lineUpElements(iwc,project));
    }

  }


}
