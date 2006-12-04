package is.idega.idegaweb.campus.webservice.nortek.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.GregorianCalendar;

import javax.xml.rpc.ServiceException;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// CaseServiceServiceLocator service = new CaseServiceServiceLocator();
		NortekServiceServiceLocator service = new NortekServiceServiceLocator();
		// Now use the service to get a stub which implements the SDI.
		// CaseService port = service.getCaseService(new URL(endpoint)); //
		try {
			NortekService port = service.getNortekService(new URL("http://campusnew.idega.is/services/NortekService"));

			System.out.println("port.banCard(1234) = " + port.banCard("1234"));
			//System.out.println("port.isCardValid(1234) = " + port.isCardValid("1234"));
			//System.out.println("port.addAmount(1234) = " + port.addAmountToCard("1234", new GregorianCalendar(), 100.0d, "1"));			
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

