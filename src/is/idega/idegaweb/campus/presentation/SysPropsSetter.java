package is.idega.idegaweb.campus.presentation;



import is.idega.idegaweb.campus.data.SystemProperties;

import java.sql.SQLException;
import java.util.List;

import com.idega.data.EntityFinder;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.DataTable;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.util.IWTimestamp;

/**
 * Title:   idegaclasses
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author  <a href="mailto:aron@idega.is">aron@idega.is
 * @version 1.0
 */

public class SysPropsSetter extends Block{

  private final static String IW_BUNDLE_IDENTIFIER="is.idega.idegaweb.campus";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private static String propParameter = is.idega.idegaweb.campus.data.SystemPropertiesBMPBean.getEntityTableName();
  private boolean isAdmin = false;
  private static long day = 1;
  private static long month = day*30;
  private static long year = day*365;

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

  protected void control(IWContext iwc){
    SystemProperties SysProps = null;
    if(iwc.isParameterSet("save")||iwc.isParameterSet("save.x")){
      SysProps = saveProperties(iwc);
      iwc.setApplicationAttribute(propParameter,SysProps);
    }
    else if(iwc.getApplicationAttribute(propParameter)!=null){
     SysProps = (SystemProperties) iwc.getApplicationAttribute(propParameter);
    }
    else{
      SysProps = seekProperties();
      if(SysProps != null)
        iwc.setApplicationAttribute(propParameter,SysProps);
      else{
        add(iwrb.getLocalizedString("no_sys_props","No System properties in database"));
      }
    }

    if(SysProps !=null)
      add(getProperties(SysProps));

  }

  public static boolean isSysPropsInMemoryElseLoad(IWContext iwc){
    if(iwc.getApplicationAttribute(propParameter)!=null)
      return true;
    else
      return loadSystemProperties(iwc);

  }

  public static SystemProperties getSystemProperties(IWContext iwc){
    if(isSysPropsInMemoryElseLoad( iwc))
      return (SystemProperties) iwc.getApplicationAttribute(propParameter);
    else
      return null;
  }

  public static boolean loadSystemProperties(IWContext iwc){
    SystemProperties Props = seekProperties();
      if(Props != null){
        iwc.setApplicationAttribute(propParameter,Props);
        return true;
      }
      else
        return false;
  }

  public static SystemProperties seekProperties(){
    SystemProperties SysProps = null;
    try {
      List L = EntityFinder.getInstance().findAll(SystemProperties.class);
      if(L!=null){
       SysProps = (SystemProperties) L.get(0);
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return SysProps;
  }

  private PresentationObject getProperties(SystemProperties SysProps){
    DataTable T = new DataTable();
    T.addTitle(iwrb.getLocalizedString("system_properties","System properties"));

    Form myForm = new Form();
    DateInput DI = new DateInput("contract_date",true);
    DropdownMenu TI = intDrp("contract_years",10);
    DropdownMenu term = termDrp("term",iwrb);
    TextInput termOfNotice = new TextInput("term_of_notice");
    termOfNotice.setAsIntegers();
    termOfNotice.setLength(4);
    TextInput adminEmail = new TextInput("admin_email");
    TextInput emailHost = new TextInput("email_host");
    String[] filter2 = {com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticPermissionGroupInstance().getGroupTypeValue()};
    DropdownMenu groups = null;
    try {
      String[] filter = new String[1];
      filter[0] = com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticPermissionGroupInstance().getGroupTypeValue();
      groups = new DropdownMenu(com.idega.core.data.GenericGroupBMPBean.getAllGroups(filter,true),"def_group");
    }
    catch (SQLException ex) {
      groups = new DropdownMenu("def_group");
    }

    int row = 1;
    T.add(Edit.formatText(iwrb.getLocalizedString("contract_date","Contract date")),1,row);
    T.add(DI,3,row);
    if(SysProps.getContractDate()!=null){
      //DI.setDate(SysProps.getContractDate());
      T.add(Edit.formatText(new IWTimestamp(SysProps.getContractDate()).toString()),4,row);
    }
    row++;
    if(SysProps.getAdminEmail()!= null)
      adminEmail.setContent(SysProps.getAdminEmail());
    if(SysProps.getEmailHost()!= null)
      emailHost.setContent(SysProps.getEmailHost());

    T.add(Edit.formatText(iwrb.getLocalizedString("contract_years","Contract years")),1,row);
    if(SysProps.getContractYears() > 0){
      T.add(Edit.formatText(SysProps.getContractYears()),4,row);
    }
    T.add(TI,3,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("term_of_notice","Term of notice")),1,row);
    if(SysProps.getTermOfNotice() >= 0){
      long iTerm = SysProps.getTermOfNotice();
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
    T.add(Edit.formatText(iwrb.getLocalizedString("default_group","Default user group")),1,row);
    int groupId = SysProps.getDefaultGroup();
    if(groupId > 0){
      groups.setSelectedElement(String.valueOf(groupId));
    }
    T.add(groups,3,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("admin_email","Admin Email")),1,row);
    T.add(adminEmail,3,row);
    row++;
    T.add(Edit.formatText(iwrb.getLocalizedString("email_host","Email Host")),1,row);
    T.add(emailHost,3,row);
    SubmitButton save = new SubmitButton(iwrb.getLocalizedString("save","Save"),"save");
    T.addButton(save);
    myForm.add(T);
    return myForm;
  }

  public SystemProperties saveProperties(IWContext iwc){
    String contractDate = iwc.getParameter("contract_date");
    String contractYears = iwc.getParameter("contract_years");
    String adminEmail = iwc.getParameter("admin_email");
    String emailHost = iwc.getParameter("email_host");
    String defaultGroup = iwc.getParameter("def_group");
    String termOfNotice = iwc.getParameter("term_of_notice");
    String term = iwc.getParameter("term");
    SystemProperties SysProps = seekProperties();
    if(SysProps !=null){
      if(contractDate.length() == 10){
        SysProps.setContractDate(new IWTimestamp(contractDate).getSQLDate());
      }
      if(contractYears !=null){
         int years = -1;
        try{
           years = Integer.parseInt(contractYears);
        }
        catch(Exception e) {}

        SysProps.setContractYears(years);

      }
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
        SysProps.setTermOfNotice(iTerm);
      }
      else{
        SysProps.setTermOfNotice(0);
      }
      if(!"".equals(adminEmail)){
        SysProps.setAdminEmail(adminEmail);
      }
      if(!"".equals(emailHost)){
        SysProps.setEmailHost(emailHost);
      }
      if(!"".equals(defaultGroup)){
        SysProps.setDefaultGroup(Integer.parseInt(defaultGroup));
      }
      try {
        SysProps.update();
      }
      catch (SQLException ex) {
      }

    }
    return SysProps;
  }

  private DropdownMenu termDrp(String name,IWResourceBundle iwrb){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement("0",iwrb.getLocalizedString("day","Day"));
    drp.addMenuElement("1",iwrb.getLocalizedString("month","Month"));
    drp.addMenuElement("2",iwrb.getLocalizedString("year","Year"));
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

  public void main(IWContext iwc){
    try{
      isAdmin = iwc.hasEditPermission(this);
    }
    catch(Exception sql){
      isAdmin = false;
    }
    iwrb = getResourceBundle(iwc);
    iwb = getBundle(iwc);
    control(iwc);
  }
}
