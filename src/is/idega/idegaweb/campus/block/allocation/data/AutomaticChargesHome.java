package is.idega.idegaweb.campus.block.allocation.data;


import com.idega.block.finance.data.AssessmentRound;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import com.idega.user.data.User;

public interface AutomaticChargesHome extends IDOHome {
	public AutomaticCharges create() throws CreateException;

	public AutomaticCharges findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAll() throws FinderException;

	public AutomaticCharges findByUser(User user) throws FinderException;

	public Collection findTransferByAssessmentRound(AssessmentRound round)
			throws FinderException;

	public Collection findHandlingByAssessmentRound(AssessmentRound round)
			throws FinderException;

	public Collection findTransferByAssessmentRound(Integer roundID)
			throws FinderException;

	public Collection findHandlingByAssessmentRound(Integer roundID)
			throws FinderException;
}