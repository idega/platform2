package se.agura.applications.vacation;

import se.agura.applications.vacation.business.VacationBusiness;
import se.agura.applications.vacation.business.VacationConstants;

import com.idega.block.process.business.CaseCodeManager;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="laddi@idega.is">Thorhallur Helgason</a>
 * @version 1.0
 * Created on Dec 8, 2004
 */
public class IWBundleStarter implements IWBundleStartable {
	
	public void start(IWBundle starterBundle) {
		CaseCodeManager caseCodeManager = CaseCodeManager.getInstance();
		caseCodeManager.addCaseBusinessForCode(VacationConstants.CASE_CODE_KEY,VacationBusiness.class);
	}

	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
}