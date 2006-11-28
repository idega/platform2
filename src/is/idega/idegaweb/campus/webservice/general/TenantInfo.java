package is.idega.idegaweb.campus.webservice.general;

import java.util.Date;

public class TenantInfo {
	private String personalID;
	
	private String buildingName;
	
	private String apartmentNumber;
	
	private Date movedInDate;
	
	public TenantInfo() {
	}
	
	public TenantInfo(String personalID, String buildingName, String apartmentNumber, Date movedInDate) {
		this.personalID = personalID;
		this.buildingName = buildingName;
		this.apartmentNumber = apartmentNumber;
		this.movedInDate = movedInDate;
	}
	
	//getters
	public String getPersonalID() {
		return personalID;
	}
	
	public String getBuildingName() {
		return buildingName;
	}
	
	public String getApartmentNumber() {
		return apartmentNumber;
	}
	
	public Date getMovedInDate() {
		return movedInDate;
	}
	
	//setters
	public void setPersonalID(String personalID) {
		this.personalID = personalID;
	}
	
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	
	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}
	
	public void setMovedInDate(Date movedInDate) {
		this.movedInDate = movedInDate;
	}
}