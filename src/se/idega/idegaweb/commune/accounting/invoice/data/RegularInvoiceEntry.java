/*
 * Created on 24.9.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.util.Date;

import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;

import com.idega.block.school.data.School;
import com.idega.user.data.User;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface RegularInvoiceEntry extends com.idega.data.IDOEntity{
	Date getFrom();
	Date getTo();
	String getPlacing();
	int getUserId();
	int getRegSpecTypeId();
	int getProviderId(); 
	String getOwnPosting();
	String getDoublePosting();
	float getAmount();
	float getVAT();
	int getVatRegulationID();
	String getNote();
	
	void setFrom(Date from);
	void setTo(Date to);	
	void setPlacing(String plascint);
	void setAmount(float amount);
	void setNote(String note);	
}
