package is.idega.idegaweb.campus.block.allocation.presentation;


import is.idega.idegaweb.campus.block.allocation.business.CampusContractWriter;
import is.idega.idegaweb.campus.block.allocation.data.ContractText;
import is.idega.idegaweb.campus.block.allocation.data.ContractTextBMPBean;
import is.idega.idegaweb.campus.block.allocation.data.ContractTextHome;
import is.idega.idegaweb.campus.presentation.CampusBlock;
import is.idega.idegaweb.campus.presentation.Edit;

import java.util.Collection;
import java.util.Iterator;

import com.idega.data.IDOLookup;
import com.idega.data.SimpleQuerier;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.BackButton;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;


/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class ContractTextSetter extends CampusBlock{

 
  private final static String IS ="IS";
  private final static String EN ="EN";
  private final static String TIS ="TIS";
  private final static String TEN ="TEN";

  private String propParameter = is.idega.idegaweb.campus.data.SystemPropertiesBMPBean.getEntityTableName();
  private String localesParameter="iw_locales";
  private String bottomThickness = "8";
  private boolean isAdmin;
  private boolean useObjectInstanciator = false;


  public ContractTextSetter() {

  }

  public String getLocalizedNameKey(){
    return "contracttext";
  }

  public String getLocalizedNameValue(){
    return "Contracttext";
  }
  public void setToUseObjectInstanciator(boolean use){
    useObjectInstanciator=use;
  }

  protected void control(IWContext iwc){

    if(isAdmin){
      //add(getPDFLink(iwb.getImage("print.gif")));
      if(iwc.getParameter("savetitle")!=null){
          updateTitleForm(iwc);
          add(getMainTable(iwc));

      }
      else if(iwc.getParameter("savetext")!=null){
        updateForm(iwc);
        add(getMainTable(iwc));
      }
      else if( iwc.getParameter("delete")!= null){
        add (ConfirmDelete(iwc));
      }
      else if(iwc.getParameter("conf_delete")!=null){
        deleteText(iwc);
        add(getMainTable(iwc));
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
        add(getMainTable(iwc));
      }

    }
    else
      add(getNoAccessObject(iwc));


  }

  private PresentationObject getMainTable(IWContext iwc){
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    Collection L = listOfTexts(iwc);
    ContractText Title = getTitle(iwc);
    String sTitle = "";
    Link newTitleLink = new Link();
    if(Title != null){
      sTitle = Title.getText();
      newTitleLink.addParameter("title_id",Title.getPrimaryKey().toString());
    }
    else{
      sTitle = localize("new_title","New Title");
      newTitleLink.addParameter("new_title","new_title");
    }
    newTitleLink.setText(sTitle);


    int row = 1;
    T.add(getPDFLink(getBundle().getImage("print.gif")),1,row);
    T.add(getNewLink(),2,row);
    row++;
    T.add(Edit.titleText(localize("header","Header")),1,row);
    row++;
    T.add(newTitleLink,2,row);
    row++;
    T.add(Edit.titleText(localize("order","Order")),1,row);
    T.add(Edit.titleText(localize("title","Title")),2,row);
    row++;
    if(L!=null){
      int len = L.size();
      ContractText CT;
      for (Iterator iter = L.iterator(); iter.hasNext();) {
		CT = (ContractText) iter.next();
        Link link = new Link(CT.getName());
        link.addParameter("text_id",CT.getPrimaryKey().toString());
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

      T.add(localize("no_texts","No text in database"),1,2);

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
        ContractText CT = ((ContractTextHome)IDOLookup.getHome(ContractText.class)).findByPrimaryKey(new Integer(sId));
        text = new TextInput("tname",CT.getText());
        HiddenInput HI = new HiddenInput("title_id",sId);
        T.add(HI);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    else{
      text = new TextInput("tname");
    }
    SubmitButton save = new SubmitButton("savetitle","Save");
    text.setLength(80);
    T.add(getUpLink(),1,row++);
    T.add(Edit.formatText(localize("text","Text")),1,row++);
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
        ContractText CT = ((ContractTextHome)IDOLookup.getHome(ContractText.class)).findByPrimaryKey(new Integer(sId));
        name = new TextInput("name",CT.getName());
        text = getTextArea("texti",CT.getText());

        CB.setChecked(CT.getUseTags());
        intDrop.setSelectedElement(String.valueOf(CT.getOrdinal()));
        HiddenInput HI = new HiddenInput("text_id",sId);
        T.add(HI);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    else{
      try {
        name = new TextInput("name");
        text = getTextArea("texti","");
        int max = 1+ SimpleQuerier.executeIntQuery("select max("+ContractTextBMPBean.getOrdinalColumnName()+") from "+ContractTextBMPBean.getEntityTableName() );
        intDrop.setSelectedElement(String.valueOf(max));
       }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    SubmitButton save = new SubmitButton("savetext","Save");
    SubmitButton delete = new SubmitButton("delete","Delete");
    DropdownMenu tagDrop = getTagDrop("tags");
    tagDrop.setOnChange("this.form.texti.value += this.options[this.selectedIndex].value;");
    name.setLength(80);
    T.add(Edit.formatText(localize("title","Title")),1,row++);
    T.add(name,1,row++);
    T.add(Edit.formatText(localize("text","Text")),1,row++);
    T.add(text,1,row++);

    Table bottomTable = new Table();
    bottomTable.setWidth("100%");
	bottomTable.add(Edit.formatText(localize("order","Order")),1,1);
    bottomTable.add(intDrop,1,1);
    bottomTable.add(Edit.formatText(localize("set_to_parse","Parse ? ")),2,1);
    bottomTable.add(CB,2,1);
    bottomTable.add(save,3,1);
	bottomTable.add(Edit.formatText(localize("tag","Tag:")),4,1);
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
    SubmitButton del = new SubmitButton("conf_delete",localize("ok","OK"));
    BackButton back = new BackButton(localize("cancel","Cancel"));
    back.setHistoryMove(2);
    T.add( Edit.formatText(localize("sure_to_delete","Do really want to delete")),1,1);
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
        ContractText CT = ((ContractTextHome)IDOLookup.getHome(ContractText.class)).findByPrimaryKey(new Integer(id));
        CT.remove();
      }
      catch (Exception ex) {
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
	try{
    if(sTextId != null){
        CT = ((ContractTextHome)IDOLookup.getHome(ContractText.class)).findByPrimaryKey(new Integer(sTextId));
    }
    else{
      CT = ((ContractTextHome)IDOLookup.getHome(ContractText.class)).create();
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

        CT.store();
      }}
      
      catch (Exception ex) {
        ex.printStackTrace();
      }
    
  }

   private void updateTitleForm(IWContext iwc){
    String sTextId = iwc.getParameter("title_id");
    String sText = iwc.getParameter("tname");

    ContractText CT = null;
	try{
    if(sTextId != null){
        CT = ((ContractTextHome)IDOLookup.getHome(ContractText.class)).findByPrimaryKey(new Integer(sTextId));
    }
    else{
      CT = ((ContractTextHome)IDOLookup.getHome(ContractText.class)).create();
    }
    if(CT !=null){
      CT.setName("title");
      CT.setText(sText);
      CT.setOrdinal(-1);
      CT.setLanguage(TIS);
      CT.setUseTags(false);
       CT.store();
      }
	}
      catch (Exception ex) {
        ex.printStackTrace();
      }
  }

  private Collection listOfTexts(IWContext iwc){
    Collection texts = null;

    try {
	  texts = getContractService(iwc).getContractTextHome().findByLanguage(IS);
      //L = EntityFinder.getInstance().findAllByColumnOrdered(ContractText.class,ContractTextBMPBean.getLanguageColumnName(),IS,ContractTextBMPBean.getOrdinalColumnName());
    }
    catch (Exception ex) {

    }
    return texts;
  }

  private ContractText getTitle(IWContext iwc){
	Collection texts = null;

    try {
		texts = getContractService(iwc).getContractTextHome().findByLanguage(TIS);
      
      if(texts!= null && !texts.isEmpty())
        return (ContractText) texts.iterator().next();
      else
        return null;
    }
    catch (Exception ex) {
      return null;
    }
  }


  private Link getUpLink(){
    return new Link(getBundle().getImage("list.gif"));
  }

  private Link getNewLink(){
    Link newLink = new Link(getBundle().getImage("new.gif"));
    newLink.addParameter("new_text","new");
    return newLink;
  }

  public Link getPDFLink(PresentationObject MO){
    Link L = new Link(MO);
		L.setWindowToOpen(ContractFilerWindow.class);
    L.addParameter("test","test");
    return L;
  }

  private DropdownMenu getTagDrop(String name){
    String[] tags = CampusContractWriter.getTags();
    DropdownMenu drp = new DropdownMenu(name);
    drp.addDisabledMenuElement("tag",localize("tags","Tags"));
    for (int i = 0; i < tags.length; i++) {
      drp.addMenuElement(" ["+tags[i]+"]",localize(tags[i],tags[i]));
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
    TA.setStyleClass(Edit.styleAttribute);
    TA.setColumns(80);
    TA.setRows(20);
    return TA;
  }

   public void main(IWContext iwc){
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }
  


}
