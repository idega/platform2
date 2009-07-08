package is.idega.idegaweb.campus.block.allocation.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.finance.data.AssessmentRound;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

public class AutomaticChargesBMPBean extends GenericEntity implements
		AutomaticCharges {

	private static final long serialVersionUID = 3111384029740367003L;

	public static final String ENTITY_NAME = "cam_auto_charge";

	private static final String COLUMN_USER = "user_id";
	private static final String COLUMN_CHARGE_FOR_DOWNLOAD = "charge_for_download";
	private static final String COLUMN_CHARGE_FOR_HANDLING = "charge_for_handling";
	private static final String COLUMN_CHARGE_FOR_TRANSFER = "charge_for_transfer";
	private static final String COLUMN_CHARGE_FOR_HANDLING_ASSESSMENT = "handling_charge_assessment_id";
	private static final String COLUMN_CHARGE_FOR_TRANSFER_ASSESSMENT = "transfer_charge_assessment_id";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addOneToOneRelationship(COLUMN_USER, User.class);
		addAttribute(COLUMN_CHARGE_FOR_DOWNLOAD, "Charge for download",
				Boolean.class);
		addAttribute(COLUMN_CHARGE_FOR_HANDLING, "Charge for handling", Boolean.class);
		addAttribute(COLUMN_CHARGE_FOR_TRANSFER, "Charge for transfer", Boolean.class);
		addManyToOneRelationship(COLUMN_CHARGE_FOR_HANDLING_ASSESSMENT, AssessmentRound.class);
		addManyToOneRelationship(COLUMN_CHARGE_FOR_TRANSFER_ASSESSMENT, AssessmentRound.class);
	}

	//setters
	public void setUser(User user) {
		setColumn(COLUMN_USER, user);
	}

	public void setChargeForDownload(boolean chargeForDownload) {
		setColumn(COLUMN_CHARGE_FOR_DOWNLOAD, chargeForDownload);
	}

	public void setChargeForHandling(boolean chargeForHandling) {
		setColumn(COLUMN_CHARGE_FOR_HANDLING, chargeForHandling);
	}

	public void setChargeForTransfer(boolean chargeForTransfer) {
		setColumn(COLUMN_CHARGE_FOR_TRANSFER, chargeForTransfer);
	}
	
	public void setHandlingChargeAssessment(AssessmentRound round) {
		setColumn(COLUMN_CHARGE_FOR_HANDLING_ASSESSMENT, round);
	}

	public void setTransferChargeAssessment(AssessmentRound round) {
		setColumn(COLUMN_CHARGE_FOR_TRANSFER_ASSESSMENT, round);
	}
	
	//getters
	public User getUser() {
		return (User) getColumnValue(COLUMN_USER);
	}

	public boolean getChargeForDownload() {
		return getBooleanColumnValue(COLUMN_CHARGE_FOR_DOWNLOAD, false);
	}
	
	public boolean getChargeForHandling() {
		return getBooleanColumnValue(COLUMN_CHARGE_FOR_HANDLING, false);
	}
	
	public boolean getChargeForTransfer() {
		return getBooleanColumnValue(COLUMN_CHARGE_FOR_TRANSFER, false);
	}

	public AssessmentRound getHandlingChargeAssessment() {
		return (AssessmentRound) getColumnValue(COLUMN_CHARGE_FOR_HANDLING_ASSESSMENT);
	}

	public AssessmentRound getTransferChargeAssessment() {
		return (AssessmentRound) getColumnValue(COLUMN_CHARGE_FOR_TRANSFER_ASSESSMENT);
	}

	//ejb
	public Collection ejbFindAll() throws FinderException {
		return super.idoFindAllIDsBySQL();
	}

	public Object ejbFindByUser(User user) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_USER, user);
		
		return idoFindOnePKByQuery(query);
	}
	
	public Collection ejbFindTransferByAssessmentRound(AssessmentRound round) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_CHARGE_FOR_TRANSFER_ASSESSMENT, round);
		
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindHandlingByAssessmentRound(AssessmentRound round) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_CHARGE_FOR_HANDLING_ASSESSMENT, round);
		
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindTransferByAssessmentRound(Integer roundID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_CHARGE_FOR_TRANSFER_ASSESSMENT, roundID);
		
		return idoFindPKsByQuery(query);
	}

	public Collection ejbFindHandlingByAssessmentRound(Integer roundID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEquals(COLUMN_CHARGE_FOR_HANDLING_ASSESSMENT, roundID);
		
		return idoFindPKsByQuery(query);
	}
}