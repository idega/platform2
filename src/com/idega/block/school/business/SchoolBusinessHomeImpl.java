/**
 * 
 */
package com.idega.block.school.business;



import com.idega.business.IBOHomeImpl;

/**
 * @author bluebottle
 *
 */
public class SchoolBusinessHomeImpl extends IBOHomeImpl implements
		SchoolBusinessHome {
	protected Class getBeanInterfaceClass() {
		return SchoolBusiness.class;
	}

	public SchoolBusiness create() throws javax.ejb.CreateException {
		return (SchoolBusiness) super.createIBO();
	}

}
