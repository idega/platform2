/*
 * Created on 2005-maj-17
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.childcare.business;




import com.idega.business.IBOHomeImpl;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ChildCareSessionHomeImpl extends IBOHomeImpl implements
		ChildCareSessionHome {
	protected Class getBeanInterfaceClass() {
		return ChildCareSession.class;
	}

	public ChildCareSession create() throws javax.ejb.CreateException {
		return (ChildCareSession) super.createIBO();
	}

}
