/**
 * NortekService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.campus.webservice.nortek.server;

public interface NortekService extends java.rmi.Remote {
    public boolean isCardValid(java.lang.String cardSerialNumber) throws java.rmi.RemoteException;
    public boolean banCard(java.lang.String cardSerialNumber) throws java.rmi.RemoteException;
    public boolean addAmountToCard(java.lang.String cardSerialNumber, java.util.Calendar timestamp, double amount, java.lang.String terminal) throws java.rmi.RemoteException;
}
