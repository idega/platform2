package is.idega.idegaweb.project.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.Table;
import is.idega.idegaweb.project.business.ProjectBusiness;
import is.idega.idegaweb.project.data.IPCategoryType;
import com.idega.presentation.PresentationObject;

import java.util.List;
import java.util.Iterator;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class IPCategoryCreator extends IWAdminWindow {

  protected final static String _PRM_SUBMIT = "ip_submit";
  public final static String _PRM_UPDATE = ProjectBusiness._PRM_UPDATE;
  protected final static String categoryNameFieldName = "ip_cat_name";
  protected final static String categoryDescriptionFieldName = "ip_cat_description";
  protected final static String categoryTypeFieldName = "ip_cat_type";

  private Form myForm;

  private TextInput categoryNameField;
  private TextArea categoryDescriptionField;
  private DropdownMenu categoryTypeField;

  private Text textTemplate;
  private Text categoryNameText;
  private Text categoryDescriptionText;
  private Text categoryTypeText;

  private SubmitButton okButton;
  private CloseButton cancelButton;

  private String rowHeight = "30";

  private ProjectBusiness business = null;

  public IPCategoryCreator() {
    super();
    this.setHeight(250);
    this.setWidth(220);
    this.setScrollbar(false);
    myForm = new Form();

    initializeFields();
    initializeTexts();

  }

  public void initializeFields(){
    categoryNameField = new TextInput(categoryNameFieldName);
    categoryNameField.setLength(20);

    categoryDescriptionField = new TextArea(categoryDescriptionFieldName);
    categoryDescriptionField.setHeight(3);
    categoryDescriptionField.setWidth(20);

    categoryTypeField = new DropdownMenu(categoryTypeFieldName);


    okButton = new SubmitButton(_PRM_SUBMIT,"   OK   ");
    cancelButton = new CloseButton(" Cancel ");

  }

  public void initializeTexts(){
    textTemplate = new Text();

    categoryNameText = (Text)textTemplate.clone();
    categoryNameText.setText("Name: ");
    categoryTypeText = (Text)textTemplate.clone();
    categoryTypeText.setText("Type of category: ");
    categoryDescriptionText = (Text)textTemplate.clone();
    categoryDescriptionText.setText("Description: ");
  }

  public PresentationObject lineUpElements(IWContext iwc){
    Table frameTable = new Table(1,2);
    frameTable.setAlignment("center");
    frameTable.setVerticalAlignment("middle");
    frameTable.setCellpadding(0);
    frameTable.setCellspacing(0);

    // nameTable begin
    Table nameTable = new Table(1,6);
    nameTable.setCellpadding(0);
    nameTable.setCellspacing(0);
    nameTable.setHeight(1,rowHeight);
    nameTable.setHeight(2,rowHeight);

    nameTable.add(categoryNameText,1,1);
    nameTable.add(categoryNameField,1,2);
    nameTable.add(categoryTypeText,1,3);
    nameTable.add(categoryTypeField,1,4);
    nameTable.add(categoryDescriptionText,1,5);
    nameTable.add(categoryDescriptionField,1,6);
    // nameTable end

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

  protected void configureCategoryTypeDropdown(IWContext iwc)throws Exception{
    categoryTypeField.removeElements();

    if(business == null){
      business = ProjectBusiness.getInstance();
    }

    List types = business.getCategoryTypes();
    if(types != null){
      Iterator iter = types.iterator();
      while (iter.hasNext()) {
        IPCategoryType item = (IPCategoryType)iter.next();
        categoryTypeField.addMenuElement(item.getID(),item.getName());
      }
    }else{
      categoryTypeField.addMenuElement("","No type declared");
    }

  }

  public void main(IWContext iwc) throws Exception {
    myForm.empty();
    this.add(myForm);
    business = ProjectBusiness.getInstance();

    if(iwc.getParameter(_PRM_SUBMIT) != null){
       if(iwc.getParameter(_PRM_UPDATE) != null){
          boolean succeeded= false;
          if(succeeded){
            this.close();
            this.setParentToReload();
          }
       } else {
          boolean succeeded= false;

          try {
            String id = iwc.getParameter(categoryTypeFieldName);
            String name = iwc.getParameter(categoryNameFieldName);
            if(id == null){
              //error
            } else if(id.equals("")){
              //error
            }
            if(name == null){
              //error
            } else if(name.equals("")){
              name = "Untitled Category";
            }
            business.createIPCategory(Integer.parseInt(id),name,iwc.getParameter(categoryDescriptionFieldName));
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
      configureCategoryTypeDropdown(iwc);
      myForm.add(lineUpElements(iwc));
    }

  }


}