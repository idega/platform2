/**
 * FelagsmadurSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur;

public interface FelagsmadurSoap extends java.rmi.Remote {

    /**
     * Skilar hvort félagsma?ur sé til
     */
    public int felagsmadur_til(java.lang.String pKennitala) throws java.rmi.RemoteException;

    /**
     * Skráning á félagsmanni
     */
    public is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.TVilla felagsmadur_Skra(java.lang.String pKennitala, int pFelag_id, java.lang.String pFelagNafn) throws java.rmi.RemoteException;
}
