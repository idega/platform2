package is.idegaweb.campus.allocation;

import is.idegaweb.campus.entity.SystemProperties;
import is.idegaweb.campus.entity.ContractText;
import com.idega.idegaweb.*;
import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
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

public class ContractTextSetter extends com.idega.jmodule.object.ModuleObjectContainer{

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
  protected int fontSize = 2;
  protected boolean fontBold = false;
  protected String styleAttribute = "font-size: 8pt";
  private int iBorder = 2;
  private String TextFontColor = "#000000",whiteColor = "#FFFFFF",blackColor = "#000000";
  private String redColor = "#942829",blueColor = "#27324B",lightBlue ="#ECEEF0";

  public ContractTextSetter() {

  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  protected void control(ModuleInfo modinfo){
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);

    if(isAdmin){
      //add(getPDFLink(new Image("/pics/print.gif")));
      if(modinfo.getParameter("savetitle")!=null){
          add(getHomeLink());
          updateTitleForm(modinfo);
          add(getMainTable());

      }
      else if(modinfo.getParameter("savetext")!=null){
        add(getHomeLink());
        updateForm(modinfo);
        add(getMainTable());
      }
      else if(modinfo.getParameter("delete")!=null){
        deleteText(modinfo);
        add(getHomeLink());
        add(getMainTable());
      }
      else if(modinfo.getParameter("text_id")!=null || modinfo.getParameter("new_text")!=null){
        add(getUpLink());
        add(getSetupForm(modinfo));
      }
      else if(modinfo.getParameter("new_title")!=null){
        add(getUpLink());
        add(getTitleForm(modinfo));
      }
      else if(modinfo.getParameter("title_id")!=null){
        add(getUpLink());
        add(getTitleForm(modinfo));
      }
      else {
        add(getHomeLink());
        add(getMainTable());
      }

    }
    else
      add(iwrb.getLocalizedString("access_denied","Access_denied"));


  }

  private ModuleObject getMainTable(){
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
    TextFontColor= whiteColor;
    T.add(formatText(iwrb.getLocalizedString("header","Header")),1,row);
    row++;
    T.add(newTitleLink,2,row);
    row++;
    T.add(formatText(iwrb.getLocalizedString("order","Order")),1,row);
    T.add(formatText(iwrb.getLocalizedString("title","Title")),2,row);
    TextFontColor = blackColor;
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
      T.setHorizontalZebraColored(lightBlue,whiteColor);
      T.setRowColor(1,whiteColor);
      T.setRowColor(2,blueColor);
      T.setRowColor(4,blueColor);
      T.setRowColor(row,redColor);
      T.setWidth(1,"30");
      T.mergeCells(1,2,2,2);

      T.mergeCells(1,row,8,row);
      T.add(formatText(" "),1,row);
      T.setHeight(row,bottomThickness);
    }
    else{
      T.add(iwrb.getLocalizedString("no_texts","No text in database"),1,2);
    }


    return T;
  }

  private ModuleObject getTitleForm(ModuleInfo modinfo){
    Form F = new Form();
    Table T = new Table();
    int row = 1;
    TextInput text = null;
    String sId = modinfo.getParameter("title_id");
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
    T.add(formatText(iwrb.getLocalizedString("text","Text")),1,row++);
    T.add(text,1,row++);
    T.add(save,1,row);
    F.add(T);
    return (F);
  }

  private ModuleObject getSetupForm(ModuleInfo modinfo){
    Table Frame = new Table(2,1);
    Table T = new Table();

    T.add(getNewLink(),1,1);

    int row = 2;
    DropdownMenu intDrop = getIntegerDrop("ordinal",1,100);
    TextInput name = null;
    TextArea text = null;
    CheckBox CB = new CheckBox("usetags","true");
    String sId = modinfo.getParameter("text_id");
    if(sId!=null){
      try {
        ContractText CT = new ContractText(Integer.parseInt(sId));
        name = new TextInput("name",CT.getName());
        text = getTextArea("text",CT.getText());

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
        text = getTextArea("text","");
        int max = new ContractText().getMaxColumnValue(ContractText.getOrdinalColumnName())+1;
        intDrop.setSelectedElement(String.valueOf(max));
       }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    SubmitButton save = new SubmitButton("savetext","Save");
    SubmitButton delete = new SubmitButton("delete","Delete");
    name.setLength(80);
    T.add(formatText(iwrb.getLocalizedString("title","Title")),1,row++);
    T.add(name,1,row++);
    T.add(formatText(iwrb.getLocalizedString("text","Text")),1,row++);
    T.add(text,1,row++);
    T.add(intDrop,1,row);
    T.add(CB,1,row);
    T.add(save,1,row);
    T.add(delete,1,row);



    String[] tags = CampusContractWriter.getTags();
    Table T2 = new Table();
    row = 1;
    T2.add("Tags",1,row++);
    for (int i = 0; i < tags.length; i++) {
      T2.add(tags[i],1,row++);
    }

    Frame.add(T,1,1);
    Frame.add(T2,2,1);
    Frame.setBorder(1);

    Form myForm = new Form();
    myForm.add(Frame);
    return myForm;
  }

  private void deleteText(ModuleInfo modinfo){
    String sTextId = modinfo.getParameter("text_id");
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

  private void updateForm(ModuleInfo modinfo){
    String sTextId = modinfo.getParameter("text_id");
    String sOrdinal = modinfo.getParameter("ordinal");
    String sName = modinfo.getParameter("name");
    String sText = modinfo.getParameter("text");
    String sUseTags = modinfo.getParameter("usetags");

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

   private void updateTitleForm(ModuleInfo modinfo){
    String sTextId = modinfo.getParameter("title_id");
    String sText = modinfo.getParameter("tname");

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

  private Link getHomeLink(){
    return new Link(new Image("/pics/list.gif"),"/allocation/index.jsp");
  }
  private Link getUpLink(){
    return new Link(new Image("/pics/list.gif"));
  }

  private Link getNewLink(){
    Link newLink = new Link(new Image("/pics/new.gif"));
    newLink.addParameter("new_text","new");
    return newLink;
  }

  public Link getPDFLink(ModuleObject MO){
    //Window W = new Window("PDF","/allocation/contractfile.jsp");
    Window W = new Window("PDF",ContractFiler.class,com.idega.jmodule.object.Page.class);
    W.setResizable(true);
    W.setMenubar(true);
    Link L = new Link(MO,W);
    L.addParameter("test","test");
    return L;
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
    TA.setStyle(this.styleAttribute);
    TA.setWidth(80);
    TA.setHeight(20);
    return TA;
  }

    public Text formatText(String s){
    Text T= new Text();
    if(s!=null){
      T= new Text(s);
      if(this.fontBold)
      T.setBold();
      T.setFontColor(this.TextFontColor);
      T.setFontSize(this.fontSize);
    }
    return T;
  }
  public Text formatText(int i){
    return formatText(String.valueOf(i));
  }

   public void main(ModuleInfo modinfo){
    try{
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){ isAdmin = false;}
    control(modinfo);
  }

}