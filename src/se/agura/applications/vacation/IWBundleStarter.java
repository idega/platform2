package se.agura.applications.vacation;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import se.agura.applications.vacation.business.VacationBusiness;
import se.agura.applications.vacation.business.VacationConstants;
import se.agura.applications.vacation.data.VacationType;
import se.agura.applications.vacation.data.VacationTypeHome;

import com.idega.block.process.business.CaseCodeManager;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
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
		
		insertStartData();
	}
	
	private void insertStartData() {
		try {
			VacationTypeHome home = (VacationTypeHome) IDOLookup.getHome(VacationType.class);
			VacationType type;
	
			try {
				type = home.findByName("Compensation vacation");
			}
			catch (FinderException fe) {
				type = home.create();
				type.setTypeName("Compensation vacation");
				type.setAllowsForwarding(false);
				type.setLocalizedKey("vacation_type.compensation");
				type.store();
			}
	
			try {
				type = home.findByName("Flextime");
			}
			catch (FinderException fe) {
				type = home.create();
				type.setTypeName("Flextime");
				type.setAllowsForwarding(false);
				type.setLocalizedKey("vacation_type.flextime");
				type.store();
			}
			
			try {
				type = home.findByName("Preacher leave");
			}
			catch (FinderException fe) {
				type = home.create();
				type.setTypeName("Preacher leave");
				type.setAllowsForwarding(false);
				type.setLocalizedKey("vacation_type.preacher_leave");
				type.store();
			}
			
			try {
				type = home.findByName("General");
			}
			catch (FinderException fe) {
				type = home.create();
				type.setTypeName("General");
				type.setAllowsForwarding(false);
				type.setLocalizedKey("vacation_type.general");
				type.store();
			}
			
			try {
				type = home.findByName("Parental leave");
			}
			catch (FinderException fe) {
				type = home.create();
				type.setTypeName("Parental leave");
				type.setAllowsForwarding(false);
				type.setLocalizedKey("vacation_type.parental_leave");
				type.setMetaData("parental_leave_type", "over_lowest_level,lowest_level,fathers_days", "com.idega.presentation.ui.RadioButton");
				type.setMetaData("child_personal_id", "", "com.idega.presentation.ui.TextInput");
				type.store();
			}
			
			try {
				type = home.findByName("Study break");
			}
			catch (FinderException fe) {
				type = home.create();
				type.setTypeName("Study break");
				type.setAllowsForwarding(false);
				type.setLocalizedKey("vacation_type.study_break");
				type.setMetaData("with_payment", "with,without", "com.idega.presentation.ui.RadioButton");
				type.store();
			}
			
			try {
				type = home.findByName("Official assignment");
			}
			catch (FinderException fe) {
				type = home.create();
				type.setTypeName("Official assignment");
				type.setAllowsForwarding(false);
				type.setLocalizedKey("vacation_type.official_assignment");
				type.setMetaData("explanation", "", "com.idega.presentation.ui.TextArea");
				type.store();
			}
			
			try {
				type = home.findByName("Private matter");
			}
			catch (FinderException fe) {
				type = home.create();
				type.setTypeName("Private matter");
				type.setAllowsForwarding(false);
				type.setLocalizedKey("vacation_type.private_matter");
				type.setMetaData("explanation", "", "com.idega.presentation.ui.TextArea");
				type.setMetaData("with_payment", "with,without", "com.idega.presentation.ui.RadioButton");
				type.store();
			}
			
			try {
				type = home.findByName("Union assignment");
			}
			catch (FinderException fe) {
				type = home.create();
				type.setTypeName("Union assignment");
				type.setAllowsForwarding(true);
				type.setLocalizedKey("vacation_type.union_assignment");
				type.setMetaData("explanation", "", "com.idega.presentation.ui.TextArea");
				type.setMetaData("file", "", "com.idega.block.media.presentation.FileChooser");
				type.setMetaData("with_payment", "with,without", "com.idega.presentation.ui.RadioButton");
				type.store();
			}

			try {
				type = home.findByName("Leave of absence");
			}
			catch (FinderException fe) {
				type = home.create();
				type.setTypeName("Leave of absence");
				type.setAllowsForwarding(true);
				type.setLocalizedKey("vacation_type.leave_of_absence");
				type.setMetaData("explanation", "", "com.idega.presentation.ui.TextArea");
				type.setMetaData("file", "", "com.idega.block.media.presentation.FileChooser");
				type.setMetaData("with_payment", "with,without", "com.idega.presentation.ui.RadioButton");
				type.store();
			}

			try {
				type = home.findByName("Other");
			}
			catch (FinderException fe) {
				type = home.create();
				type.setTypeName("Other");
				type.setAllowsForwarding(false);
				type.setLocalizedKey("vacation_type.other");
				type.setMetaData("explanation", "", "com.idega.presentation.ui.TextArea");
				type.setMetaData("with_payment", "with,without", "com.idega.presentation.ui.RadioButton");
				type.store();
			}
		}
		catch (IDOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
		catch (CreateException ce) {
			throw new IBORuntimeException(ce);
		}
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
}