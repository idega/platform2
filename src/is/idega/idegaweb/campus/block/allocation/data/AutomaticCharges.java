package is.idega.idegaweb.campus.block.allocation.data;


import com.idega.block.finance.data.AssessmentRound;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;

public interface AutomaticCharges extends IDOEntity {
	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#setChargeForDownload
	 */
	public void setChargeForDownload(boolean chargeForDownload);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#setChargeForHandling
	 */
	public void setChargeForHandling(boolean chargeForHandling);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#setChargeForTransfer
	 */
	public void setChargeForTransfer(boolean chargeForTransfer);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#setHandlingChargeAssessment
	 */
	public void setHandlingChargeAssessment(AssessmentRound round);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#setTransferChargeAssessment
	 */
	public void setTransferChargeAssessment(AssessmentRound round);

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#getUser
	 */
	public User getUser();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#getChargeForDownload
	 */
	public boolean getChargeForDownload();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#getChargeForHandling
	 */
	public boolean getChargeForHandling();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#getChargeForTransfer
	 */
	public boolean getChargeForTransfer();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#getHandlingChargeAssessment
	 */
	public AssessmentRound getHandlingChargeAssessment();

	/**
	 * @see is.idega.idegaweb.campus.block.allocation.data.AutomaticChargesBMPBean#getTransferChargeAssessment
	 */
	public AssessmentRound getTransferChargeAssessment();
}