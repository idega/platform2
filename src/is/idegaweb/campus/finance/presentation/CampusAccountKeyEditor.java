package is.idegaweb.campus.finance.presentation;


//import com.idega.block.finance.presentation.*;
import is.idegaweb.campus.presentation.Edit;
import com.idega.block.finance.presentation.*;
import com.idega.block.finance.data.*;
import com.idega.block.finance.business.Finder;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.*;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.text.*;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import com.idega.data.EntityFinder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class CampusAccountKeyEditor extends PresentationObjectContainer {

  public String strAction = "ake_action";
  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  protected boolean isAdmin = false;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.finance";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;

  public CampusAccountKeyEditor(){

  }

   protected void control(IWContext iwc){

   if(isAdmin){
      try{
        PresentationObject MO = new Text();

        if(iwc.getParameter(strAction) == null){
          MO = getMain(iwc);
        }
        if(iwc.getParameter(strAction) != null){
          String sAct = iwc.getParameter(strAction);
          int iAct = Integer.parseInt(sAct);

          switch (iAct) {
            case ACT1 : MO = getMain(iwc);        break;
            case ACT2 : MO = getChange(iwc);      break;
            case ACT3 : MO = doUpdate(iwc);      break;
            default: MO = getMain(iwc);           break;
          }
        }
        Table T = new Table(1,3);
        add(Edit.headerText(iwrb.getLocalizedString("account_key_editor","Account key editor"),3));
        T.add(makeLinkTable(1));
        T.add(MO);
        T.setWidth("100%");
        add(T);

      }
      catch(Exception S){
        S.printStackTrace();
      }
    }
    else
      add(iwrb.getLocalizedString("access_denied","Access denies"));
  }

  protected PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(3,1);
    int last = 3;
    LinkTable.setWidth("100%");
    LinkTable.setCellpadding(2);
    LinkTable.setCellspacing(1);
    LinkTable.setColor(Edit.colorDark);
    LinkTable.setWidth(last,"100%");
    Link Link1 = new Link(iwrb.getLocalizedString("view","View"));
    Link1.setFontColor(Edit.colorLight);
    Link1.addParameter(this.strAction,String.valueOf(this.ACT1));
    Link Link2 = new Link(iwrb.getLocalizedString("change","Change"));
    Link2.setFontColor(Edit.colorLight);
    Link2.addParameter(strAction,String.valueOf(this.ACT2));
    if(isAdmin){
      LinkTable.add(Link1,1,1);
      LinkTable.add(Link2,2,1);
    }
    return LinkTable;
  }

  private PresentationObject getMain(IWContext iwc){

    AccountKey[] keys = Finder.findAccountKeys();
    int count = keys.length;
    Table keyTable = new Table(4,count+1);
    keyTable.setWidth("100%");
    keyTable.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    keyTable.setRowColor(1,Edit.colorMiddle);
    keyTable.setCellpadding(2);
    keyTable.setCellspacing(1) ;
    keyTable.add(Edit.formatText("Nr"),1,1);
    keyTable.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),2,1);
    keyTable.add(Edit.formatText(iwrb.getLocalizedString("info","Info")),3,1);
    keyTable.add(Edit.formatText(iwrb.getLocalizedString("account_key","Account key")),4,1);

    Hashtable hk = getKeys();
    if(isAdmin){
      if(count > 0){
        for (int i = 0;i < count;i++){
          keyTable.add(Edit.formatText(String.valueOf(i+1)),1,i+2);
          keyTable.add(Edit.formatText(keys[i].getName()),2,i+2);
          keyTable.add(Edit.formatText(keys[i].getInfo()),3,i+2);
          Integer tkid = new Integer(keys[i].getTariffKeyId());
          if(hk.containsKey(tkid))
            keyTable.add( Edit.formatText( (String)hk.get( tkid) ),4,i+2);
        }
      }
    }

    return(keyTable);
  }

  private PresentationObject getChange(IWContext iwc) throws SQLException{
    Form myForm = new Form();
    myForm.maintainAllParameters();
    AccountKey[] keys = Finder.findAccountKeys();
    int count = keys.length;
    int inputcount = count+5;
    Table inputTable =  new Table(5,inputcount+1);
    inputTable.setWidth("100%");
    //inputTable.setWidth(1,"15");
    inputTable.setCellpadding(2);
    inputTable.setCellspacing(1);
    //inputTable.setColumnAlignment(1,"right");
    inputTable.setHorizontalZebraColored(Edit.colorLight,Edit.colorWhite);
    inputTable.setRowColor(1,Edit.colorMiddle);
    inputTable.add(Edit.formatText("Nr"),1,1);
    inputTable.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),2,1);
    inputTable.add(Edit.formatText(iwrb.getLocalizedString("info","Inro")),3,1);
    inputTable.add(Edit.formatText(iwrb.getLocalizedString("account_key","Account key")),4,1);
    inputTable.add(Edit.formatText(iwrb.getLocalizedString("delete","Delete")),5,1);
    List TK = null;
    try{
      TK = EntityFinder.findAll(new TariffKey());
    }
    catch(SQLException sql){}

    for (int i = 1; i <= inputcount ;i++){
      String rownum = String.valueOf(i);
      String s = "";
      TextInput nameInput, infoInput;
      HiddenInput idInput;
      CheckBox delCheck;
      DropdownMenu iDrp =  keyDrp(TK);
      iDrp.setName("ake_keydrp"+i);
      Edit.setStyle(iDrp);
      int pos;
      if(i <= count ){
        pos = i-1;
        nameInput  = new TextInput("ake_nameinput"+i,(keys[pos].getName()));
        infoInput = new TextInput("ake_infoinput"+i,(keys[pos].getInfo()));
        String sId = String.valueOf(keys[pos].getID());
        idInput = new HiddenInput("ake_idinput"+i,sId);
        delCheck = new CheckBox("ake_delcheck"+i,"true");
        iDrp.setSelectedElement(String.valueOf(keys[pos].getTariffKeyId()));
        Edit.setStyle(delCheck);
        inputTable.add(delCheck,5,i+1);
      }
      else{
        nameInput  = new TextInput("ake_nameinput"+i);
        infoInput = new TextInput("ake_infoinput"+i);
        idInput = new HiddenInput("ake_idinput"+i,"-1");
      }
      nameInput.setSize(20);
      infoInput.setSize(40);

      Edit.setStyle(nameInput);
      Edit.setStyle(infoInput);

      inputTable.add(Edit.formatText(rownum),1,i+1);
      inputTable.add(nameInput,2,i+1);
      inputTable.add(infoInput,3,i+1);
      inputTable.add(iDrp,4,i+1);
      inputTable.add(idInput);
    }
    myForm.add(new HiddenInput("ake_count", String.valueOf(inputcount) ));
    myForm.add(new HiddenInput(this.strAction,String.valueOf(this.ACT3 )));
    myForm.add(inputTable);
    SubmitButton save = new SubmitButton(iwrb.getLocalizedString("save","Save"));
    Edit.setStyle(save);
    myForm.add(save);

    return (myForm);
  }

  private PresentationObject doUpdate(IWContext iwc) throws SQLException{
    int count = Integer.parseInt(iwc.getParameter("ake_count"));
    String sName,sInfo,sDel,sTKid;
    int ID,TKid;
    AccountKey key = null;

    for (int i = 1; i < count+1 ;i++){
      sName = iwc.getParameter("ake_nameinput"+i ).trim();
      sInfo = iwc.getParameter("ake_infoinput"+i).trim();
      sDel = iwc.getParameter("ake_delcheck"+i);
      sTKid = iwc.getParameter("ake_keydrp"+i);
      TKid = Integer.parseInt(sTKid);
      ID = Integer.parseInt(iwc.getParameter("ake_idinput"+i));
      if(ID != -1 && TKid > 0){
        try{
          key = new AccountKey(ID);
          if(sDel != null && sDel.equalsIgnoreCase("true")){
            key.delete();
          }
          else{
            key.setName(sName);
            key.setInfo(sInfo);
            key.setTariffKeyId(TKid);
            key.update();
          }
        }
        catch(SQLException e){
          e.printStackTrace();
        }
      }
      else {
        if(!sName.equalsIgnoreCase("")){
          try {
            key = new AccountKey();
            key.setName(sName);
            key.setInfo(sInfo);
            key.setTariffKeyId(TKid);
            key.insert();
          }
          catch (Exception ex) {
          }
        }
      }
    }// for loop

   return getMain(iwc);
  }

  private Hashtable getKeys(){
    Hashtable h = new Hashtable();
    List TK = null;
    try{
      TK = EntityFinder.findAll(new TariffKey());
    }
    catch(SQLException sql){}
    if(TK != null){
      int len = TK.size();
      for (int i = 0; i < len; i++) {
        TariffKey T = (TariffKey) TK.get(i);
        h.put(new Integer(T.getID()),T.getName());
      }
    }
    return h;

  }

  private DropdownMenu keyDrp(List TK){
    DropdownMenu drp = new DropdownMenu();
    drp.addMenuElement(0,"--");
    if(TK != null)
      drp.addMenuElements(TK);
    return drp;
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc){
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    control(iwc);
  }

}// class AccountKeyEditor
