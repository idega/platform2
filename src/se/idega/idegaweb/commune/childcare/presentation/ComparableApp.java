package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import se.idega.idegaweb.commune.childcare.data.ChildCareApplication;

/**
 * This class is used to sort ChildCareApplication object according to their
 * choice number and granted status.
 * @author <a href="mailto:roar@idega.is">roar</a>
 * @version $Id: ComparableApp.java,v 1.4 2003/04/23 13:28:10 roar Exp $
 * @since 12.2.2003 
 */
class ComparableApp implements Comparable {
	private ChildCareApplication _app;
	private boolean _grantedFirst;
				
	ComparableApp(Object app, boolean grantedFirst){
		_app = (ChildCareApplication) app;
		_grantedFirst = grantedFirst;
	}
		
	ChildCareApplication getApplication(){
		return _app;
	}

	/**
	 * Compareing two granted applications will give different result
	 * depending on which application is used as 'comparator' and parameter.
	 * This situation should never happen though, and the order of granted
	 * applications is not important. Two granted application will never be
	 * equal.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object application){
		ChildCareApplication app = ((ComparableApp) application).getApplication();
		try {
			int diff = _app.getChoiceNumber() - app.getChoiceNumber();
			
			if (_grantedFirst){
				if (_app.getStatus().equals(ChildCareCustomerApplicationTable.STATUS_PREL)){ 
					return  -1;
				} else if (app.getStatus().equals(ChildCareCustomerApplicationTable.STATUS_PREL)){
					return  1;
				} else {
					return diff;
				}
				
			} else {
				if (diff == 0 && _app.getStatus().equals(ChildCareCustomerApplicationTable.STATUS_PREL)){ 
					return  -1;
				} else if (diff == 0 && app.getStatus().equals(ChildCareCustomerApplicationTable.STATUS_PREL)){
					return  1;
				} else {
					return diff;
				}
								
			}
			
//			if (_grantedFirst && _app.getStatus().equals(ChildCareOfferTable.STATUS_UBEH)){ 
//				return  -1;
//			} else if (_grantedFirst && app.getStatus().equals(ChildCareOfferTable.STATUS_UBEH)){
//				return  1;
//			} else {
//				return _app.getChoiceNumber() - app.getChoiceNumber();
//			}
					
		} catch(RemoteException ex){
			return -1;
		} 
	}
}