/*
 * Created on 28.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;

import se.idega.idegaweb.commune.accounting.regulations.data.VATRegulation;

import com.idega.block.school.data.School;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface RegularPaymentEntry extends IDOEntity {
	
	School getSchool(); 
	String getPlacing();	
	Date getFrom();
	Date getTo();	
	float getAmount();
	float getVAT();	
	String getNote();	
	String getOwnPosting();
	String getDoublePosting();
	VATRegulation getVatRegulation();	
	User getUser();	
	
	void setSchoolId(int schoolId);
	void setPlacing(String plascing);
	void setFrom(Date from);
	void setTo(Date to);	
	void setAmount(float amount);
	void setVAT(float vat);
	void setNote(String note);	
	void setOwnPosting(String ownPosting);
	void setDoublePosting(String doublePosting);
	void setVatRegulationId(int vatRegId);	
	void setUser(User user);
	
	void delete()throws java.sql.SQLException;	
}
