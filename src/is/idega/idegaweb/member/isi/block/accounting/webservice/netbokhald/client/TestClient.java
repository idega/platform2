package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.client;

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
		NetbokhaldServiceServiceLocator service = new NetbokhaldServiceServiceLocator();
		// Now use the service to get a stub which implements the SDI.
		// CaseService port = service.getCaseService(new URL(endpoint)); //
		try {
			NetbokhaldService port = service.getNetbokhaldService(new URL(
					"http://felixtest.sidan.is/services/NetbokhaldService"));

			NetbokhaldEntry entries[] = port.getEntries("0000000010", "14400");
			if (entries == null) {
				System.out.println("No entries returned");
			} else {
				System.out.println("size = " + entries.length);

				for (int i = 0; i < entries.length; i++) {
					System.out.print(entries[i].getSerialNumber());
					System.out.print(", key = ");
					System.out.println(entries[i].getAccountingKey());
				}
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
	// + "'and id=" + ret.getId());

}
