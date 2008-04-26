package is.idega.idegaweb.campus.block.building.data;


import java.sql.Date;
import com.idega.data.IDOEntity;

public interface ApartmentTypeRent extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentBMPBean#getApartmentTypeId
	 */
	public int getApartmentTypeId();

	/**
	 * @see is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentBMPBean#setApartmentTypeId
	 */
	public void setApartmentTypeId(int id);

	/**
	 * @see is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentBMPBean#setApartmentTypeId
	 */
	public void setApartmentTypeId(Integer id);

	/**
	 * @see is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentBMPBean#getRent
	 */
	public float getRent();

	/**
	 * @see is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentBMPBean#setRent
	 */
	public void setRent(Float name);

	/**
	 * @see is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentBMPBean#getOtherExpeneses
	 */
	public double getOtherExpeneses();

	/**
	 * @see is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentBMPBean#setOtherExpenses
	 */
	public void setOtherExpenses(double otherExpenses);

	/**
	 * @see is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentBMPBean#getValidFrom
	 */
	public Date getValidFrom();

	/**
	 * @see is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentBMPBean#setValidFrom
	 */
	public void setValidFrom(Date use_date);

	/**
	 * @see is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentBMPBean#getValidTo
	 */
	public Date getValidTo();

	/**
	 * @see is.idega.idegaweb.campus.block.building.data.ApartmentTypeRentBMPBean#setValidTo
	 */
	public void setValidTo(Date use_date);
}