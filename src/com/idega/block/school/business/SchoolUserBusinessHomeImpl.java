/**
 * 
 */
package com.idega.block.school.business;

import com.idega.business.IBOHomeImpl;


/**
 * <p>
 * TODO Dainis Describe Type SchoolUserBusinessHomeImpl
 * </p>
 *  Last modified: $Date: 2004/06/28 09:09:50 $ by $Author: Dainis $
 * 
 * @author <a href="mailto:Dainis@idega.com">Dainis</a>
 * @version $Revision: 1.1 $
 */
public class SchoolUserBusinessHomeImpl extends IBOHomeImpl implements SchoolUserBusinessHome {

	protected Class getBeanInterfaceClass() {
		return SchoolUserBusiness.class;
	}

	public SchoolUserBusiness create() throws javax.ejb.CreateException {
		return (SchoolUserBusiness) super.createIBO();
	}
}
