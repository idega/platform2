/**
 * 
 */
package se.idega.idegaweb.commune.care.data;

import com.idega.block.school.data.SchoolSeason;
import com.idega.data.IDOEntity;


/**
 * <p>
 * TODO Dainis Describe Type AfterSchoolChoice
 * </p>
 *  Last modified: $Date: 2006/04/05 15:28:39 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.2.2.4 $
 */
public interface AfterSchoolChoice extends IDOEntity, ChildCareApplication {

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCaseCodeKey
	 */
	public String getCaseCodeKey();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCaseCodeDescription
	 */
	public String getCaseCodeDescription();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getSchoolSeasonId
	 */
	public int getSchoolSeasonId();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getSchoolSeason
	 */
	public SchoolSeason getSchoolSeason();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getPayerName
	 */
	public String getPayerName();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getPayerPersonalID
	 */
	public String getPayerPersonalID();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCardType
	 */
	public String getCardType();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCardNumber
	 */
	public String getCardNumber();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCardValidMonth
	 */
	public int getCardValidMonth();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getCardValidYear
	 */
	public int getCardValidYear();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getFClass
	 */
	public boolean getFClass();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#getWantsRefreshments
	 */
	public boolean getWantsRefreshments();

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setSchoolSeasonId
	 */
	public void setSchoolSeasonId(int schoolSeasonID);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setPayerName
	 */
	public void setPayerName(String name);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setPayerPersonalID
	 */
	public void setPayerPersonalID(String personalID);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setCardType
	 */
	public void setCardType(String type);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setCardNumber
	 */
	public void setCardNumber(String number);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setCardValidMonth
	 */
	public void setCardValidMonth(int month);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setCardValidYear
	 */
	public void setCardValidYear(int year);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setFClass
	 */
	public void setFClass(boolean fClass);

	/**
	 * @see se.idega.idegaweb.commune.care.data.AfterSchoolChoiceBMPBean#setWantsRefreshments
	 */
	public void setWantsRefreshments(boolean wantsRefreshments);
}
