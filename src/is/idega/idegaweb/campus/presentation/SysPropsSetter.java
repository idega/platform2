package is.idega.idegaweb.campus.presentation;

import is.idega.idegaweb.campus.business.CampusSettings;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.finance.presentation.Finance;
import com.idega.core.category.business.CategoryBusiness;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;


/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class SysPropsSetter extends CampusBlock{
 
  private static long day = 1;
  private static long month = day*30;
  private static long year = day*365;
  private CampusSettings settings = null;
  
  public SysPropsSetter() {

  }

  public String getLocalizedNameKey(){
    return "systemproperties";
  }

  public String getLocalizedNameValue(){
    return "Properties";
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc)throws RemoteException{
  	
  	if(isAdministrator(iwc)){
	  	settings  = getCampusSettings(iwc);
	  	if(iwc.isParameterSet("cmp_stng_save")){
	  		saveProperties(iwc);
	  	}
	    
	    DataTable T = getDataTable();
	    T.addTitle(localize("system_settings","System settings"));
	
	    Form myForm = new Form();
	    DateInput DI = new DateInput("contract_date",true);
	    DropdownMenu TI = intDrp("contract_years",10);
	    DropdownMenu term = termDrp("term");
	    TextInput termOfNotice = getTextInput("term_of_notice","",4);
	    termOfNotice.setAsIntegers(localize("please_use_only_integers","Please use only integers"));
	    termOfNotice.setLength(4);
	    TextInput adminEmail = getTextInput("admin_email");
	    TextInput emailHost = getTextInput("email_host");
	    DropdownMenu sendEventMails = new DropdownMenu("send_event_mail");
	    sendEventMails = (DropdownMenu) getStyledInterface(sendEventMails);
	    sendEventMails.addMenuElement(Boolean.toString(true),localize("boolean.true","Yes"));
	    sendEventMails.addMenuElement(Boolean.toString(false),localize("boolean.false","No"));
	    String[] filter2 = {com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticPermissionGroupInstance().getGroupTypeValue()};
	    DropdownMenu groups = null;
	    try {
	      String[] filter = new String[1];
	      filter[0] = com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticPermissionGroupInstance().getGroupTypeValue();
	      java.util.Collection permissionGroups = getUserService(iwc).getGroupHome().findAllGroups(filter,true);
	      groups = new DropdownMenu(permissionGroups,"def_group");
	    }
	    catch (Exception ex) {
	      groups = new DropdownMenu("def_group");
	    }
	    groups.addMenuElementFirst("-1",localize("group","Group"));
	    
	    DropdownMenu financeCategories = new DropdownMenu("finance_category");
	    financeCategories.addMenuElement("-1",localize("category","Category"));
	    try {
			Collection coll = CategoryBusiness.getInstance().getCategoryHome().findRootsByType(Finance.CATEGORY_TYPE);
			financeCategories.addMenuElements(coll);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (FinderException e) {
			e.printStackTrace();
		}
	    int row = 1;
	    /*
	    T.add(Edit.formatText(iwrb.getLocalizedString("contract_date","Contract date")),1,row);
	    T.add(DI,3,row);
	    if(SysProps.getContractDate()!=null){
	      //DI.setDate(SysProps.getContractDate());
	      T.add(getText(new IWTimestamp(SysProps.getContractDate()).toString()),4,row);
	    }
	    row++;
	    */
	    if(settings.getAdminEmail()!= null)
	      adminEmail.setContent(settings.getAdminEmail());
	    if(settings.getSmtpServer()!= null)
	      emailHost.setContent(settings.getSmtpServer());
	    	
	    sendEventMails.setSelectedElement(Boolean.toString(settings.getSendEventMail()));    
	
	    /*
	    T.add(Edit.formatText(iwrb.getLocalizedString("contract_years","Contract years")),1,row);
	    if(SysProps.getContractYears() > 0){
	      T.add(Edit.formatText(SysProps.getContractYears()),4,row);
	    }
	    T.add(TI,3,row);
	    row++;
	    */
	    T.add(getHeader(localize("term_of_notice","Term of notice")),1,row);
	    if(settings.getTermOfNoticeDays()!=null && settings.getTermOfNoticeDays().intValue() >= 0){
	      long iTerm = settings.getTermOfNoticeDays().intValue() ;
	      long i = iTerm/year;
	      String selected = "1";
	      if(i != 0){
	        iTerm = i;
	        selected = "2";
	      }
	      else if((i = iTerm/month) != 0 ){
	        iTerm = i;
	        selected = "1";
	      }
	      else{
	        selected = "0";
	      }
	      term.setSelectedElement(selected);
	      termOfNotice.setContent(String.valueOf(iTerm));
	    }
	    T.add(termOfNotice ,3,row);
	    T.add(term,3,row);
	    row++;
	    T.add(getHeader(localize("tenant_group","Tenant group")),1,row);
	    Integer groupId = settings.getTenantGroupID();
	    if(groupId!=null && groupId.intValue() > 0){
	      groups.setSelectedElement(String.valueOf(groupId));
	    }
	    T.add(groups,3,row);
	    row++;
	    T.add(getHeader(localize("finance_category","Finance Category")),1,row);
	    Integer finCatId = settings.getFinanceCategoryID();
	    if(finCatId!=null && finCatId.intValue() > 0){
	    	financeCategories.setSelectedElement(finCatId.toString());
	    }
	    T.add(financeCategories,3,row);
	    row++;
	    T.add(getHeader(localize("admin_email","Admin Email")),1,row);
	    T.add(adminEmail,3,row);
	    row++;
	    T.add(getHeader(localize("email_host","Email Host")),1,row);
	    T.add(emailHost,3,row);
	    row++;
	    T.add(getHeader(localize("send_event_mails","Send event mails")),1,row);
	    T.add(sendEventMails,3,row);
	    row++;
	    
	    SubmitButton save =(SubmitButton)getSubmitButton("cmp_stng_save","true","Save","save");
	    T.addButton(save);
	    myForm.add(T);
	    add(myForm);
  		}
  		else{
  			add( getNoAccessObject(iwc));
  		}
  }

  public void saveProperties(IWContext iwc) throws RemoteException{
    String contractDate = iwc.getParameter("contract_date");
    String contractYears = iwc.getParameter("contract_years");
    String adminEmail = iwc.getParameter("admin_email");
    String emailHost = iwc.getParameter("email_host");
    String defaultGroup = iwc.getParameter("def_group");
    String termOfNotice = iwc.getParameter("term_of_notice");
    String financeCategory = iwc.getParameter("finance_category");
    String term = iwc.getParameter("term");
    String sendEventMails = iwc.getParameter("send_event_mail");
     
    if(settings !=null){
      /*if(contractDate.length() == 10){
       settings.setContractDate(new IWTimestamp(contractDate).getSQLDate());
      }
      if(contractYears !=null){
         int years = -1;
        try{
           years = Integer.parseInt(contractYears);
        }
        catch(Exception e) {}

        SysProps.setContractYears(years);

      }
      */
      if(!"".equals(termOfNotice )){
        long iTerm = Long.parseLong(termOfNotice );
        if(!"".equals(term )){
          int i = Integer.parseInt(term);
          switch (i) {
            case 0:  iTerm *=day  ;       break;
            case 1:  iTerm *=month ;      break;
            case 2:  iTerm *=year  ;      break;
          }

        }
        settings.setTermOfNoticeDays(new Integer((int)iTerm));
      }
      else{
      	settings.setTermOfNoticeDays(new Integer(0));
      }
      if(!"".equals(adminEmail)){
       settings.setAdminEmail(adminEmail);
      }
      if(!"".equals(emailHost)){
        settings.setSmtpServer(emailHost);
      }
      if(!"".equals(defaultGroup)){
        settings.setTenantGroupID(Integer.valueOf(defaultGroup));
      }
      if(!"".equals(financeCategory)){
        settings.setFinanceCategoryID(Integer.valueOf(financeCategory));
      }
      if(!"".equals(sendEventMails)){
        settings.setSendEventMail(Boolean.valueOf(sendEventMails).booleanValue());
      }
     getCampusService(iwc).storeSettings(settings);

    }
    
  }

  private DropdownMenu termDrp(String name){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement("0",localize("day","Day"));
    drp.addMenuElement("1",localize("month","Month"));
    drp.addMenuElement("2",localize("year","Year"));
    drp.setSelectedElement("0");
    return drp;
  }

  private DropdownMenu intDrp(String name,int I){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(-1,"--");
    for (int i = 1; i <= I; i++) {
      drp.addMenuElement(String.valueOf(i));
    }
    return drp;
  }
   
}
