package is.idega.idegaweb.campus.block.allocation.data;


import com.idega.user.data.User;
import com.idega.data.IDOEntity;

public interface ChargeForUnlimitedDownload extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ChargeForUnlimitedDownloadBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ChargeForUnlimitedDownloadBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ChargeForUnlimitedDownloadBMPBean#setChargeForDownload
	 */
	public void setChargeForDownload(boolean chargeForDownload);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.ChargeForUnlimitedDownloadBMPBean#getChargeForDownload
	 */
	public boolean getChargeForDownload();
}