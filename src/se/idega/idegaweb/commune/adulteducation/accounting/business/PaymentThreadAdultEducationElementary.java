package se.idega.idegaweb.commune.adulteducation.accounting.business;

import java.sql.Date;

import com.idega.block.school.data.School;
import com.idega.presentation.IWContext;

public class PaymentThreadAdultEducationElementary extends
		PaymentThreadAdultEducation {

	final static int SCHOOL_TYPE_ID = 66;
	
	public PaymentThreadAdultEducationElementary(Date month, IWContext iwc, School school, boolean testRun) {
		super(month, iwc, school, testRun);
	}

	public PaymentThreadAdultEducationElementary(Date month, IWContext iwc) {
		super(month, iwc);
	}
	
	public void run() {
	}
}