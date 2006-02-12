/**
 * 
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;


import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * @author bluebottle
 *
 */
public interface PaymentHeader extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#getSchoolID
	 */
	public int getSchoolID();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#getSchool
	 */
	public School getSchool();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#getSchoolCategoryID
	 */
	public String getSchoolCategoryID();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#getSchoolCategory
	 */
	public SchoolCategory getSchoolCategory();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#getSignatureID
	 */
	public int getSignatureID();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#getStatus
	 */
	public char getStatus();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#getDateAttested
	 */
	public Date getDateAttested();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#getPeriod
	 */
	public Date getPeriod();

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#setSchoolID
	 */
	public void setSchoolID(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#setSchool
	 */
	public void setSchool(School s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#setSchoolCategoryID
	 */
	public void setSchoolCategoryID(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#setSchoolCategory
	 */
	public void setSchoolCategory(SchoolCategory s);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#setSignaturelID
	 */
	public void setSignaturelID(int i);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#setSignaturelID
	 */
	public void setSignaturelID(User u);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#setStatus
	 */
	public void setStatus(char c);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#setDateAttested
	 */
	public void setDateAttested(Date d);

	/**
	 * @see se.idega.idegaweb.commune.accounting.invoice.data.PaymentHeaderBMPBean#setPeriod
	 */
	public void setPeriod(Date d);

}
