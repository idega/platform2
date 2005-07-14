/**
 * FelagsmadurSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package is.idega.idegaweb.member.isi.block.leagues.webservice;

public interface FelagsmadurSoap_PortType extends java.rmi.Remote {
    public int felagsmadur_til(java.lang.String pKennitala) throws java.rmi.RemoteException;
    public is.idega.idegaweb.member.isi.block.leagues.webservice.TVilla felagsmadur_Skra(java.lang.String pKennitala, int pFelag_id, java.lang.String pFelagNafn) throws java.rmi.RemoteException;
}
