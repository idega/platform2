/**
 * 
 */
package se.idega.idegaweb.commune.accounting.regulations.data;



import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface CareTime extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.CareTimeBMPBean#getCareTimeFrom
	 */
	public int getCareTimeFrom();

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.CareTimeBMPBean#getCareTimeTo
	 */
	public int getCareTimeTo();

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.CareTimeBMPBean#getDisplayString
	 */
	public String getDisplayString();

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.CareTimeBMPBean#setCareTimeFrom
	 */
	public void setCareTimeFrom(int careTimeFrom);

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.CareTimeBMPBean#setCareTimeTo
	 */
	public void setCareTimeTo(int careTimeTo);

	/**
	 * @see se.idega.idegaweb.commune.accounting.regulations.data.CareTimeBMPBean#setDisplayString
	 */
	public void setDisplayString(String display);

}
