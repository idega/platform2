package is.idega.idegaweb.campus.block.application.data;


import com.idega.block.building.data.ApartmentCategory;
import com.idega.data.IDOEntity;

public interface ApartmentCategoryCombination extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.application.data.ApartmentCategoryCombinationBMPBean#getCategory1
	 */
	public ApartmentCategory getCategory1();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.ApartmentCategoryCombinationBMPBean#getCategory2
	 */
	public ApartmentCategory getCategory2();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.ApartmentCategoryCombinationBMPBean#setCategory1
	 */
	public void setCategory1(ApartmentCategory category);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.ApartmentCategoryCombinationBMPBean#setCategory2
	 */
	public void setCategory2(ApartmentCategory category);
}