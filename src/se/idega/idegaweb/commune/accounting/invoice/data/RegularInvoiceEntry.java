/*
 * Created on 24.9.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;

import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.VATRegulation;

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
	User getUser();
	RegulationSpecType getRegSpecType();
	int getRegSpecTypeId();
	School getSchool(); 
	int getSchoolId();
	String getSchoolCategoryId();	
	String getOwnPosting();
	String getDoublePosting();
	float getAmount();
	float getVAT();
	VATRegulation getVatRegulation();
	int getVatRegulationId();	
	String getNote();
	Date getCreatedDate();
	String getCreatedName();
	Date getEditDate();
	String getEditName();
	
	void setFrom(Date from);
	void setTo(Date to);	
	void setPlacing(String plascint);
	void setUser(User user);
	void setRegSpecType(RegulationSpecType regType);
	void setRegSpecTypeId(int regTypeId);
	void setSchoolId(int schoolId);
	void setSchoolCategoryId(String s);	
	void setAmount(float amount);
	void setVAT(float vat);
	void setVatRegulation(VATRegulation vatRegulation);
	void setVatRegulationId(int vatRegId);	
	void setNote(String note);	
	void setOwnPosting(String ownPosting);
	void setDoublePosting(String doublePosting);
	void setCreatedDate(Date date);
	void setCreatedSign(String name);
	void setEditDate(Date date);
	void setEditSign(String name);
	
	void delete()throws java.sql.SQLException;

}
