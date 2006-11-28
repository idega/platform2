package is.idega.idegaweb.campus.webservice.general.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.idega.util.IWTimestamp;

public class TestClient {
	public static void main(String[] args) {
		// CaseServiceServiceLocator service = new CaseServiceServiceLocator();
		CampusServiceServiceLocator service = new CampusServiceServiceLocator();
		// Now use the service to get a stub which implements the SDI.
		// CaseService port = service.getCaseService(new URL(endpoint)); //
		try {
			CampusService port = service.getCampusService(new URL("http://campusdemo.idega.is/services/CampusService"));
			TenantInfo info[] = port.getTenantInfo(6);
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					TenantInfo tenant = info[i];
					System.out.print("ssn = " + tenant.getPersonalID());
					System.out.print(", building = " + tenant.getBuildingName());
					System.out.print(", apartment = " + tenant.getApartmentNumber());
					System.out.println(", date = " + new IWTimestamp(tenant.getMovedInDate().getTime()).getDateString("dd-MM-yyyy hh:mm"));
				}
			}
			
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}
}