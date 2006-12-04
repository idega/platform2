package is.idega.idegaweb.campus.block.finance.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class CampusAssessmentBusinessHomeImpl extends IBOHomeImpl implements CampusAssessmentBusinessHome {
	public Class getBeanInterfaceClass() {
		return CampusAssessmentBusiness.class;
	}

	public CampusAssessmentBusiness create() throws CreateException {
		return (CampusAssessmentBusiness) super.createIBO();
	}
}