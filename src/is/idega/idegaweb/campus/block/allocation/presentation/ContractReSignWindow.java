package is.idega.idegaweb.campus.block.allocation.presentation;


import is.idega.idegaweb.campus.block.allocation.business.ContractBusiness;
import is.idega.idegaweb.campus.presentation.Edit;
import is.idega.idegaweb.campus.block.allocation.data.Contract;
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

import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import com.idega.core.data.Email;
import com.idega.core.user.business.UserBusiness;
import com.idega.block.finance.business.AccountManager;
import com.idega.block.finance.data.Account;
import com.idega.block.login.business.LoginCreator;
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

public class ContractReSignWindow extends Window{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private boolean isAdmin;
  private boolean isLoggedOn;
  private String login = null;
  private String passwd = null;
  private boolean print = false;
  private SystemProperties SysProps = null;
  private GenericGroup eGroup = null;
  private User eUser = null;

  /*
    Blár litur í topp # 27324B
    Hvítur litur fyrir neðan það # FFFFFF
    Ljósblár litur í töflu # ECEEF0
    Auka litur örlítið dekkri (í lagi að nota líka) # CBCFD3
  */

  public ContractReSignWindow() {
    setWidth(300);
    setHeight(300);
    setResizable(true);
  }

  protected void control(IWContext iwc){

    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);

    if(isAdmin || isLoggedOn){
      if(iwc.getApplicationAttribute(SysProps.getEntityTableName())!=null){
        SysProps = (SystemProperties)iwc.getApplicationAttribute(SysProps.getEntityTableName());
      }

      if(iwc.isParameterSet("save") || iwc.isParameterSet("save.x")){
        doReSignContract(iwc);
      }
      add(getSignatureTable(iwc));
    }
    else
      add(Edit.formatText(iwrb.getLocalizedString("access_denied","Access denied")));

    //add(String.valueOf(iSubjectId));
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public PresentationObject makeLinkTable(int menuNr){
    Table LinkTable = new Table(6,1);

    return LinkTable;
  }

  private PresentationObject getSignatureTable(IWContext iwc){
    int iContractId = Integer.parseInt( iwc.getParameter("contract_id"));
    //Table T = new Table(2,8);
    DataTable T = new DataTable();
    T.setWidth("100%");
    T.addTitle(iwrb.getLocalizedString("contract_resign","Contract resign"));
    T.addButton(new CloseButton(iwrb.getImage("close.gif")));
    T.addButton(new SubmitButton(iwrb.getImage("save.gif"),"save"));

    int row = 1;
    int col = 1;

    try{
      if(iContractId > 0){
        Contract eContract = new Contract(iContractId);
        User user = new User(eContract.getUserId().intValue());
        boolean isContractUser = user.getID() == eUser.getID();
        if(user !=null){
          T.add(new HiddenInput("contract_id",String.valueOf(eContract.getID())),1,row);
          T.add(Edit.formatText(iwrb.getLocalizedString("name","Name")),1,row);
          T.add(Edit.formatText(user.getName()),2,row);
          row++;
          T.add(Edit.formatText(iwrb.getLocalizedString("valid_from","Valid to")),1,row);
          T.add(Edit.formatText(new idegaTimestamp(eContract.getValidFrom()).getLocaleDate(iwc)),2,row);
          row++;
          T.add(Edit.formatText(iwrb.getLocalizedString("valid_to","Valid from")),1,row);
          T.add(Edit.formatText(new idegaTimestamp(eContract.getValidTo()).getLocaleDate(iwc)),2,row);
          row++;
          T.add(Edit.formatText(iwrb.getLocalizedString("moving_date","Moving date")),1,row);
          idegaTimestamp movdate = eContract.getMovingDate()!=null?new idegaTimestamp(eContract.getMovingDate()):null;
          DateInput movDate = new DateInput("mov_date",true);
          idegaTimestamp moving = idegaTimestamp.RightNow();
          int termofnotice = 1;
          if(SysProps !=null)
            termofnotice = (int)SysProps.getTermOfNoticeDays();
          moving.addDays(termofnotice);

          if(moving.isLaterThan(new idegaTimestamp(eContract.getValidTo())))
            movDate.setDate(eContract.getValidTo());
          else
            movDate.setDate(moving.getSQLDate());

          //Edit.setStyle(movDate);
          movDate.setStyleAttribute("style",Edit.styleAttribute);

          if(movdate !=null )
            movDate.setDate(movdate.getSQLDate());
          if(isAdmin || isContractUser)
            T.add(movDate,2,row);
          else if(movdate !=null)
            T.add(Edit.formatText(movdate.getLocaleDate(iwc)),2,row);
          row++;
          T.add(Edit.formatText(iwrb.getLocalizedString("new_address","New address")),1,row);
          T.add(new TextInput("new_address"),2,row);
          row++;
          T.add(Edit.formatText(iwrb.getLocalizedString("new_zip","New zip")),1,row);
          T.add(new TextInput("new_zip"),2,row);
          row++;
          T.add(Edit.formatText(iwrb.getLocalizedString("new_phone","New phone")),1,row);
          T.add(new TextInput("new_phone"),2,row);
          row++;


        }
      }
    }
    catch(SQLException ex){}

    Form F = new Form();
    F.add(T);
    return F;
  }

  private void doReSignContract(IWContext iwc){

    int id = Integer.parseInt(iwc.getParameter("contract_id"));
    String sInfo = iwc.getParameter("resign_info");
    if(sInfo == null)
      sInfo = "";
    String sMovDate = iwc.getParameter("mov_date");
    idegaTimestamp movDate = null;
    if(sMovDate !=null && sMovDate.length() == 10 )
      movDate = new idegaTimestamp(sMovDate);
    if(isAdmin)
      ContractBusiness.endContract(id,movDate,sInfo);
    else if(eUser !=null)
      ContractBusiness.resignContract(id,movDate,sInfo);
    close();
  }

  private void doAddEmail( int iUserId ,IWContext iwc){
    String sEmail = iwc.getParameter("new_email");
    UserBusiness.addNewUserEmail(iUserId,sEmail);
  }

  private PresentationObject getApartmentTable(Apartment A){
    Table T = new Table();
    Floor F = BuildingCacher.getFloor(A.getFloorId());
    Building B = BuildingCacher.getBuilding(F.getBuildingId());
    Complex C = BuildingCacher.getComplex(B.getComplexId());
    T.add(Edit.formatText(A.getName()),1,1);
    T.add(Edit.formatText(F.getName()),2,1);
    T.add(Edit.formatText(B.getName()),3,1);
    T.add(Edit.formatText(C.getName()),4,1);
    return T;
  }

  private String getApartmentString(Apartment A){
    StringBuffer S = new StringBuffer();
    Floor F = BuildingCacher.getFloor(A.getFloorId());
    Building B = BuildingCacher.getBuilding(F.getBuildingId());
    Complex C = BuildingCacher.getComplex(B.getComplexId());
    S.append(A.getName());S.append(" ");
    S.append(F.getName());S.append(" ");
    S.append(B.getName());S.append(" ");
    S.append(C.getName());
    return S.toString();
  }

  public void main(IWContext iwc) throws Exception {
    eUser = iwc.getUser();
    //isStaff = com.idega.core.accesscontrol.business.AccessControl
    isAdmin = iwc.hasEditPermission(this);
    isLoggedOn = com.idega.block.login.business.LoginBusiness.isLoggedOn(iwc);
    control(iwc);
  }
}
