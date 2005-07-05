/*
 * Created on Jul 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package se.idega.idegaweb.commune.adulteducation.business;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.adulteducation.AdultEducationConstants;
import se.idega.idegaweb.commune.adulteducation.data.AdultEducationChoice;
import se.idega.idegaweb.commune.school.business.SchoolCaseBusiness;
import com.idega.block.process.data.Case;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;


/**
 * <p>
 * TODO thomas Describe Type AdultEducationCaseBusiness
 * </p>
 *  Last modified: $Date: 2005/07/05 16:46:39 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public class AdultEducationCaseBusiness implements SchoolCaseBusiness {

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.business.SchoolCaseBusiness#hasCaseCode(java.lang.String)
	 */
	public boolean isCase(Case useCase) {
		return AdultEducationConstants.ADULT_EDUCATION_CASE_CODE.equals(useCase.getCaseCode());
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.school.business.SchoolCaseBusiness#caseIsOpen(com.idega.block.process.data.Case, com.idega.presentation.IWContext)
	 */
	public boolean caseIsOpen(Case useCase, IWContext iwc) throws RemoteException, IBOLookupException, FinderException {
		AdultEducationBusiness adultSchBuiz;
		adultSchBuiz = (AdultEducationBusiness) IBOLookup.getServiceInstance(iwc, AdultEducationBusiness.class);
		AdultEducationChoice adultChoice = adultSchBuiz.getChoice(useCase.getPrimaryKey());
		return (adultChoice != null && !adultChoice.isPlacementMessageSent());
	}
}
