package se.idega.idegaweb.commune.adulteducation.accounting.business;

import java.sql.Date;
import java.util.ArrayList;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolClassMember;
import com.idega.presentation.IWContext;

import se.idega.idegaweb.commune.accounting.invoice.business.BillingThread;

public class AdultEducationStarterThread extends BillingThread {

	private static BillingThread elementaryThread = null;
	
	private static BillingThread highSchoolThread  = null;
	
	private static BillingThread sfiThread = null;
	
	public AdultEducationStarterThread(Date month, IWContext iwc) {
		super(month, iwc);
	}

	public AdultEducationStarterThread(Date month, IWContext iwc, School school, boolean testRun) {
		super(month, iwc, school, testRun);
	}

	protected void setStudyPath(SchoolClassMember schoolClassMember,
			ArrayList conditions) {
	}

	public void terminate() {
		if (elementaryThread != null) {
			elementaryThread.terminate();
			elementaryThread = null;
		}

		if (highSchoolThread != null) {
			highSchoolThread.terminate();
			highSchoolThread = null;
		}
		
		if (sfiThread != null) {
			sfiThread.terminate();
			sfiThread = null;
		}
	}

	public void run() {
		elementaryThread = new PaymentThreadAdultEducationElementary(calculationDate, (IWContext)iwc, school, testRun);
		elementaryThread.start();

		highSchoolThread = new PaymentThreadAdultEducationHighSchool(calculationDate, (IWContext)iwc, school, testRun);
		highSchoolThread.start();

		sfiThread = new PaymentThreadAdultEducationSFI(calculationDate, (IWContext)iwc, school, testRun);
		sfiThread.start();
	}
}