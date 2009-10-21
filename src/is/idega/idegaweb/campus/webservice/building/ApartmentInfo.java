package is.idega.idegaweb.campus.webservice.building;

public class ApartmentInfo {
	private int id;
	private String name;
	private String serialNumber;
	private String floor;
	private String type;
	private String typeShortName;
	private String typeInfo;
	private String typeEnglishInfo;
	private String category;
	private String subcategory;
	private int numberOfRooms;
	private double size;
	private boolean hasKitchen;
	private boolean hasBathroom;
	private boolean hasStorageroom;
	private boolean hasStudyroom;
	private boolean hasAttic;
	private boolean isFurnished;
	
	private double rent;
	private double heat;
	private double electricity;
	private double collectiveFee;
	
	public ApartmentInfo() {
		
	}
	
	public ApartmentInfo(int id, String name, String serialNumber, String floor) {
		this.id = id;
		this.name = name;
		this.serialNumber = serialNumber;
		this.floor = floor;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String getFloor() {
		return floor;
	}

	public String getType() {
		return type;
	}

	public String getTypeShortName() {
		return typeShortName;
	}

	public String getTypeInfo() {
		return typeInfo;
	}

	public String getTypeEnglishInfo() {
		return typeEnglishInfo;
	}

	public String getCategory() {
		return category;
	}

	public String getSubcategory() {
		return subcategory;
	}

	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public double getSize() {
		return size;
	}

	public boolean hasKitchen() {
		return hasKitchen;
	}

	public boolean hasBathroom() {
		return hasBathroom;
	}

	public boolean hasStorageroom() {
		return hasStorageroom;
	}

	public boolean hasStudyroom() {
		return hasStudyroom;
	}

	public boolean hasAttic() {
		return hasAttic;
	}

	public boolean isFurnished() {
		return isFurnished;
	}

	public double getRent() {
		return rent;
	}

	public double getHeat() {
		return heat;
	}

	public double getElectricity() {
		return electricity;
	}

	public double getCollectiveFee() {
		return collectiveFee;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTypeShortName(String typeShortName) {
		this.typeShortName = typeShortName;
	}

	public void setTypeInfo(String typeInfo) {
		this.typeInfo = typeInfo;
	}

	public void setTypeEnglishInfo(String typeEnglishInfo) {
		this.typeEnglishInfo = typeEnglishInfo;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public void setHasKitchen(boolean hasKitchen) {
		this.hasKitchen = hasKitchen;
	}

	public void setHasBathroom(boolean hasBathroom) {
		this.hasBathroom = hasBathroom;
	}

	public void setHasStorageroom(boolean hasStorageroom) {
		this.hasStorageroom = hasStorageroom;
	}

	public void setHasStudyroom(boolean hasStudyroom) {
		this.hasStudyroom = hasStudyroom;
	}

	public void setHasAttic(boolean hasAttic) {
		this.hasAttic = hasAttic;
	}

	public void setFurnished(boolean isFurnished) {
		this.isFurnished = isFurnished;
	}

	public void setRent(double rent) {
		this.rent = rent;
	}

	public void setHeat(double heat) {
		this.heat = heat;
	}

	public void setElectricity(double electricity) {
		this.electricity = electricity;
	}

	public void setCollectiveFee(double collectiveFee) {
		this.collectiveFee = collectiveFee;
	}
}