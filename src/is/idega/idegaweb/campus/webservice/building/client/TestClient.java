package is.idega.idegaweb.campus.webservice.building.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BuildingWSServiceServiceLocator service = new BuildingWSServiceServiceLocator();
		try {
			BuildingWSService port = service.getBuildingService(new URL(
					"http://www.studentagardar.is/services/BuildingService"));
			/*ComplexInfo info[] = port.getComplexInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					ComplexInfo complex = info[i];
					System.out.println("id = " + complex.getId());
					System.out.println("name = " + complex.getName());
					System.out.println("info = " + complex.getInfo());
					System.out.println("enInfo = " + complex.getEnglishInfo());
					System.out.println("");
				}
			}*/
			/*ComplexInfo complex = new ComplexInfo();
			complex.setId(30);
			BuildingInfo info[] = port.getBuildingInfo(complex);
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					BuildingInfo building = info[i];
					System.out.println("id = " + building.getId());
					System.out.println("name = " + building.getName());
					System.out.println("info = " + building.getInfo());
					System.out.println("enInfo = " + building.getEnglishInfo());
					System.out.println("address = " + building.getAddress());
					System.out.println("");
				}
			}*/
			BuildingInfo building = new BuildingInfo();
			building.setId(45);
			//building.setId(2);
			ApartmentInfo info[] = port.getApartmentInfo(building);
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					ApartmentInfo apartment = info[i];
					System.out.println("id = " + apartment.getId());
					System.out.println("name = " + apartment.getName());
					System.out.println("serialNumber = " + apartment.getSerialNumber());
					System.out.println("floor = " + apartment.getFloor());
					System.out.println("type = " + apartment.getType());
					System.out.println("typeShortName = " + apartment.getTypeShortName());
					System.out.println("info = " + apartment.getTypeInfo());
					System.out.println("enInfo = " + apartment.getTypeEnglishInfo());
					System.out.println("subcategory = " + apartment.getSubcategory());
					System.out.println("category = " + apartment.getCategory());
					System.out.println("furnished = " + apartment.isFurnished());
					System.out.println("hasAttic = " + apartment.isHasAttic());
					System.out.println("hasBathroom = " + apartment.isHasBathroom());
					System.out.println("hasKitchen = " + apartment.isHasKitchen());
					System.out.println("hasStorageroom = " + apartment.isHasStorageroom());
					System.out.println("hasStudyroom = " + apartment.isHasStudyroom());
					System.out.println("numberOfRooms = " + apartment.getNumberOfRooms());
					System.out.println("size = " + apartment.getSize());
					System.out.println("rent = " + apartment.getRent());
					System.out.println("heat = " + apartment.getHeat());
					System.out.println("electricity = " + apartment.getElectricity());
					System.out.println("collective fee = " + apartment.getCollectiveFee());
					System.out.println("");
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
