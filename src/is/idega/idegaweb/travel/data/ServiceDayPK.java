package is.idega.idegaweb.travel.data;

import com.idega.data.PrimaryKey;

/**
 * @author gimmi
 */
public class ServiceDayPK extends PrimaryKey {

	private String COLUMN_SERVICE_ID = ServiceDayBMPBean.getColumnNameServiceId();
	private String COLUMN_DAY_OF_WEEK = ServiceDayBMPBean.getColumnNameDayOfWeek();
	
	/**
	 * @param scorecardID
	 * @param holeID
	 */
	public ServiceDayPK(Object serviceID, Object dayOfWeek) {
		this();
		setServiceID(serviceID);
		setDayOfWeek(dayOfWeek);
	}
	
	public ServiceDayPK() {
		super();
	}
	
	public void setServiceID(Object serviceID) {
		setPrimaryKeyValue(COLUMN_SERVICE_ID, serviceID);
	}

	public Object getServiceID() {
		return getPrimaryKeyValue(COLUMN_SERVICE_ID);
	}

	public void setDayOfWeek(Object dayOfWeek) {
		setPrimaryKeyValue(COLUMN_DAY_OF_WEEK, dayOfWeek);
	}

	public Object getDayOfWeek() {
		return getPrimaryKeyValue(COLUMN_DAY_OF_WEEK);
	}
}
