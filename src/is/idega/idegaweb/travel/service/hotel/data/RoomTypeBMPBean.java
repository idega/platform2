package is.idega.idegaweb.travel.service.hotel.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;

/**
 * @author gimmi
 */
public class RoomTypeBMPBean extends GenericEntity implements RoomType {

	public static String COLUMN_NAME = "TYPE_NAME";
	private static String COLUMN_VALID = "IS_VALID";

	public RoomTypeBMPBean() {
	  super();
	}

	public String getEntityName() {
		return "TB_ACC_ROOM_TYPE";
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "name", true, true, String.class);
		addAttribute(COLUMN_VALID, "valid", true, true, Boolean.class);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);	
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);	
	}
	
	public boolean getIsValid() {
		return getBooleanColumnValue(COLUMN_VALID);	
	}
	
	public void setIsValid(boolean isValid) {
		setColumn(COLUMN_VALID, isValid);	
	}
	
	public Collection ejbFindAll() throws FinderException {
		return this.idoFindAllIDsByColumnBySQL(COLUMN_VALID, "Y");	
	}
	

}
