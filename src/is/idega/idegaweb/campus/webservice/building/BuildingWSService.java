package is.idega.idegaweb.campus.webservice.building;

public interface BuildingWSService {
	public ComplexInfo[] getComplexInfo();
	public BuildingInfo[] getBuildingInfo(ComplexInfo complex);
	public ApartmentInfo[] getApartmentInfo(BuildingInfo building);
}