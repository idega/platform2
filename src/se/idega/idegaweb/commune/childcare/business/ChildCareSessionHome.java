/*
 * Created on 2005-maj-17
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package se.idega.idegaweb.commune.childcare.business;




import com.idega.business.IBOHome;

/**
 * @author Malin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface ChildCareSessionHome extends IBOHome {
	public ChildCareSession create() throws javax.ejb.CreateException,
			java.rmi.RemoteException;

}
