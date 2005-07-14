/*
 * $Id: KSIWSImpl.java,v 1.1 2005/07/14 01:00:43 eiki Exp $
 * Created on Jul 7, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.leagues.webservice;

import is.idega.idegaweb.member.isi.block.leagues.business.KSIUserGroupPluginBusiness;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWMainApplication;


public class KSIWSImpl implements KSIWS {

    public static void main(String[] args) {
 
        try {
        		FelagsmadurLocator locator = new FelagsmadurLocator();
        		FelagsmadurSoap_PortType wservice = locator.getFelagsmadurSoap();
        		
        		//heidar helguson
        		System.out.println("felagsmadur (EKKI) til : "+wservice.felagsmadur_til("2208774779"));
        		
        		System.out.println("felagsmadur til : "+wservice.felagsmadur_til("2801683279"));
        	
        	
			String endpoint = "http://felixdemo.sidan.is/KSIWS.jws";
			String method = "doClubMemberExchange";

//  Make the call
			String pin = "2502785279";
			String clubNumb = "101";
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
