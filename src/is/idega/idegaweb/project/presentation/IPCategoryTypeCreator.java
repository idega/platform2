package is.idega.idegaweb.project.presentation;

import com.idega.idegaweb.presentation.IWAdminWindow;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;
import com.idega.presentation.Table;
import is.idega.idegaweb.project.business.ProjectBusiness;
import com.idega.presentation.PresentationObject;

/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class IPCategoryTypeCreator extends IWAdminWindow {

  protected final static String _PRM_SUBMIT = "ip_submit";
  public final static String _PRM_UPDATE = ProjectBusiness._PRM_UPDATE;
  protected final static String categoryTypeNameFieldName = "ip_ct_name";
  protected final static String categoryTypeDescriptionFieldName = "ip_ct_description";

  private Form myForm;

  private TextInput categoryTypeNameField;
  private TextArea categoryTypeDescriptionField;


  private Text textTemplate;
  private Text categoryTypeNameText;
  private Text categoryTypeDescriptionText;

  private SubmitButton okButton;
  private CloseButton cancelButton;

  private String rowHeight = "30";

  private ProjectBusiness business;

  public IPCategoryTypeCreator() {
    super();
    this.setHeight(230);
    this.setWidth(230);
    this.setScrollbar(false);
    myForm = new Form();

    initializeFields();
    initializeTexts();

  }

  public void initializeFields(){
    categoryTypeNameField = new TextInput(categoryTypeNameFieldName);
    categoryTypeNameField.setLength(20);

    categoryTypeDescriptionField = new TextArea(categoryTypeDescriptionFieldName);
    categoryTypeDescriptionField.setHeight(3);
    categoryTypeDescriptionField.setWidth(20);


    okButton = new SubmitButton(_PRM_SUBMIT,"   OK   ");
    cancelButton = new CloseButton(" Cancel ");

  }

  public void initializeTexts(){
    textTemplate = new Text();

    categoryTypeNameText = (Text)textTemplate.clone();
    categoryTypeNameText.setText("Name: ");
    categoryTypeDescriptionText = (Text)textTemplate.clone();
    categoryTypeDescriptionText.setText("Description: ");
  }

  public PresentationObject lineUpElements(IWContext iwc){
    Table frameTable = new Table(1,2);
    frameTable.setAlignment("center");
    frameTable.setVerticalAlignment("middle");
    frameTable.setCellpadding(0);
    frameTable.setCellspacing(0);

    // nameTable begin
    Table nameTable = new Table(1,4);
    nameTable.setCellpadding(0);
    nameTable.setCellspacing(0);
    nameTable.setHeight(1,rowHeight);
    nameTable.setHeight(2,rowHeight);

    nameTable.add(categoryTypeNameText,1,1);
    nameTable.add(categoryTypeNameField,1,2);
    nameTable.add(categoryTypeDescriptionText,1,3);
    nameTable.add(categoryTypeDescriptionField,1,4);
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
            String name = iwc.getParameter(categoryTypeNameFieldName);
            if(name == null){
              //error
            } else if(name.equals("")){
              name = "Untitled Categorytype";
            }
            business.createIPCategoryType(name,iwc.getParameter(categoryTypeDescriptionFieldName));
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
      myForm.add(lineUpElements(iwc));
    }

  }


}