package is.idega.idegaweb.campus.block.allocation.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

public class ChargeForUnlimitedDownloadBMPBean extends GenericEntity implements
		ChargeForUnlimitedDownload {

	public static final String ENTITY_NAME = "cam_download_charge";

	private static final String COLUMN_USER = "user_id";
	private static final String COLUMN_CHARGE_FOR_DOWNLOAD = "charge_for_download";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addOneToOneRelationship(COLUMN_USER, User.class);
		addAttribute(COLUMN_CHARGE_FOR_DOWNLOAD, "Charge for download",
				Boolean.class);
	}

	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}

	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}

	public void setChargeForDownload(boolean chargeForDownload) {
		setColumn(COLUMN_CHARGE_FOR_DOWNLOAD, chargeForDownload);
	}

	public boolean getChargeForDownload() {
		return getBooleanColumnValue(COLUMN_CHARGE_FOR_DOWNLOAD, false);
	}

	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Collection ejbFindAllCharged() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_CHARGE_FOR_DOWNLOAD, true);
		
		return super.idoFindPKsByQuery(query);
	}

	public Object ejbFindByUser(User user) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_USER, user);
		
		return idoFindOnePKByQuery(query);
	}
}
