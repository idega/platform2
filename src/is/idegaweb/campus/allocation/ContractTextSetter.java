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

public class ContractTextSetter extends KeyEditor{

  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  private final static String IS ="IS";
  private final static String EN ="EN";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private String propParameter = SystemProperties.getSystemPropertiesEnitityName();
  private String localesParameter="iw_locales";

  public ContractTextSetter(String sHeader) {
    super(sHeader);
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  protected void control(ModuleInfo modinfo){
    iwb = getBundle(modinfo);
    iwrb = getResourceBundle(modinfo);

    if(isAdmin){
      if(modinfo.getParameter("save")!=null){
        updateForm(modinfo);
        add(getHomeLink());
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
      else {
        add(getHomeLink());
        add(getMainTable());
      }
    }
    else
      add("Hefur ekki réttindi");


  }

  private ModuleObject getMainTable(){
    Table T = new Table();
    T.setCellpadding(0);
    T.setCellspacing(0);
    List L = listOfTexts();

    T.add(getNewLink(),1,1);
    if(L!=null){
      int len = L.size();
      ContractText CT;
      for (int i = 0; i < len; i++) {
        CT = (ContractText) L.get(i);
        Link link = new Link(CT.getName());
        link.addParameter("text_id",CT.getID());
        T.add(link,1,i+2);
      }
    }
    else{
      T.add("Enginn samnings texti í grunni",1,2);
    }


    return T;
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
    SubmitButton save = new SubmitButton("save","Save");
    SubmitButton delete = new SubmitButton("delete","Delete");
    name.setLength(80);
    T.add(name,1,row++);
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

  private Link getHomeLink(){
    return new Link(new Image("/pics/list.gif"),"/allocation/index.jsp");
  }
  private Link getUpLink(){
    return new Link(new Image("/pics/list.gif"));
  }

  private Link getNewLink(){
    Link newLink = new Link("Nýtt");
    newLink.addParameter("new_text","new");
    return newLink;
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

}