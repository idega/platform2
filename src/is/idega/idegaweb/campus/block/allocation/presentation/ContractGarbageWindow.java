package is.idega.idegaweb.campus.block.allocation.presentation;


import is.idega.idegaweb.campus.block.allocation.business.*;
import is.idega.idegaweb.campus.presentation.Edit;
import is.idega.idegaweb.campus.block.allocation.data.*;
import is.idega.idegaweb.campus.data.SystemProperties;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.PresentationObjectContainer;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.data.*;
import com.idega.block.application.data.Applicant;

import com.idega.util.IWTimestamp;
import java.sql.SQLException;

import com.idega.core.contact.data.Email;
import com.idega.core.user.business.UserBusiness;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.data.Account;
import com.idega.core.accesscontrol.business.LoginCreator;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.user.data.User;
import com.idega.core.data.GenericGroup;
import com.idega.core.accesscontrol.data.LoginTable;
import java.util.List;
import com.idega.util.SendMail;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class ContractGarbageWindow extends Window{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  public final static String prmContractId = "cam_contract_id";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private int iContractId = -1;

  /*
    Blár litur í topp # 27324B
    Hvítur litur fyrir neðan það # FFFFFF
    Ljósblár litur í töflu # ECEEF0
    Auka litur örlítið dekkri (í lagi að nota líka) # CBCFD3
  */

  public ContractGarbageWindow() {
    setWidth(530);
    setHeight(370);
    setResizable(true);
  }

  protected void control(IWContext iwc){
    //   debugParameters(iwc);
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    init(iwc);
    if(iwc.isParameterSet("save") || iwc.isParameterSet("save.x")){
      if(doGarbageContract(iwc)){
        add(Edit.formatText(iwrb.getLocalizedString("contract_was_garbaged","Contract was garbaged")));
        this.setParentToReload();
        this.close();
      }
     else
        add(Edit.formatText(iwrb.getLocalizedString("contract_was_not_garbaged","Contract could not be garbaged")));
    }
    else if(iContractId >0)
      add(getEditTable(iwc));
    else
      add(Edit.formatText(iwrb.getLocalizedString("no_contract_to_garbage","No contract to garbage")));

  }

  private void init(IWContext iwc){
    iContractId = Integer.parseInt( iwc.getParameter(prmContractId));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  private PresentationObject getEditTable(IWContext iwc){

    //Table T = new Table(2,8);
    DataTable T = new DataTable();
    T.setUseTitles(false);
    T.setUseTop(false);
    T.setUseBottom(false);
    T.setWidth("100%");
    T.addTitle(iwrb.getLocalizedString("contract_garbage","Contract garbage"));
    T.add(new HiddenInput(prmContractId,String.valueOf(iContractId)));
    T.addButton(new CloseButton(iwrb.getImage("close.gif")));
    T.addButton(new SubmitButton(iwrb.getImage("save.gif"),"save"));

    int row = 1;
    int col = 1;

    T.add(Edit.formatText(iwrb.getLocalizedString("garbage_are_you_sure","Are you sure ?")),1,1);

    Form F = new Form();
    F.add(T);
    return F;
  }

  private boolean doGarbageContract(IWContext iwc){

      int id = iContractId;
      if(id>0){
        ContractBusiness.doGarbageContract(id);
        return true;
      }

    return false;
  }



  public void main(IWContext iwc) throws Exception {
    control(iwc);
  }

}
