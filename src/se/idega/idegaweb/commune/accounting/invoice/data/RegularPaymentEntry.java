/*
 * Created on 24.9.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.util.Date;

/**
 * @author Roar
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface RegularPaymentEntry extends com.idega.data.IDOEntity{
	Date getFrom();
	Date getTo();
	String getPlacing();
	Float getAmount();
	String getNote();
	
	String setFrom(Date from);
	String setTo(Date to);	
	String setPlacing(String plascint);
	String setAmount(Float amount);
	String setNote(String note);	
}
