/*
 * Created on Jul 8, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author birna
 */
public class RunResultViewer  {
  
  private static String groupYear;
  private static String groupDistance;
  
  public RunResultViewer() {
    
  }
  
  public void main(IWContext iwc) {
    groupDistance = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
    int groupID = -1;
    if(groupDistance!=null && !groupDistance.equals("")) {
      groupID = Integer.parseInt(groupDistance);
    }
    Group distance = null;
    
    try {
      distance = getGroupBiz(iwc).getGroupByGroupID(groupID);
    } catch (RemoteException e) {
      e.printStackTrace();
    } catch (FinderException e) {
      e.printStackTrace();
    }
    Collection runners = getGroupBiz(iwc).getUsersFromGroupRecursive(distance);
    
  }
  
  private Table getFormTable() {
    Table table = new Table();
    return table;
  }
  private Table getResultTable() {
    Table table = new Table();
    return table;
  }
  private Table getHeaderTable() {
    Table table = new Table();
    return table;
  }
  private Table getGroupInfoTable() {
    Table table = new Table();
    return table;
  }
  private GroupBusiness getGroupBiz(IWContext iwc) {
    GroupBusiness business = null;
    try{
      business = (GroupBusiness) IBOLookup.getServiceInstance(iwc,GroupBusiness.class);
    }catch(IBOLookupException idoex) {
      business = null;
    }
    return business;
  }
  
}
