package is.idega.idegaweb.campus.block.finance.presentation;

import is.idega.idegaweb.campus.block.finance.business.CampusAssessmentBusiness;

import com.idega.block.finance.business.AssessmentBusiness;
import com.idega.block.finance.data.EntryGroup;
import com.idega.block.finance.presentation.EntryGroups;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.util.IWTimestamp;

public class CampusEntryGroups extends EntryGroups {

	public CampusEntryGroups() {
		super();
	}
	
	protected PresentationObject doGroup(IWContext iwc, IWTimestamp from, IWTimestamp to, IWTimestamp paymentDate, IWTimestamp dueDate) {
		try {
			AssessmentBusiness assBuiz = (AssessmentBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc,
					AssessmentBusiness.class);
			EntryGroup group = assBuiz.groupEntriesWithSQL(from, to, paymentDate, dueDate);
			
			CampusAssessmentBusiness camBiz = (CampusAssessmentBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc,
					CampusAssessmentBusiness.class);
			
			camBiz.createDKXMLFile(group, iwc.getCurrentLocale());
							
			return getHeader(localize(LOC_KEY_GROUP_CREATED, "Group was created"));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return getHeader(localize(LOC_KEY_GROUP_NOT_CREATED, "Group was not created"));
		}
	}
}
