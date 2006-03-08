/**
 * 
 */
package se.idega.idegaweb.commune.accounting.school.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO Dainis Describe Type StudyPathBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2006/03/08 11:05:20 $ by $Author: dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.4.2.1 $
 */
public class StudyPathBusinessHomeImpl extends IBOHomeImpl implements StudyPathBusinessHome {

	protected Class getBeanInterfaceClass() {
		return StudyPathBusiness.class;
	}

	public StudyPathBusiness create() throws javax.ejb.CreateException {
		return (StudyPathBusiness) super.createIBO();
	}
}
