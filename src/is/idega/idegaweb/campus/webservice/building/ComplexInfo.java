package is.idega.idegaweb.campus.webservice.building;

public class ComplexInfo {
	private int id;
	private String name;
	private String info;
	private String englishInfo;
	
	public ComplexInfo() {
		
	}
	
	public ComplexInfo(int id, String name, String info, String englishInfo) {
		this.id = id;
		this.name = name;
		this.info = info;
		this.englishInfo = englishInfo;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getInfo() {
		return this.info;
	}

	public String getEnglishInfo() {
		return this.englishInfo;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public void setEnglishInfo(String info) {
		this.englishInfo = info;
	}
}