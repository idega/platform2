/*
 * Created on Jul 8, 2004
 */
package is.idega.idegaweb.marathon.presentation;

import is.idega.idegaweb.marathon.util.IWMarathonConstants;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.InterfaceObject;

/**
 * Description: <br>
 * Copyright: Idega Software 2004 <br>
 * Company: Idega Software <br>
 * @author birna
 */
public class RunYearSortDropdownTriple extends InterfaceObject{
    
    public void main(IWContext iwc) throws Exception{
        IWResourceBundle iwrb = getResourceBundle(iwc);
        RunYearDropdownDouble runYearDD = new RunYearDropdownDouble();
        
        DropdownMenu sortDD = new DropdownMenu(IWMarathonConstants.PARAMETER_SORT_BY);
        
        sortDD.addMenuElement(IWMarathonConstants.PARAMETER_TOTAL,iwrb.getLocalizedString(IWMarathonConstants.RYSDD_TOTAL,"Total result list"));
        sortDD.addMenuElement(IWMarathonConstants.PARAMETER_GROUPS,iwrb.getLocalizedString(IWMarathonConstants.RYSDD_GROUPS,"Groups"));
        sortDD.addMenuElement(IWMarathonConstants.PARAMETER_GROUPS_COMPETITION,iwrb.getLocalizedString(IWMarathonConstants.RYSDD_GROUPS_COMP,"Group competition"));
        Table t = new Table();
        t.setCellpadding(0);
        t.setCellspacing(0);
        t.add(runYearDD,1,1);
        t.add(sortDD,2,1);
        add(t);
        
    }

	public String getBundleIdentifier() {
		return IWMarathonConstants.IW_BUNDLE_IDENTIFIER;
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.ui.InterfaceObject#isContainer()
	 */
	public boolean isContainer() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.idega.presentation.ui.InterfaceObject#handleKeepStatus(com.idega.presentation.IWContext)
	 */
	public void handleKeepStatus(IWContext iwc) {
		// TODO Auto-generated method stub
		
	}

}
