package is.idega.idegaweb.campus.webservice.building;

public class BuildingInfo {
	private int id;
	private String name;
	private String address;
	private String info;
	private String englishInfo;
	
	public BuildingInfo() {
		
	}
	
	public BuildingInfo(int id, String name, String address, String info, String englishInfo) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.info = info;
		this.englishInfo = englishInfo;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getInfo() {
		return info;
	}

	public String getEnglishInfo() {
		return englishInfo;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setEnglishInfo(String englishInfo) {
		this.englishInfo = englishInfo;
	}
}