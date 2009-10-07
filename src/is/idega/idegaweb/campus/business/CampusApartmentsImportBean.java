package is.idega.idegaweb.campus.business;

import is.idega.idegaweb.campus.block.phone.data.CampusPhone;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.building.business.BuildingCacher;
import com.idega.block.building.business.BuildingService;
import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentCategory;
import com.idega.block.building.data.ApartmentSubcategory;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.Floor;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.Tariff;
import com.idega.block.importer.business.ImportFileHandler;
import com.idega.block.importer.data.ImportFile;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.user.data.Group;
import com.idega.util.IWTimestamp;

public class CampusApartmentsImportBean extends IBOServiceBean implements
		ImportFileHandler, CampusApartmentsImport {

	private CampusService service = null;
	private BuildingService buildingService = null;
	private ImportFile file = null;
	private List failedRecords = null;

	public List getFailedRecords() throws RemoteException {
		return failedRecords;
	}

	public boolean handleRecords() throws RemoteException {
		this.service = getCampusService(getIWApplicationContext());
		this.buildingService = this.service.getBuildingService();
		this.failedRecords = new ArrayList();

		if (this.file != null) {
			String line = (String) this.file.getNextRecord();
			while (line != null && !"".equals(line)) {
				if (!handleLine(line)) {
					failedRecords.add(line);
				}
				line = (String) file.getNextRecord();
			}
		}

		if (!failedRecords.isEmpty()) {
			System.out.println("Errors in the following lines :");
			Iterator iter = failedRecords.iterator();
			while (iter.hasNext()) {
				System.out.println((String) iter.next());
			}
		}

		return true;
	}

	private boolean handleLine(String line) throws RemoteException {
		ArrayList values = this.file.getValuesFromRecordString(line);
		int size = values.size();

		if (size < 22) {
			return false;
		}
		String buildingName = ((String) values.get(0)).trim();
		String apartmentNumber = ((String) values.get(1)).trim();
		String rent = ((String) values.get(4)).trim();
		String typeSize = ((String) values.get(5)).trim();
		String electricity = ((String) values.get(6)).trim();
		String heat = ((String) values.get(7)).trim();
		String buildingFund = ((String) values.get(8)).trim();
		String typeAbbrevation = ((String) values.get(9)).trim();
		String complexName = ((String) values.get(10)).trim();
		String floorName = ((String) values.get(11)).trim();

		String kitchen = ((String) values.get(12)).trim();
		String storage = ((String) values.get(13)).trim();
		String attic = ((String) values.get(14)).trim();
		String balcony = ((String) values.get(15)).trim();
		String bathroom = ((String) values.get(16)).trim();
		String study = ((String) values.get(17)).trim();
		String furniture = ((String) values.get(18)).trim();
		String info = ((String) values.get(19)).trim();
		String categoryName = ((String) values.get(20)).trim();
		String subCategoryName = ((String) values.get(21)).trim();

		String numberOfRooms = "";
		if (size > 22) {
			numberOfRooms = ((String) values.get(22)).trim();
		}
		String rentIndexType = "";
		if (size > 23) {
			rentIndexType = ((String) values.get(23)).trim();
		}
		String heatIndexType = "";
		if (size > 24) {
			heatIndexType = ((String) values.get(24)).trim();
		}
		String electricityIndexType = "";
		if (size > 25) {
			electricityIndexType = ((String) values.get(25)).trim();
		}

		String rentAccKey = "";
		if (size > 26) {
			rentAccKey = ((String) values.get(26)).trim();
		}
		String heatAccKey = "";
		if (size > 27) {
			heatAccKey = ((String) values.get(27)).trim();
		}
		String electricityAccKey = "";
		if (size > 28) {
			electricityAccKey = ((String) values.get(28)).trim();
		}

		if ("Building".equals(buildingName)) {
			return true;
		}

		// Get complex or create it if it doesn't exist
		Complex complex;
		try {
			complex = this.buildingService.getComplexHome().findComplexByName(
					complexName);
		} catch (FinderException e) {
			try {
				complex = this.buildingService.getComplexHome().create();
				complex.setName(complexName);
				complex.store();
			} catch (CreateException e1) {
				e1.printStackTrace();
				return false;
			}
		}

		// Get building or create it if it doesn't exist
		Building building;
		try {
			building = this.buildingService.getBuildingHome()
					.findByComplexAndName(buildingName, complex);
		} catch (FinderException e) {
			try {
				building = this.buildingService.getBuildingHome().create();
				building.setName(buildingName);
				building.setStreet(buildingName);
				building.setComplex(complex);
				building.store();
			} catch (CreateException e1) {
				e1.printStackTrace();
				return false;
			}
		}

		// Get floor or create it if it doesn't exist
		Floor floor;
		try {
			floor = this.buildingService.getFloorHome().findByBuildingAndName(
					floorName, building);
		} catch (FinderException e) {
			try {
				floor = this.buildingService.getFloorHome().create();
				floor.setName(floorName);
				floor.setBuilding(building);
				floor.store();
			} catch (CreateException e1) {
				e1.printStackTrace();
				return false;
			}
		}

		// Get apartment category or create it if it doesn't exist
		ApartmentCategory category;
		try {
			category = this.buildingService.getApartmentCategoryHome()
					.findByName(categoryName);
		} catch (FinderException e) {
			try {
				category = this.buildingService.getApartmentCategoryHome()
						.create();
				category.setName(categoryName);
				category.store();
			} catch (CreateException e1) {
				e1.printStackTrace();
				return false;
			}
		}

		// Get apartment sub-category or create it if it doesn't exist
		ApartmentSubcategory subCategory;
		try {
			subCategory = this.buildingService.getApartmentSubcategoryHome()
					.findByCategoryAndName(subCategoryName, category);
		} catch (FinderException e) {
			try {
				subCategory = this.buildingService
						.getApartmentSubcategoryHome().create();
				subCategory.setApartmentCategory(category);
				subCategory.setName(subCategoryName);
				subCategory.store();
			} catch (CreateException e1) {
				e1.printStackTrace();
				return false;
			}
		}

		// Get apartment type or create it if it doesn't exist
		ApartmentType type;
		try {
			type = this.buildingService.getApartmentTypeHome()
					.findBySubcategoryAndAbbrevation(typeAbbrevation,
							subCategory);
		} catch (FinderException e) {
			try {
				type = this.buildingService.getApartmentTypeHome().create();
				type.setAbbreviation(typeAbbrevation);
				type.setName(typeAbbrevation);
				type.setApartmentSubcategory(subCategory);
				if (numberOfRooms != null && !"".equals(numberOfRooms)) {
					type.setRoomCount(Integer.parseInt(numberOfRooms));
				}
				if (typeSize != null && !"".equals(typeSize)) {
					type.setArea(Double.parseDouble(typeSize));
				}
				if (kitchen != null && !"".equals(kitchen)) {
					type.setKitchen(true);
				}
				if (bathroom != null && !"".equals(bathroom)) {
					type.setBathRoom(true);
				}
				if (storage != null && !"".equals(storage)) {
					type.setStorage(true);
				}
				if (study != null && !"".equals(study)) {
					type.setStudy(true);
				}
				if (attic != null && !"".equals(attic)) {
					type.setLoft(true);
				}
				if (furniture != null && !"".equals(furniture)) {
					type.setFurniture(true);
				}
				if (balcony != null && !"".equals(balcony)) {
					type.setBalcony(true);
				}
				if (rent != null && !"".equals(rent)) {
					type.setRent(Integer.parseInt(rent));
				}
				if (info != null && !"".equals(info)) {
					type.setInfo(info);
				}
				type.store();
			} catch (CreateException e1) {
				e1.printStackTrace();
				return false;
			}
		}

		Apartment apartment;
		try {
			apartment = this.buildingService.getApartmentHome()
					.findByFloorAndTypeAndName(apartmentNumber, floor, type);
		} catch (FinderException e) {
			try {
				apartment = this.buildingService.getApartmentHome().create();
				apartment.setName(apartmentNumber);
				apartment.setFloor(floor);
				apartment.setApartmentType(type);
				apartment.setRentable(true);
				apartment.store();
				
				CampusPhone phone = this.buildingService.getCampusPhoneHome().create();
				phone.setApartment(apartment);
				phone.setPhoneNumber("");
				phone.store();
			} catch (CreateException e1) {
				e1.printStackTrace();
				return false;
			}
		}

		BuildingCacher.setToReloadNextTimeReferenced();

		try {
			AccountKey rentKey = null;
			AccountKey heatKey = null;
			AccountKey electricityKey = null;
			try {
				rentKey = this.service.getFinanceService()
						.getAccountKeyHome().findByName(rentAccKey);
				heatKey = this.service.getFinanceService()
						.getAccountKeyHome().findByName(heatAccKey);
				electricityKey = this.service.getFinanceService()
						.getAccountKeyHome().findByName(electricityAccKey);
			} catch (FinderException e) {
				return true;
			}

			IWTimestamp now = new IWTimestamp();

			Tariff tariff = this.service.getFinanceService().getTariffHome()
					.create();
			tariff.setAccountKey(rentKey);
			tariff.setName(rentKey.getInfo());
			tariff.setUseFromDate(now.getTimestamp());
			tariff.setUseToDate(now.getTimestamp());
			if (rentIndexType != null && !"".equals(rentIndexType)) {
				tariff.setUseIndex(true);
				tariff.setIndexType(rentIndexType);
				tariff.setIndexUpdated(now.getTimestamp());
			}
			tariff.setTariffAttribute("t_"
					+ ((Integer) type.getPrimaryKey()).intValue());
			tariff.setPrice(Float.parseFloat(rent));
			tariff.setTariffGroupId(1);
			tariff.store();

			tariff = this.service.getFinanceService().getTariffHome().create();
			tariff.setAccountKey(heatKey);
			tariff.setName(heatKey.getInfo());
			tariff.setUseFromDate(now.getTimestamp());
			tariff.setUseToDate(now.getTimestamp());
			if (heatIndexType != null && !"".equals(heatIndexType)) {
				tariff.setUseIndex(true);
				tariff.setIndexType(heatIndexType);
				tariff.setIndexUpdated(now.getTimestamp());
			}
			tariff.setTariffAttribute("t_"
					+ ((Integer) type.getPrimaryKey()).intValue());
			tariff.setPrice(Float.parseFloat(heat));
			tariff.setTariffGroupId(1);
			tariff.store();

			tariff = this.service.getFinanceService().getTariffHome().create();
			tariff.setAccountKey(electricityKey);
			tariff.setName(electricityKey.getInfo());
			tariff.setUseFromDate(now.getTimestamp());
			tariff.setUseToDate(now.getTimestamp());
			if (electricityIndexType != null
					&& !"".equals(electricityIndexType)) {
				tariff.setUseIndex(true);
				tariff.setIndexType(electricityIndexType);
				tariff.setIndexUpdated(now.getTimestamp());
			}
			tariff.setTariffAttribute("t_"
					+ ((Integer) type.getPrimaryKey()).intValue());
			tariff.setPrice(Float.parseFloat(electricity));
			tariff.setTariffGroupId(1);
			tariff.store();
		} catch (CreateException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void setImportFile(ImportFile file) throws RemoteException {
		this.file = file;
	}

	public void setRootGroup(Group rootGroup) throws RemoteException {
	}

	protected CampusService getCampusService(IWApplicationContext iwac) {
		try {
			return (CampusService) IBOLookup.getServiceInstance(iwac,
					CampusService.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}