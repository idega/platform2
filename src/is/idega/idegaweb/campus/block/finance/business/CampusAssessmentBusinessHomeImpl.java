/**
 * 
 */
package is.idega.idegaweb.campus.block.finance.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class CampusAssessmentBusinessHomeImpl extends IBOHomeImpl implements
		CampusAssessmentBusinessHome {
	protected Class getBeanInterfaceClass() {
		return CampusAssessmentBusiness.class;
	}

	public CampusAssessmentBusiness create() throws javax.ejb.CreateException {
		return (CampusAssessmentBusiness) super.createIBO();
	}

}
