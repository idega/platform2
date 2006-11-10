package is.idega.idegaweb.member.isi.block.accounting.webservice.general.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// CaseServiceServiceLocator service = new CaseServiceServiceLocator();
		AccountingServiceServiceLocator service = new AccountingServiceServiceLocator();
		// Now use the service to get a stub which implements the SDI.
		// CaseService port = service.getCaseService(new URL(endpoint)); //
		try {
			AccountingService port = service.getAccountingService(new URL("http://felixtest.sidan.is/services/AccountingService"));

			UserInfo user = port.getUser("2906852309");
			if (user.isValid()) {
				System.out.println("User.first_name = " + user.getFirstName());
				System.out.println("User.middle_name = " + user.getMiddleName());
				System.out.println("User.last_name = " + user.getLastName());
				System.out.println("User.ssn = " + user.getSocialsecurity());
				AddressInfo address = user.getAddress();
				System.out.println("address.street = " + address.getStreetName());
				System.out.println("address.number = " + address.getStreetNumber());
				System.out.println("address.po = " + address.getPostalcode());
				System.out.println("address.city = " + address.getCity());
				System.out.println("address.country = " + address.getCountry());
			} else {
				System.out.println("User invalid");
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	// Stub stub = (Stub) port2;
	// stub.setUsername("Thoma");
	// stub.setPassword("Weser");
	// NewCasePort port = service.getCreateNewCaseHttp();
	// boolean ret2 =
	// port2.validateTicket("10t1703532189A49F9278C58343B2D71B7E5871D2EA98");
	// CaseResult ret = port.createOrUpdateCase(wsCase);
	// System.out.print(ret2);
	// System.out.println("Sent 'Hello!', got operation='" + ret.getOperation()
	// + "'and id=" + ret.getId());	}

}
