package is.idega.idegaweb.project.presentation;

import com.idega.presentation.Block;

import com.idega.presentation.text.Link;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.CloseButton;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.text.Text;
import com.idega.idegaweb.IWResourceBundle;
import is.idega.idegaweb.project.business.ProjectBusiness;
import is.idega.idegaweb.project.data.IPCategory;
import com.idega.presentation.ui.Window;

import java.util.List;



/**
 * Title:        IW Project
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Guðmundur Ágúst Sæmundsson</a>
 * @version 1.0
 */

public class ProjectCategoryModificationLink extends Block {

  protected Link modifyCategoryLink = null;
  protected Image optionalImage = null;
  protected int _categoryTypeId = -1;
  public static final String _PROJECT_BUNDLE_IDENTIFIER = "is.idega.idegaweb.project";


  public ProjectCategoryModificationLink() {
    super();
  }

  public String getBundleIdentifier(){
    return _PROJECT_BUNDLE_IDENTIFIER;
  }

  public void setImage(Image image){
    optionalImage = image;
  }

  public void setCategoryTypeId(int id){
    _categoryTypeId = id;
  }

  public void main(IWContext iwc) throws Exception {
    //IWBundle core = iwc.getApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
    this.empty();


    if(optionalImage != null){
      modifyCategoryLink = new Link(optionalImage);
    } else {
      IWResourceBundle iwrb = this.getResourceBundle(iwc);
      Image tmp = iwrb.getImage("modify_category.gif","modify category");
      if(tmp != null){
        modifyCategoryLink = new Link(tmp);
      } else {
        modifyCategoryLink = new Link("*");
      }
    }

    modifyCategoryLink.setName("modify category");
    modifyCategoryLink.setWindowToOpen(ModifyCategoryWindow.class);
    modifyCategoryLink.addParameter(ModifyCategoryWindow._CATEGORY_TYPE, _categoryTypeId);
    this.add(modifyCategoryLink);

  }



  public static class ModifyCategoryWindow extends Window{
    private static final String _CATEGORY_TYPE = "ip_c_t";
    private static final String _PRM_NAME = "ip_c_name";
    private static final String _PRM_DELETE = "ip_c_del";
    private static final String _PRM_ID = "ip_c_id";




    public ModifyCategoryWindow(){
      super("Modify Categories");
      this.setAllMargins(0);
      this.setWidth(300);
      this.setHeight(300);
      //this.setBackgroundColor("#d4d0c8");
    }


    public void main(IWContext iwc) throws Exception {

      Form myForm = new Form();
      myForm.maintainParameter(_CATEGORY_TYPE);

      int typeId = -1;
      try {
        typeId = Integer.parseInt(iwc.getParameter(_CATEGORY_TYPE));
      }
      catch (Exception ex) {
        // do Nothing
      }


      if(iwc.getParameter("commit") != null){

        //Save

        String[] names = iwc.getParameterValues(_PRM_NAME);
        String[] ids = iwc.getParameterValues(_PRM_ID);
        String[] del = iwc.getParameterValues(_PRM_DELETE);
        if(names != null && ids != null){
          System.out.println(" id\tname");
          for (int i = 0; i < names.length; i++) {
            System.out.println(" "+ids[i]+"\t"+names[i]);
          }
          if(del != null){
            for (int i = 0; i < del.length; i++) {
              System.out.print(del[i]);
              System.out.print(", ");
            }

          }
        }else{
          System.out.println("names = "+ names +", ids = "+ ids);
        }

        this.setParentToReload();
        this.close();

      } else {

        CategoryList cList = new CategoryList(_PRM_DELETE,_PRM_NAME, _PRM_ID, typeId);

        myForm.add(cList);
        myForm.add(new SubmitButton("commit","    OK   "));
        myForm.add(new CloseButton("  Cancel  "));
        this.add(myForm);
      }


    }


  }




  public static class CategoryList extends AbstractContentList {

    private String _checkboxName = null;
    private String _textinputName = null;
    private String _hiddeninputName = null;
    private int _catTypeId = -1;

    private CategoryList() {
      super();
    }

    private CategoryList(String checkboxName, String textinputName, String hiddeninputName, int catTypeId) {
      this();
      _catTypeId = catTypeId;
      _checkboxName = checkboxName;
      _textinputName = textinputName;
      _hiddeninputName = hiddeninputName;
    }

    public synchronized Object clone(){
      ModifyCategoryWindow obj = (ModifyCategoryWindow)super.clone();

      return obj;
    }


    public List getEntityList(IWContext iwc) throws Exception {
      List l = null;
      if(_catTypeId != -1){
        l = ProjectBusiness.getInstance().getCategories(_catTypeId);
      }

      return l;

    }

    public void initColumns(IWContext iwc) throws java.lang.Exception {
      //this.setColumns(6);
      //this.setWidth("567");
      this.setColumns(3);
      this.setWidth("240");
      this.setExtraRowsAtBeginning(1);


      this.setColumnWidth(1,"40");
      this.setColumnWidth(2,"150");
      this.setColumnWidth(3,"1");
    }

    public PresentationObject getObjectToAddToColumn(int colIndex, int rowIndex, Object item, IWContext iwc, boolean beforeEntities)throws Exception{
      if(item == null){
        if(beforeEntities && (rowIndex == 1)){
          Text text = new Text();
          text.setBold();

          switch (colIndex) {
            case 1:
              text.setText("Eyða");
              break;
            case 2:
              text.setText("Nafn");
              break;
            case 3:
              return null;
          }
          return text;

        }
      } else {
          IPCategory ipc = (IPCategory)item;

          switch (colIndex) {
            case 1:
              return getCheckBox(ipc.getID());
            case 2:
              return getTextInput(ipc.getName());
            case 3:
              return getHiddenInput(ipc.getID());
          }
      }
      return null;
    }

    public CheckBox getCheckBox(int catId){
      CheckBox box = new CheckBox(_checkboxName);
      box.setValue(catId);
      return box;
    }

    public TextInput getTextInput(String value){
      TextInput input = new TextInput(_textinputName,value);
      return input;
    }

    public HiddenInput getHiddenInput(int id){
      HiddenInput input = new HiddenInput(_hiddeninputName,Integer.toString(id));
      return input;
    }




  } // InnerClass CategoryList



}