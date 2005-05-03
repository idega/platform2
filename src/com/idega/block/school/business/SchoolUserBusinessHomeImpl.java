/*
 * Created on 2005-maj-03
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.idega.block.school.business;



import com.idega.business.IBOHomeImpl;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SchoolUserBusinessHomeImpl extends IBOHomeImpl implements
		SchoolUserBusinessHome {
	protected Class getBeanInterfaceClass() {
		return SchoolUserBusiness.class;
	}

	public SchoolUserBusiness create() throws javax.ejb.CreateException {
		return (SchoolUserBusiness) super.createIBO();
	}

}
