package se.idega.idegaweb.commune.adulteducation.accounting.business;

import java.sql.Date;

import com.idega.block.school.data.School;
import com.idega.presentation.IWContext;

public class PaymentThreadAdultEducationSFI extends PaymentThreadAdultEducation {
	final static int SCHOOL_TYPE_ID = 68;
	
	public PaymentThreadAdultEducationSFI(Date month, IWContext iwc, School school, boolean testRun) {
		super(month, iwc, school, testRun);
	}

	public PaymentThreadAdultEducationSFI(Date month, IWContext iwc) {
		super(month, iwc);
	}
	
	public void run() {
		//Do nothing for now.
	}

}
