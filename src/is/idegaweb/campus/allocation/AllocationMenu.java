package is.idegaweb.campus.allocation;

import com.idega.jmodule.object.textObject.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.Table;
import com.idega.jmodule.object.ModuleObject;
import com.idega.jmodule.object.ModuleInfo;
import com.idega.jmodule.object.ModuleObjectContainer;
import com.idega.jmodule.object.FrameList;
import com.idega.block.finance.presentation.*;
import com.idega.block.application.data.*;
import com.idega.block.application.business.ApplicationFinder;
import is.idegaweb.campus.entity.SystemProperties;
import java.util.List;
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


public class AllocationMenu extends FrameList{

  protected final int ACT1 = 1,ACT2 = 2, ACT3 = 3,ACT4  = 4,ACT5 = 5;
  private final String strAction = "fin_action";
  private boolean isAdmin = false;
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  private final static String IW_BUNDLE_IDENTIFIER="is.idegaweb.campus.allocation";


  public void main(ModuleInfo modinfo){
    iwrb = getResourceBundle(modinfo);
    iwb = getBundle(modinfo);

    setLinkStyle("font-family: Verdana, Arial, sans-serif; font-weight: bold; font-size: 7pt; text-decoration: none;");
    makeLinkTable();
    setZebraColors("#FFFFFF","#ECECEC");
  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }


  public void makeLinkTable(){

    addToList(CampusApprover.class,iwrb.getLocalizedString("applications","Applications"),CampusAllocation.FRAME_NAME);
    addToList(CampusContracts.class,iwrb.getLocalizedString("contracts","Contracts"),CampusAllocation.FRAME_NAME);
    addToList(RoughOrderForm.class,iwrb.getLocalizedString("roughorder","Rough order"),CampusAllocation.FRAME_NAME);
    addToList(CampusAllocator.class,iwrb.getLocalizedString("allocate","Allocate"),CampusAllocation.FRAME_NAME);
    addToList(EmailSetter.class,iwrb.getLocalizedString("emails","Emails"),CampusAllocation.FRAME_NAME);
    addToList(ContractTextSetter.class,iwrb.getLocalizedString("contracttexts","Contract Texts"),CampusAllocation.FRAME_NAME);
    addToList(SubjectMaker.class,iwrb.getLocalizedString("subjects","Subjects"),CampusAllocation.FRAME_NAME);
    addToList(AprtTypePeriodMaker.class,iwrb.getLocalizedString("periods","Periods"),CampusAllocation.FRAME_NAME);
    addToList(SysPropsSetter.class,iwrb.getLocalizedString("properties","Properties"),CampusAllocation.FRAME_NAME);

  }


}