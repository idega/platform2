/*
 * Created on Jul 9, 2004
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

/**
 * Description: This class displays a double dropdown menu. The first menu displays <br>
 * a list of year groups for a specific run (Rvk.marathon, Laugavegurinn, etc.) and the second menu displays<br>
 * a list of available distances for the specific run on the year selected<br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author birna
 */
public class YearDistanceDropdownDouble extends SelectDropdownDouble{
    
    public YearDistanceDropdownDouble() {
        super(IWMarathonConstants.GROUP_TYPE_RUN_YEAR,IWMarathonConstants.GROUP_TYPE_RUN_DISTANCE);
    }
    public void main(IWContext iwc) throws Exception {
        IWResourceBundle iwrb = getResourceBundle(iwc);
        RunBusiness runBiz = getRunBiz(iwc);
        GroupBusiness groupBiz = getGroupBiz(iwc);
        
        String runGroupID = iwc.getParameter(IWMarathonConstants.GROUP_TYPE_RUN);
        if(runGroupID!=null && !runGroupID.equals("")) {
            addEmptyElement(iwrb.getLocalizedString("year_dis_ddd.select_year","Select year..."),
                    iwrb.getLocalizedString("year_dis_ddd.select_distance","Select distance..."));

            int runID = Integer.parseInt(runGroupID);
            Group runGroup = groupBiz.getGroupByGroupID(runID);
            Collection years = runBiz.getYears(runGroup);
            Iterator yearIter = years.iterator();

            Map dis = new LinkedHashMap();
            dis.put("-1",iwrb.getLocalizedString("run_year_ddd.select_distance","Select distance..."));

            while(yearIter.hasNext()) {
                Group year = (Group) yearIter.next();
                dis.putAll(runBiz.getDistancesMap(runGroup,year.getName()));
                addMenuElement(year.getPrimaryKey().toString(),year.getName(),dis);
          
            }
        }else {
            addEmptyElement(iwrb.getLocalizedString("year_dis_ddd.no_run","No run is selected..."),
                    iwrb.getLocalizedString("year_dis_ddd.no_run","No run is selected..."));

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
