/*
 * Created on Jul 13, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.StyledIWAdminWindow;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author birna
 */
public class CreateYearWindow extends StyledIWAdminWindow{
  
  public void main(IWContext iwc) throws Exception{
    IWResourceBundle iwrb = getResourceBundle(iwc);
    String runID = iwc.getParameter("ic_group_id");
    
    Group run = null;
    if(runID!=null && !runID.equals("")) {
      int id = Integer.parseInt(runID);
      run = getGroupBiz(iwc).getGroupByGroupID(id);
    }
    
    
    SubmitButton create = new SubmitButton(iwrb.getLocalizedString("run_reg.submit_step_one","Next step"),"action",String.valueOf(1));
    Table table = new Table();
    Form form = new Form();
    form.maintainParameter("ic_group_id");
    table.add(new TextInput("year"),1,1);
    table.add(create,1,2);
    form.add(table);
    add(form,iwc);
    switch(parseAction(iwc)) {
    case 1:
      getRunBiz(iwc).createNewGroupYear(iwc,run,iwc.getParameter("year"));
      close();
    }
  }
  private int parseAction(IWContext iwc) {
    if (iwc.isParameterSet("action")) {
      return Integer.parseInt(iwc.getParameter("action"));
    }
    else {
      return 0;
    }
  }
  private RunBusiness getRunBiz(IWContext iwc){
    RunBusiness business = null;
    try{
      business = (RunBusiness) IBOLookup.getServiceInstance(iwc,RunBusiness.class);
    }catch(IBOLookupException e) {
      business = null;
    }
    return business;  
  }
  private GroupBusiness getGroupBiz(IWContext iwc) throws IBOLookupException {
    GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(iwc,GroupBusiness.class);
    return business;
  }
}
