package is.idega.idegaweb.campus.block.application.data;


import com.idega.block.building.data.Apartment;
import com.idega.block.application.data.Application;
import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface RejectionHistory extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.application.data.RejectionHistoryBMPBean#getApplication
	 */
	public Application getApplication();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.RejectionHistoryBMPBean#getRejectionDate
	 */
	public Timestamp getRejectionDate();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.RejectionHistoryBMPBean#getApartment
	 */
	public Apartment getApartment();

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.RejectionHistoryBMPBean#setApplication
	 */
	public void setApplication(Application application);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.RejectionHistoryBMPBean#setRejectionDate
	 */
	public void setRejectionDate(Timestamp date);

	/**
	 * @see is.idega.idegaweb.campus.block.application.data.RejectionHistoryBMPBean#setApartment
	 */
	public void setApartment(Apartment apartment);
}