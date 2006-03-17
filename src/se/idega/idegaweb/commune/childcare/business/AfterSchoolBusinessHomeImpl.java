/**
 * 
 */
package se.idega.idegaweb.commune.childcare.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO is Describe Type AfterSchoolBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2006/03/17 16:43:03 $ by $Author: igors $
 * 
 * @author <a href="mailto:is@idega.com">is</a>
 * @version $Revision: 1.3.2.5 $
 */
public class AfterSchoolBusinessHomeImpl extends IBOHomeImpl implements AfterSchoolBusinessHome {

	protected Class getBeanInterfaceClass() {
		return AfterSchoolBusiness.class;
	}

	public AfterSchoolBusiness create() throws javax.ejb.CreateException {
		return (AfterSchoolBusiness) super.createIBO();
	}
}
