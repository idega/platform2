package is.idegaweb.campus.allocation;

import is.idegaweb.campus.presentation.Edit;
import com.idega.jmodule.object.ModuleObjectContainer;
import java.util.List;
import java.sql.SQLException;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.TextInput;
import com.idega.jmodule.object.interfaceobject.DropdownMenu;
import com.idega.jmodule.object.interfaceobject.Form;
import com.idega.jmodule.object.interfaceobject.SubmitButton;
import com.idega.jmodule.object.interfaceobject.DateInput;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.Image;
import is.idegaweb.campus.entity.SystemProperties;
import com.idega.util.idegaTimestamp;
import com.idega.data.EntityFinder;
import java.sql.SQLException;
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

public class SysPropsSetter extends ModuleObjectContainer{

  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private static String propParameter = SystemProperties.getEntityTableName();
  private boolean isAdmin = false;

  public SysPropsSetter() {

  }

  protected void control(ModuleInfo modinfo){
    SystemProperties SysProps = null;
    if(modinfo.getParameter("save")!=null){
      SysProps = saveProperties(modinfo);
      modinfo.setApplicationAttribute(propParameter,SysProps);
    }
    else if(modinfo.getApplicationAttribute(propParameter)!=null){
     SysProps = (SystemProperties) modinfo.getApplicationAttribute(propParameter);
    }
    else{
      SysProps = seekProperties();
      if(SysProps != null)
        modinfo.setApplicationAttribute(propParameter,SysProps);
      else{
        add(iwrb.getLocalizedString("no_sys_props","No System properties in database"));
      }
    }
    //add(getHomeLink());
    if(SysProps !=null)
      add(getProperties(SysProps));

  }

  public static boolean isSysPropsInMemoryElseLoad(ModuleInfo modinfo){
    if(modinfo.getApplicationAttribute(propParameter)!=null)
      return true;
    else
      return loadSystemProperties(modinfo);

  }

  public static boolean loadSystemProperties(ModuleInfo modinfo){
    SystemProperties Props = seekProperties();
      if(Props != null){
        modinfo.setApplicationAttribute(propParameter,Props);
        return true;
      }
      else
        return false;
  }

  public static SystemProperties seekProperties(){
    SystemProperties SysProps = null;
    try {
      List L = EntityFinder.findAll(new SystemProperties());
      if(L!=null){
       SysProps = (SystemProperties) L.get(0);
      }
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return SysProps;
  }

  private Link getHomeLink(){
    return new Link(new Image("/pics/list.gif"),"/allocation/index.jsp");
  }

  private ModuleObject getProperties(SystemProperties SysProps){
    Table T = new Table();
    Form myForm = new Form();
    DateInput DI = new DateInput("contract_date",true);
    DropdownMenu TI = intDrp("contract_years",10);
    TextInput adminEmail = new TextInput("admin_email");
    TextInput emailHost = new TextInput("email_host");
    DropdownMenu groups = new DropdownMenu(com.idega.core.user.business.UserBusiness.listOfGroups(),"def_group");
    int row = 1;
    T.add(Edit.formatText(iwrb.getLocalizedString("contract_date","Contract date")),1,row);
    T.add(DI,3,row);
    if(SysProps.getContractDate()!=null){
      //DI.setDate(SysProps.getContractDate());
      T.add(Edit.formatText(new idegaTimestamp(SysProps.getContractDate()).toString()),4,row);
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
    SubmitButton save = new SubmitButton("save","Save");
    T.add(save,4,7);
    myForm.add(T);
    return myForm;
  }

  public SystemProperties saveProperties(ModuleInfo modinfo){
    String contractDate = modinfo.getParameter("contract_date");
    String contractYears = modinfo.getParameter("contract_years");
    String adminEmail = modinfo.getParameter("admin_email");
    String emailHost = modinfo.getParameter("email_host");
    String defaultGroup = modinfo.getParameter("def_group");
    SystemProperties SysProps = seekProperties();
    if(SysProps !=null){
      if(contractDate.length() == 10){
        SysProps.setContractDate(new idegaTimestamp(contractDate).getSQLDate());
      }
      if(contractYears !=null){
         int years = -1;
        try{
           years = Integer.parseInt(contractYears);
        }
        catch(Exception e) {}

        SysProps.setContractYears(years);

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

  private DropdownMenu intDrp(String name,int I){
    DropdownMenu drp = new DropdownMenu(name);
    drp.addMenuElement(-1,"--");
    for (int i = 1; i <= I; i++) {
      drp.addMenuElement(String.valueOf(i));
    }
    return drp;
  }

  public void main(ModuleInfo modinfo){
    try{
      isAdmin = com.idega.core.accesscontrol.business.AccessControl.isAdmin(modinfo);
    }
    catch(SQLException sql){
      isAdmin = false;
    }
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);
    control(modinfo);
  }
}