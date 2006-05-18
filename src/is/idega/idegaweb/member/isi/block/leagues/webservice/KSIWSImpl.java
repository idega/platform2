/*
 * $Id: KSIWSImpl.java,v 1.2.4.1 2006/05/18 14:48:59 gimmi Exp $
 * Created on Jul 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.leagues.webservice;

import is.idega.idegaweb.member.isi.block.leagues.business.KSIUserGroupPluginBusiness;
import is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.FelagsmadurLocator;
import is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.FelagsmadurSoap;
import is.ksi.www2.ssl.vefthjon_Felix.Felagsmadur.TVilla;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import org.apache.axis.client.Stub;
import org.apache.axis.message.SOAPHeaderElement;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWMainApplication;



public class KSIWSImpl implements KSIWS {

    public static void main(String[] args) {
 
    	
    	
    	try {
    		
    		FelagsmadurLocator locator = new FelagsmadurLocator();
    		FelagsmadurSoap wservice = locator.getFelagsmadurSoap();
    	
		//	String message = registerPlayerToClubViaWebService("2502785279",907, "Sm‡stund");
			
    		System.out.println("felagsmadur til : "+wservice.felagsmadur_til("2502785279"));
    		
    		
			//System.out.println(message);
		}
		catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		catch (SOAPException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	/*
        try {
        		FelagsmadurLocator locator = new FelagsmadurLocator();
        		FelagsmadurSoap_PortType wservice = locator.getFelagsmadurSoap();
        		
        		//heidar helguson
        		System.out.println("felagsmadur (EKKI) til : "+wservice.felagsmadur_til("2208774779"));
        		
        		System.out.println("felagsmadur til : "+wservice.felagsmadur_til("2801683279"));
        	
        	
			String endpoint = "http://felixdemo.sidan.is/KSIWS.jws";
			String method = "doClubMemberExchange";

//  Make the call
			
			//wrong pin test
			//String pin = "250278527";

			//Ari thor hj‡ kr
			String pin = "2806723949";

			//wrong club test
//			String clubNumb = "999";
			
			String clubNumb = "240";
			String date = "25-02-2005";
      
			Service service = new Service();
			Call call = (Call) service.createCall();

			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			call.setOperationName( method );
			call.addParameter("op1", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("op2", XMLType.XSD_STRING, ParameterMode.IN);
			call.addParameter("op3", XMLType.XSD_STRING, ParameterMode.IN);
			call.setReturnType(XMLType.XSD_STRING);

			String ret = (String) call.invoke( new Object [] { pin,clubNumb,date });

			System.out.println("Got result : " + ret);
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
		catch (ServiceException e) {
			e.printStackTrace();
		}
   
    */
		/* catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		
    
//        try {
//            NewDocument service = new NewDocumentLocator();
//
//            
//            
//            NewDocPortType doc = service.getNewDocPort();
//            reply = doc.newDoc("MainTopic", "test12", "test23", "test34");
//
//        
//        
///*          IF THE URL CHANGES  NewDocBindingStub stub = new NewDocBindingStub(new URL("http://testdb/Spjall2.nsf/webservice?OpenAgent"), service);
//            DataHandler file = new DataHandler(new FileDataSource("/Users/bluebottle/line_bluebottle.jpg"));
//            stub.addAttachment(file);
//            reply = stub.newDoc("MainTopic", "test12", "test23", "test34");*/
//            
//        
//        
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }


    }
    
    
    
    public static String registerPlayerToClubViaWebService(String personalId, int clubNumber, String clubName) throws RemoteException, ServiceException, MalformedURLException, SOAPException{
		FelagsmadurLocator locator = new FelagsmadurLocator();
		QName serviceName = locator.getServiceName();
		//FelagsmadurSoap_PortType wservice = locator.getFelagsmadurSoap(new URL("http://127.0.0.1:8080/ssl/vefthjon_felix/felagsmadur.asmx?"));
		FelagsmadurSoap wservice = locator.getFelagsmadurSoap();
		
		SOAPHeaderElement authHeader = new SOAPHeaderElement(serviceName.getNamespaceURI(),"AuthHeader");
		//authHeader.setMustUnderstand(true);
		
		SOAPElement userName = authHeader.addChildElement("UserName");
		userName.addTextNode("felix7");
		SOAPElement password = authHeader.addChildElement("Password");
		password.addTextNode("r2bold5");
	
		Stub stub = (Stub) wservice;
		stub.setHeader(authHeader);
		
		
//SET PROPERTY ADDS TO THE HTTP HEADER NOT SOAP HEADER
//		stub._setProperty("Username","felix7");
//		stub._setProperty("Password","r2bold5");
//		stub.setUsername("felix7");
//		stub.setPassword("r2bold5");
//		stub._setProperty(Stub.USERNAME_PROPERTY,"felix7");
//		stub._setProperty(Stub.PASSWORD_PROPERTY,"r2bold5");
	
		TVilla msg =  wservice.felagsmadur_Skra(personalId,clubNumber,clubName);
		int error = msg.getIVilla();
	
		
		String text = msg.getSVilla_texti();
		
		return error+" "+text;
		
	}

	public String doClubMemberExchange(String personalIdOfPlayer, String clubNumberToRegisterTo, String dateOfActivation) {
		KSIUserGroupPluginBusiness biz;
		try {
			biz = (KSIUserGroupPluginBusiness) IBOLookup.getServiceInstance(IWMainApplication.getDefaultIWApplicationContext(),KSIUserGroupPluginBusiness.class);
			return biz.doClubMemberExchange(personalIdOfPlayer,clubNumberToRegisterTo,dateOfActivation);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "Service failed to complete club member exchange please contact ISI or eiki@idega.is. The error message was :"+e.getMessage();
		}	
	}
}
