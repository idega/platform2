/*
 * Created on 2005-jun-01
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.accounting.school.business;



import com.idega.business.IBOHomeImpl;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class StudyPathBusinessHomeImpl extends IBOHomeImpl implements
		StudyPathBusinessHome {
	protected Class getBeanInterfaceClass() {
		return StudyPathBusiness.class;
	}

	public StudyPathBusiness create() throws javax.ejb.CreateException {
		return (StudyPathBusiness) super.createIBO();
	}

}
