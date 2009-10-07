package is.idega.idegaweb.campus.block.phone.data;


import java.sql.Date;
import com.idega.data.IDOEntity;
import com.idega.block.building.data.Apartment;

public interface CampusPhone extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneBMPBean#setPhoneNumber
	 */
	public void setPhoneNumber(String number);

	/**
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneBMPBean#getPhoneNumber
	 */
	public String getPhoneNumber();

	/**
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneBMPBean#getApartmentId
	 */
	public int getApartmentId();

	/**
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneBMPBean#getApartment
	 */
	public Apartment getApartment();

	/**
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneBMPBean#setApartmentId
	 */
	public void setApartmentId(int id);

	/**
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneBMPBean#setApartment
	 */
	public void setApartment(Apartment apartment);

	/**
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneBMPBean#setDateInstalled
	 */
	public void setDateInstalled(Date date);

	/**
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneBMPBean#getDateInstalled
	 */
	public Date getDateInstalled();

	/**
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneBMPBean#setDateResigned
	 */
	public void setDateResigned(Date date);

	/**
	 * @see is.idega.idegaweb.campus.block.phone.data.CampusPhoneBMPBean#getDateResigned
	 */
	public Date getDateResigned();
}