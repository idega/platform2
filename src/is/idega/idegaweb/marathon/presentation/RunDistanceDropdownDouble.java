/*
 * Created on Jul 1, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.business.RunBusiness;
import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.SelectDropdownDouble;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

/**
 * Description: This class displays a double dropdown menu. The first menu displays <br>
 * a list of run groups (Rvk.marathon, Laugavegurinn, etc.) and the second menu displays<br>
 * a list of available distances run in the specific run (42 km, 21 km, etc.)<br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author birna
 */
public class RunDistanceDropdownDouble extends SelectDropdownDouble{
  
  public RunDistanceDropdownDouble() {
    super(IWMarathonConstants.GROUP_TYPE_RUN,IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
  }
  public void main(IWContext iwc) throws Exception {
    IWResourceBundle iwrb = getResourceBundle(iwc);
    RunBusiness runBiz = getRunBiz(iwc);
    
    Collection runs = runBiz.getRuns();
    
    addEmptyElement(iwrb.getLocalizedString("run_year_ddd.select_run","Select run..."),
        iwrb.getLocalizedString("run_year_ddd.select_distance","Select distance..."));
    
    IWTimestamp ts = IWTimestamp.RightNow();
    Integer y = new Integer(ts.getYear());
    String yearString = y.toString();
    
    Map dis = new LinkedHashMap();

    Iterator runIter = runs.iterator();
    while(runIter.hasNext()) {
      Group run = (Group) runIter.next();
      addMenuElement(run.getPrimaryKey().toString(),run.getName(),runBiz.getDistancesMap(run,yearString));
    }
    
    super.main(iwc);
  }
  /**
   * @see com.idega.presentation.ui.SelectDropdownDouble#getValue(java.lang.Object)
   */
  
  protected String getValue(IWContext iwc, Object value) {
    if (value instanceof String) {
      String str = (String) value;
      return str;
    }
    else {
      IWResourceBundle iwrb = iwc.getIWMainApplication().getBundle(IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
      return iwrb.getLocalizedString("run_year_ddd.select_distance","Select distance...");
    }
  }
  
  /**
   * @see com.idega.presentation.ui.SelectDropdownDouble#getKey(java.lang.Object)
   */
  protected String getKey(IWContext iwc, Object key) {
    if (key instanceof String) {
      String str = (String) key;
      return str;
    }
    else {
      return (String) key;
    }
  }
  public String getBundleIdentifier() {
    return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
  }
  
  private RunBusiness getRunBiz(IWContext iwc) throws IBOLookupException {
    RunBusiness business = (RunBusiness) IBOLookup.getServiceInstance(iwc,RunBusiness.class);
    return business;
  }
  private GroupBusiness getGroupBiz(IWContext iwc) throws IBOLookupException {
    GroupBusiness business = (GroupBusiness) IBOLookup.getServiceInstance(iwc,GroupBusiness.class);
    return business;
  }
  
  
  
  
}
