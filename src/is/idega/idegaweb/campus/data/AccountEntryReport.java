package is.idega.idegaweb.campus.data;

import java.sql.Date;

public interface AccountEntryReport {
	public void setEntityContext(javax.ejb.EntityContext p0)
			throws java.rmi.RemoteException;

	public void unsetEntityContext() throws java.rmi.RemoteException;

	public java.lang.Class getPrimaryKeyClass();

	public void setEJBHome(javax.ejb.EJBHome p0);

	public String getName();

	public Integer getAccountID();

	/**
	 * @param accountID
	 *            The accountID to set.
	 */
	public void setAccountID(Integer accountID);

	/**
	 * @return Returns the building.
	 */
	public String getBuilding();

	/**
	 * @param building
	 *            The building to set.
	 */
	public void setBuilding(String building);

	/**
	 * @return Returns the buildingId.
	 */
	public Integer getBuildingId();

	/**
	 * @param buildingId
	 *            The buildingId to set.
	 */
	public void setBuildingId(Integer buildingId);

	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName();

	/**
	 * @param firstName
	 *            The firstName to set.
	 */
	public void setFirstName(String firstName);

	/**
	 * @return Returns the keyCode.
	 */
	public String getKeyCode();

	/**
	 * @param keyCode
	 *            The keyCode to set.
	 */
	public void setKeyCode(String keyCode);

	/**
	 * @return Returns the keyInfo.
	 */
	public String getKeyInfo();

	/**
	 * @param keyInfo
	 *            The keyInfo to set.
	 */
	public void setKeyInfo(String keyInfo);

	/**
	 * @return Returns the lastName.
	 */
	public String getLastName();

	/**
	 * @param lastName
	 *            The lastName to set.
	 */
	public void setLastName(String lastName);

	/**
	 * @return Returns the middleName.
	 */
	public String getMiddleName();

	/**
	 * @param middleName
	 *            The middleName to set.
	 */
	public void setMiddleName(String middleName);

	/**
	 * @return Returns the personalID.
	 */
	public String getPersonalID();

	/**
	 * @param personalID
	 *            The personalID to set.
	 */
	public void setPersonalID(String personalID);

	/**
	 * @return Returns the total.
	 */
	public Float getTotal();

	/**
	 * @param total
	 *            The total to set.
	 */
	public void setTotal(Float total);

	/**
	 * @return Returns the keyID.
	 */
	public Integer getKeyID();

	/**
	 * @param keyID
	 *            The keyID to set.
	 */
	public void setKeyID(Integer keyID);

	public String getInfo();

	public void setInfo(String info);
	
	public Date getPaymentDate();
	
	public void setPaymentDate(Date timestamp);

}