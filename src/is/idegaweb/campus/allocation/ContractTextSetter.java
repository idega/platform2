package is.idegaweb.campus.allocation;


import is.idegaweb.campus.presentation.Edit;
import is.idegaweb.campus.entity.SystemProperties;
import is.idegaweb.campus.entity.ContractText;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.util.idegaTimestamp;
import com.idega.block.finance.presentation.KeyEditor;
import com.idega.development.presentation.Localizer;
import com.idega.util.LocaleUtil;
import java.util.List;
import java.sql.SQLException;
import com.idega.data.EntityFinder;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class ContractTextSetter extends com.idega.presentation.PresentationObjectContainer{

  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  private final static String IS ="IS";
  private final static String EN ="EN";
  private final static String TIS ="TIS";
  private final static String TEN ="TEN";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private String propParameter = SystemProperties.getEntityTableName();
  private String localesParameter="iw_locales";
  private String bottomThickness = "8";
  private boolean isAdmin;
  private boolean useObjectInstanciator = false;


  public ContractTextSetter() {

  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void setToUseObjectInstanciator(boolean use){
    useObjectInstanciator=use;
  }

  protected void control(IWContext iwc){
    iwb = getBundle(iwc);
    iwrb = getResourceBundle(iwc);

    if(isAdmin){
      //add(getPDFLink(new Image("/pics/print.gif")));
      if(iwc.getParameter("savetitle")!=null){
          updateTitleForm(iwc);
          add(getMainTable());

      }
      else if(iwc.getParameter("savetext")!=null){
        updateForm(iwc);
        add(getMainTable());
      }
      else if( iwc.getParameter("delete")!= null){
        add (ConfirmDelete(iwc));
      }
      else if(iwc.getParameter("conf_delete")!=null){
        deleteText(iwc);
        add(getMainTable());
      }
      else if(iwc.getParameter("text_id")!=null || iwc.getParameter("new_text")!=null){
        add(getSetupForm(iwc));
      }
      else if(iwc.getParameter("new_title")!=null){
        add(getTitleForm(iwc));
      }
      else if(iwc.getParameter("title_id")!=null){
        add(getTitleForm(iwc));
      }
      else {
        add(getMainTable());
      }

    }
    else
      add(iwrb.getLocalizedString("access_denied","Access_denied"));


  }

  private PresentationObject getMainTable(){
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    List L = listOfTexts();
    ContractText Title = getTitle();
    String sTitle = "";
    Link newTitleLink = new Link();
    if(Title != null){
      sTitle = Title.getText();
      newTitleLink.addParameter("title_id",String.valueOf(Title.getID()));
    }
    else{
      sTitle = iwrb.getLocalizedString("new_title","New Title");
      newTitleLink.addParameter("new_title","new_title");
    }
    newTitleLink.setText(sTitle);


    int row = 1;
    T.add(getPDFLink(new Image("/pics/print.gif")),1,row);
    T.add(getNewLink(),2,row);
    row++;
    T.add(Edit.titleText(iwrb.getLocalizedString("header","Header")),1,row);
    row++;
    T.add(newTitleLink,2,row);
    row++;
    T.add(Edit.titleText(iwrb.getLocalizedString("order","Order")),1,row);
    T.add(Edit.titleText(iwrb.getLocalizedString("title","Title")),2,row);
    row++;
    if(L!=null){
      int len = L.size();
      ContractText CT;
      for (int i = 0; i < len; i++) {
        CT = (ContractText) L.get(i);
        Link link = new Link(CT.getName());
        link.addParameter("text_id",CT.getID());
        T.add(String.valueOf(CT.getOrdinal()),1,row);
        T.add(link,2,row);
        row++;
      }
      //T.setColumnAlignment(1,"right");
      T.setHorizontalZebraColored(Edit.colorLightBlue,Edit.colorWhite);
      T.setRowColor(1,Edit.colorWhite);
      T.setRowColor(2,Edit.colorBlue);
      T.setRowColor(4,Edit.colorBlue);
      T.setRowColor(row,Edit.colorRed);
      T.setWidth(1,"30");
      T.mergeCells(1,2,2,2);

      T.mergeCells(1,row,8,row);
      T.add(Edit.formatText(" "),1,row);
      T.setHeight(row,bottomThickness);
    }
    else{
      T.add(iwrb.getLocalizedString("no_texts","No text in database"),1,2);
    }


    return T;
  }

  private PresentationObject getTitleForm(IWContext iwc){
    Form F = new Form();
    Table T = new Table();
    int row = 1;
    TextInput text = null;
    String sId = iwc.getParameter("title_id");
    if(sId!=null){
     try {
        ContractText CT = new ContractText(Integer.parseInt(sId));
        text = new TextInput("tname",CT.getText());
        HiddenInput HI = new HiddenInput("title_id",sId);
        T.add(HI);
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    else{
      text = new TextInput("tname");
    }
    SubmitButton save = new SubmitButton("savetitle","Save");
    text.setLength(80);
    T.add(getUpLink(),1,row++);
    T.add(Edit.formatText(iwrb.getLocalizedString("text","Text")),1,row++);
    T.add(text,1,row++);
    T.add(save,1,row);
    F.add(T);
    return (F);
  }

  private PresentationObject getSetupForm(IWContext iwc){
    //Table Frame = new Table(2,1);
    Table T = new Table();
    T.add(getUpLink(),1,1);
    T.add(getNewLink(),1,1);

    int row = 2;
    DropdownMenu intDrop = getIntegerDrop("ordinal",1,100);
    TextInput name = null;
    TextArea text = null;
    CheckBox CB = new CheckBox("usetags","true");
    String sId = iwc.getParameter("text_id");
    if(sId!=null){
      try {
        ContractText CT = new ContractText(Integer.parseInt(sId));
        name = new TextInput("name",CT.getName());
        text = getTextArea("texti",CT.getText());

        CB.setChecked(CT.getUseTags());
        intDrop.setSelectedElement(String.valueOf(CT.getOrdinal()));
        HiddenInput HI = new HiddenInput("text_id",sId);
        T.add(HI);
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    else{
      try {
        name = new TextInput("name");
        text = getTextArea("texti","");
        int max = new ContractText().getMaxColumnValue(ContractText.getOrdinalColumnName())+1;
        intDrop.setSelectedElement(String.valueOf(max));
       }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    SubmitButton save = new SubmitButton("savetext","Save");
    SubmitButton delete = new SubmitButton("delete","Delete");
    DropdownMenu tagDrop = getTagDrop("tags");
    tagDrop.setOnChange("this.form.texti.value += this.options[this.selectedIndex].value;");
    name.setLength(80);
    T.add(Edit.formatText(iwrb.getLocalizedString("title","Title")),1,row++);
    T.add(name,1,row++);
    T.add(Edit.formatText(iwrb.getLocalizedString("text","Text")),1,row++);
    T.add(text,1,row++);

    Table bottomTable = new Table();
    bottomTable.setWidth("100%");
    bottomTable.add(intDrop,1,1);
    bottomTable.add(CB,2,1);
    bottomTable.add(save,3,1);
    bottomTable.add(tagDrop,4,1);
    bottomTable.add(delete,5,1);

    T.add(bottomTable,1,row);


/*
    String[] tags = CampusContractWriter.getTags();
    Table T2 = new Table();
    row = 1;
    T2.add("Tags",1,row++);
    for (int i = 0; i < tags.length; i++) {
      T2.add("["+tags[i]+"]",1,row++);
    }
    Frame.add(T2,2,1);
*/
   // Frame.add(T,1,1);

   // Frame.setBorder(1);

    Form myForm = new Form();
    myForm.add(T);
    return myForm;
  }

  private PresentationObject ConfirmDelete(IWContext iwc){
    String sTextId = iwc.getParameter("text_id");
    Form F = new Form();

    Table T = new Table(3,2);
    T.mergeCells(1,1,3,1);
    if(sTextId != null)
      T.add(new HiddenInput("text_id",sTextId));
    SubmitButton del = new SubmitButton("conf_delete",iwrb.getLocalizedString("ok","OK"));
    BackButton back = new BackButton(iwrb.getLocalizedString("cancel","Cancel"));
    back.setHistoryMove(2);
    T.add( Edit.formatText(iwrb.getLocalizedString("sure_to_delete","Do really want to delete")),1,1);
    T.add( del,1,2);
     T.add( back,3,2);
    F.add(T);
    return F;
  }

  private void deleteText(IWContext iwc){
    String sTextId = iwc.getParameter("text_id");
    if(sTextId !=null){
      try {
        int id = Integer.parseInt(sTextId);
        ContractText CT = new ContractText(id);
        CT.delete();
      }
      catch (SQLException ex) {
      }
    }
  }

  private void updateForm(IWContext iwc){
    String sTextId = iwc.getParameter("text_id");
    String sOrdinal = iwc.getParameter("ordinal");
    String sName = iwc.getParameter("name");
    String sText = iwc.getParameter("texti");
    String sUseTags = iwc.getParameter("usetags");

    ContractText CT = null;
    boolean bInsert = true;
    if(sTextId != null){
      try{

        CT = new ContractText(Integer.parseInt(sTextId));
        bInsert = false;
      }
      catch(SQLException ex){ex.printStackTrace();}
    }
    else{

      CT = new ContractText();
      bInsert = true;
    }
    if(CT !=null){
      CT.setName(sName);
      CT.setText(sText);
      CT.setOrdinal(Integer.parseInt(sOrdinal));
      CT.setLanguage("IS");
      if(sUseTags!=null)
        CT.setUseTags(true);
      else
        CT.setUseTags(false);

      try {
        if(bInsert)
          CT.insert();
        else
          CT.update();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

   private void updateTitleForm(IWContext iwc){
    String sTextId = iwc.getParameter("title_id");
    String sText = iwc.getParameter("tname");

    ContractText CT = null;
    boolean bInsert = true;
    if(sTextId != null){
      try{
        CT = new ContractText(Integer.parseInt(sTextId));
        bInsert = false;
      }
      catch(SQLException ex){ex.printStackTrace();}
    }
    else{

      CT = new ContractText();
      bInsert = true;
    }
    if(CT !=null){
      CT.setName("title");
      CT.setText(sText);
      CT.setOrdinal(-1);
      CT.setLanguage(TIS);
      CT.setUseTags(false);

      try {
        if(bInsert)
          CT.insert();
        else
          CT.update();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  private List listOfTexts(){
    List L = null;

    try {
      ContractText CT = new ContractText();
      L = EntityFinder.findAllByColumnOrdered(CT,CT.getLanguageColumnName(),IS,CT.getOrdinalColumnName());
    }
    catch (SQLException ex) {

    }
    return L;
  }

  private ContractText getTitle(){
    List L = null;

    try {
      ContractText CT = new ContractText();
      L = EntityFinder.findAllByColumnOrdered(CT,CT.getLanguageColumnName(),TIS,CT.getOrdinalColumnName());
      if(L!= null)
        return (ContractText) L.get(0);
      else
        return null;
    }
    catch (SQLException ex) {
      return null;
    }
  }


  private Link getUpLink(){
    return new Link(new Image("/pics/list.gif"));
  }

  private Link getNewLink(){
    Link newLink = new Link(new Image("/pics/new.gif"));
    newLink.addParameter("new_text","new");
    return newLink;
  }

  public Link getPDFLink(PresentationObject MO){

    Window W ;
    if(useObjectInstanciator)
      W = new Window("PDF",ContractFiler.class,com.idega.presentation.Page.class);
    else
      W = new Window("PDF","/allocation/contractfile.jsp");
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("test","test");
    return L;
  }

  private DropdownMenu getTagDrop(String name){
    String[] tags = CampusContractWriter.getTags();
    DropdownMenu drp = new DropdownMenu(name);
    drp.addDisabledMenuElement("tag",iwrb.getLocalizedString("tags","Tags"));
    for (int i = 0; i < tags.length; i++) {
      drp.addMenuElement(" ["+tags[i]+"]",iwrb.getLocalizedString(tags[i],tags[i]));
    }
    return drp;
  }

  private DropdownMenu getIntegerDrop(String name,int from, int to){
    DropdownMenu drp = new DropdownMenu(name);
    for (int i = from; i <= to; i++) {
      drp.addMenuElement(i,String.valueOf(i));
    }
    return drp;
  }

  private TextArea getTextArea(String name,String content){
    TextArea TA = new TextArea(name,content);
    TA.setStyle(Edit.styleAttribute);
    TA.setWidth(80);
    TA.setHeight(20);
    return TA;
  }

   public void main(IWContext iwc){
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }

}
