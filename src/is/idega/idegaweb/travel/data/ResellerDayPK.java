package is.idega.idegaweb.travel.data;

import com.idega.data.PrimaryKey;

/**
 * @author gimmi
 */
public class ResellerDayPK extends PrimaryKey {

	private String COLUMN_RESELLER_ID = ResellerDayBMPBean.getColumnNameResellerId();
	private String COLUMN_SERVICE_ID = ResellerDayBMPBean.getColumnNameServiceId();
	private String COLUMN_DAY_OF_WEEK = ResellerDayBMPBean.getColumnNameDayOfWeek();
	
	/**
	 * @param scorecardID
	 * @param holeID
	 */
	public ResellerDayPK(Object resellerID, Object serviceID, Object dayOfWeek) {
		this();
		setServiceID(serviceID);
		setResellerID(resellerID);
		setDayOfWeek(dayOfWeek);
	}
	
	public ResellerDayPK() {
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
	
	public void setResellerID(Object resellerID) {
		setPrimaryKeyValue(COLUMN_RESELLER_ID, resellerID);
	}
	
	public Object getResellerID() {
		return getPrimaryKeyValue(COLUMN_RESELLER_ID);
	}
}
