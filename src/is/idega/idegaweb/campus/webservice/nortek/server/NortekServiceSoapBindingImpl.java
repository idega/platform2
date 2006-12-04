/**
 * NortekServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.nortek.server;

import is.idega.idegaweb.campus.nortek.business.NortekBusiness;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWMainApplication;

public class NortekServiceSoapBindingImpl implements is.idega.idegaweb.campus.webservice.nortek.server.NortekService{
    public boolean isCardValid(java.lang.String cardSerialNumber) throws java.rmi.RemoteException {
        return getBusiness().isCardValid(cardSerialNumber);
    }

    public boolean banCard(java.lang.String cardSerialNumber) throws java.rmi.RemoteException {
        return getBusiness().banCard(cardSerialNumber, true);
    }

    public boolean addAmountToCard(java.lang.String cardSerialNumber, java.util.Calendar timestamp, double amount, java.lang.String terminal) throws java.rmi.RemoteException {
        return getBusiness().addAmountToCardUser(cardSerialNumber, timestamp.getTime(), amount, terminal);
    }
    
    private NortekBusiness getBusiness() throws IBOLookupException {
    	NortekBusiness bus1 = (NortekBusiness) IBOLookup
		.getServiceInstance(IWMainApplication
				.getDefaultIWApplicationContext(),
				NortekBusiness.class);
    	
    	return bus1;
    }
}