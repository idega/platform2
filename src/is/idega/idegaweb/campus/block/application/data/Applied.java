package is.idega.idegaweb.campus.block.application.data;


import com.idega.block.building.data.ApartmentSubcategory;
import com.idega.block.building.data.Apartment;
import com.idega.data.IDOEntity;

public interface Applied extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#getComplexIdColumnName
	 */
	public String getComplexIdColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#getSubcategoryColumnName
	 */
	public String getSubcategoryColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#getApplicationIdColumnName
	 */
	public String getApplicationIdColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#getOrderColumnName
	 */
	public String getOrderColumnName();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#setComplexId
	 */
	public void setComplexId(int id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#setComplexId
	 */
	public void setComplexId(Integer id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#getComplexId
	 */
	public Integer getComplexId();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#setSubcategoryID
	 */
	public void setSubcategoryID(int id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#setSubcategoryID
	 */
	public void setSubcategoryID(Integer id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#getSubcategoryID
	 */
	public int getSubcategoryID();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#getSubcategory
	 */
	public ApartmentSubcategory getSubcategory();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#setApplicationId
	 */
	public void setApplicationId(int id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#setApplicationId
	 */
	public void setApplicationId(Integer id);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#getApplicationId
	 */
	public Integer getApplicationId();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#setOrder
	 */
	public void setOrder(int order);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#setOrder
	 */
	public void setOrder(Integer order);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#getOrder
	 */
	public Integer getOrder();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#setApartment
	 */
	public void setApartment(Apartment apartment);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#setApartmentID
	 */
	public void setApartmentID(int apartmentID);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#getApartment
	 */
	public Apartment getApartment();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.AppliedBMPBean#getApartmentID
	 */
	public int getApartmentID();
}