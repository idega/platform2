package is.idega.idegaweb.campus.webservice.building.business;

import is.idega.idegaweb.campus.webservice.building.server.ApartmentInfo;
import is.idega.idegaweb.campus.webservice.building.server.BuildingInfo;
import is.idega.idegaweb.campus.webservice.building.server.ComplexInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.ejb.FinderException;

import com.idega.block.building.data.Apartment;
import com.idega.block.building.data.ApartmentHome;
import com.idega.block.building.data.ApartmentType;
import com.idega.block.building.data.Building;
import com.idega.block.building.data.BuildingHome;
import com.idega.block.building.data.Complex;
import com.idega.block.building.data.ComplexHome;
import com.idega.block.building.data.Floor;
import com.idega.block.building.data.FloorHome;
import com.idega.block.finance.data.Tariff;
import com.idega.block.finance.data.TariffHome;
import com.idega.block.text.business.ContentFinder;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.data.LocalizedText;
import com.idega.block.text.data.TxText;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.util.LocaleUtil;

public class BuildingWSServiceBusinessBean extends IBOServiceBean implements
		BuildingWSServiceBusiness {

	public ComplexInfo[] getComplexInfo() {
		try {
			Collection complexCollection = this.getComplexHome()
					.findAllIncludingLocked();
			if (complexCollection != null && !complexCollection.isEmpty()) {
				ComplexInfo info[] = new ComplexInfo[complexCollection.size()];
				int i = 0;
				Iterator it = complexCollection.iterator();
				while (it.hasNext()) {
					Complex complex = (Complex) it.next();
					info[i] = new ComplexInfo();
					info[i].setId(((Integer) complex.getPrimaryKey())
							.intValue());
					info[i].setName(complex.getName());

					TxText txt = complex.getText();
					String infoTxt = null;
					String infoEnTxt = null;
					if (txt != null) {
						ContentHelper helper = ContentFinder.getContentHelper(
								txt.getContentId(), LocaleUtil
										.getIcelandicLocale());
						if (helper != null) {
							LocalizedText text = helper.getLocalizedText();
							if (text != null) {
								infoTxt = text.getBody().replaceAll("\\<.*?>",
										"");
								infoTxt = infoTxt.replaceAll("\\&nbsp;", "");
							}
						}
						helper = ContentFinder.getContentHelper(txt
								.getContentId(), Locale.ENGLISH);
						if (helper != null) {
							LocalizedText text = helper.getLocalizedText();
							if (text != null) {
								infoEnTxt = text.getBody().replaceAll(
										"\\<.*?>", "");
								infoEnTxt = infoEnTxt
										.replaceAll("\\&nbsp;", "");
							}
						}
					}

					if (complex.getInfo() != null) {
						info[i].setInfo(complex.getInfo());
					} else if (infoTxt != null) {
						info[i].setInfo(infoTxt);
					} else {
						info[i].setInfo("");
					}

					if (infoEnTxt != null) {
						info[i].setEnglishInfo(infoEnTxt);
					} else {
						info[i].setEnglishInfo("");
					}

					i++;
				}

				return info;
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	public BuildingInfo[] getBuildingInfo(ComplexInfo complex) {
		try {
			Collection buildingCollection = getBuildingHome().findByComplex(
					new Integer(complex.getId()));
			if (buildingCollection != null && !buildingCollection.isEmpty()) {
				BuildingInfo info[] = new BuildingInfo[buildingCollection
						.size()];
				int i = 0;
				Iterator it = buildingCollection.iterator();
				while (it.hasNext()) {
					Building building = (Building) it.next();
					info[i] = new BuildingInfo();
					info[i].setId(((Integer) building.getPrimaryKey())
							.intValue());
					info[i].setName(building.getName());
					info[i].setAddress(building.getStreet());

					TxText txt = building.getText();
					String infoTxt = null;
					String infoEnTxt = null;
					if (txt != null) {
						ContentHelper helper = ContentFinder.getContentHelper(
								txt.getContentId(), LocaleUtil
										.getIcelandicLocale());
						if (helper != null) {
							LocalizedText text = helper.getLocalizedText();
							if (text != null) {
								infoTxt = text.getBody().replaceAll("\\<.*?>",
										"");
								infoTxt = infoTxt.replaceAll("\\&nbsp;", "");
							}
						}
						helper = ContentFinder.getContentHelper(txt
								.getContentId(), Locale.ENGLISH);
						if (helper != null) {
							LocalizedText text = helper.getLocalizedText();
							if (text != null) {
								infoEnTxt = text.getBody().replaceAll(
										"\\<.*?>", "");
								infoEnTxt = infoEnTxt
										.replaceAll("\\&nbsp;", "");
							}
						}
					}

					if (building.getInfo() != null) {
						info[i].setInfo(building.getInfo());
					} else if (infoTxt != null) {
						info[i].setInfo(infoTxt);
					} else {
						info[i].setInfo("");
					}

					if (infoEnTxt != null) {
						info[i].setEnglishInfo(infoEnTxt);
					} else {
						info[i].setEnglishInfo("");
					}

					i++;
				}

				return info;
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}

	public ApartmentInfo[] getApartmentInfo(BuildingInfo building) {
		try {
			Building b = getBuildingHome().findByPrimaryKey(
					new Integer(building.getId()));
			Collection floors = getFloorHome().findByBuilding((Integer) b.getPrimaryKey());
			if (floors != null && !floors.isEmpty()) {
				Collection apartmentCollection = new ArrayList();
				Iterator it = floors.iterator();
				while (it.hasNext()) {
					Floor floor = (Floor) it.next();
					try {
						apartmentCollection.addAll(getApartmentHome()
								.findByFloor(floor));
					} catch (FinderException e) {
					}
				}

				if (!apartmentCollection.isEmpty()) {
					ApartmentInfo info[] = new ApartmentInfo[apartmentCollection
							.size()];
					Map apartmentInfoMap = new HashMap();
					int i = 0;
					it = apartmentCollection.iterator();
					while (it.hasNext()) {
						Apartment apartment = (Apartment) it.next();
						info[i] = new ApartmentInfo();
						info[i].setId(((Integer) apartment.getPrimaryKey())
								.intValue());
						info[i].setName(apartment.getName());
						info[i].setSerialNumber(apartment.getSerialNumber());
						info[i].setFloor(apartment.getFloor().getName());
						ApartmentTypeInfoHolder holder = null;
						if (apartmentInfoMap.containsKey(apartment.getApartmentType().getPrimaryKey())) {
							holder = (ApartmentTypeInfoHolder) apartmentInfoMap.get(apartment.getApartmentType().getPrimaryKey());
						} else {
							holder = getApartmentTypeInfo(apartment.getApartmentType());
							apartmentInfoMap.put(apartment.getApartmentType().getPrimaryKey(), holder);
						}
						
						info[i].setType(holder.type);
						info[i].setTypeShortName(holder.typeShortName);
						info[i].setCategory(holder.category);
						info[i].setCollectiveFee(holder.collectiveFee);
						info[i].setElectricity(holder.electricity);
						info[i].setFurnished(holder.furnished);
						info[i].setHasAttic(holder.hasAttic);
						info[i].setHasBathroom(holder.hasBathroom);
						info[i].setHasKitchen(holder.hasKitchen);
						info[i].setHasStorageroom(holder.hasStorageroom);
						info[i].setHasStudyroom(holder.hasStudyroom);
						info[i].setHeat(holder.heat);
						info[i].setNumberOfRooms(holder.numberOfRooms);
						info[i].setRent(holder.rent);
						info[i].setSize(holder.size);
						info[i].setSubcategory(holder.subcategory);
						info[i].setTypeEnglishInfo(holder.typeEnglishInfo);
						info[i].setTypeInfo(holder.typeInfo);

						i++;
					}

					return info;
				}
			}

		} catch (FinderException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private ApartmentTypeInfoHolder getApartmentTypeInfo(ApartmentType type) {
		ApartmentTypeInfoHolder holder = new ApartmentTypeInfoHolder();
		if (type.getName() != null) {
			holder.type = type.getName();
		} else {
			holder.type = "";
		}
		if (type.getAbbreviation() != null) {
			holder.typeShortName = type.getAbbreviation();
		} else {
			holder.typeShortName = "";
		}
		holder.furnished = type.getFurniture();
		holder.hasAttic = type.getLoft();
		holder.hasBathroom = type.getBathRoom();
		holder.hasKitchen = type.getKitchen();
		holder.hasStorageroom = type.getStorage();
		holder.hasStudyroom = type.getStudy();
		holder.numberOfRooms = type.getRoomCount();
		holder.size = type.getArea();
		holder.category = "";			
		holder.subcategory = "";				
		if (type.getApartmentSubcategory() != null) {
			if (type.getApartmentSubcategory().getName() != null) {
				holder.subcategory = type.getApartmentSubcategory().getName();
			} 
			if (type.getApartmentSubcategory().getApartmentCategory() != null) {
				if (type.getApartmentSubcategory().getApartmentCategory().getName() != null) {
					holder.category = type.getApartmentSubcategory().getApartmentCategory().getName();								
				}
			}
		}
		TxText txt = type.getText();
		String infoTxt = null;
		String infoEnTxt = null;
		if (txt != null) {
			ContentHelper helper = ContentFinder.getContentHelper(
					txt.getContentId(), LocaleUtil
							.getIcelandicLocale());
			if (helper != null) {
				LocalizedText text = helper.getLocalizedText();
				if (text != null) {
					infoTxt = text.getBody().replaceAll("\\<.*?>",
							"");
					infoTxt = infoTxt.replaceAll("\\&nbsp;", "");
				}
			}
			helper = ContentFinder.getContentHelper(txt
					.getContentId(), Locale.ENGLISH);
			if (helper != null) {
				LocalizedText text = helper.getLocalizedText();
				if (text != null) {
					infoEnTxt = text.getBody().replaceAll(
							"\\<.*?>", "");
					infoEnTxt = infoEnTxt
							.replaceAll("\\&nbsp;", "");
				}
			}
		}

		if (type.getInfo() != null) {
			holder.typeInfo = type.getInfo();
		} else if (infoTxt != null) {
			holder.typeInfo = infoTxt;
		} else {
			holder.typeInfo = "";
		}

		if (infoEnTxt != null) {
			holder.typeEnglishInfo = infoEnTxt;
		} else {
			holder.typeEnglishInfo = "";
		}

		//get the rent!!
		double rent = 0.0d;
		double heat = 0.0d;
		double electricity = 0.0d;
		double collectiveFee = 0.0d;
		
		StringBuffer attribute = new StringBuffer("t_");
		attribute.append(type.getPrimaryKey());
		try {
			Collection tariffs = getTariffHome().findByAttribute(attribute.toString());
			if (tariffs != null && !tariffs.isEmpty()) {
				Iterator it = tariffs.iterator();
				while (it.hasNext()) {
					Tariff tariff = (Tariff) it.next();
					String indexType = tariff.getIndexType();
					if ("A".equals(indexType)) {
						rent += tariff.getPrice();
					} else if ("B".equals(indexType)) {
						rent += tariff.getPrice();
					} else if ("C".equals(indexType)) {
						electricity += tariff.getPrice();
					} else if ("D".equals(indexType)) {
						heat += tariff.getPrice();
					} else {
						collectiveFee += tariff.getPrice();
					}
				}
				
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}
		
		holder.rent = rent;
		holder.heat = heat;
		holder.electricity = electricity;
		holder.collectiveFee = collectiveFee;
		
		return holder;
	}

	private class ApartmentTypeInfoHolder {
		String category;
		double collectiveFee;
		double electricity;
		boolean furnished;
		boolean hasAttic;
		boolean hasBathroom;
		boolean hasKitchen;
		boolean hasStorageroom;
		boolean hasStudyroom;
		double heat;
		int numberOfRooms;
		double rent;
		double size;
		String subcategory;
		String type;
		String typeEnglishInfo;
		String typeInfo;
		String typeShortName;
	}

	private ComplexHome getComplexHome() {
		try {
			return (ComplexHome) IDOLookup.getHome(Complex.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}

	private BuildingHome getBuildingHome() {
		try {
			return (BuildingHome) IDOLookup.getHome(Building.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}

	private FloorHome getFloorHome() {
		try {
			return (FloorHome) IDOLookup.getHome(Floor.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}

	private ApartmentHome getApartmentHome() {
		try {
			return (ApartmentHome) IDOLookup.getHome(Apartment.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	private TariffHome getTariffHome() {
		try {
			return (TariffHome) IDOLookup.getHome(Tariff.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}
}