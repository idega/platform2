package is.idega.idegaweb.golf.block.image.data;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Timestamp;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
public class ImageEntityBMPBean extends GenericEntity implements ImageEntity {
	public ImageEntityBMPBean() {
		super();
	}
	public ImageEntityBMPBean(int id) throws SQLException {
		super(id);
	}
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute("content_type", "Image type", true, true, "java.lang.String");
		addAttribute("image_value", "The image Value", false, false, "com.idega.data.BlobWrapper");
		addAttribute("image_name", "Image name", true, true, "java.lang.String");
		addAttribute("date_added", "Date added or changed", true, true, "java.sql.Timestamp");
		addAttribute("from_file", "Image from file?", true, true, "java.lang.Boolean");
		addAttribute("image_text", "Image text", true, true, "java.lang.String");
		addAttribute("image_link", "Image link", true, true, "java.lang.String");
		addAttribute("image_link_owner", "Which has a link the image/text/both/none?", true, true, "java.lang.String");
		addAttribute("image_size", "Image size in bytes", true, true, "java.lang.Integer");
		addAttribute("parent_id", "Image parent", true, true, "java.lang.Integer");
		addAttribute("width", "Image width", true, true, "java.lang.String");
		addAttribute("height", "Image height", true, true, "java.lang.String");
	}
	public void insertStartData() throws Exception {
		ImageEntity image = (ImageEntity)((ImageEntityHome)IDOLookup.getHomeLegacy(ImageEntity.class)).createLegacy();
		image.setName("Default no image");
		image.insert();
		ImageEntity image2 = (ImageEntity)((ImageEntityHome)IDOLookup.getHomeLegacy(ImageEntity.class)).createLegacy();
		image2.setID(-1);
		image2.setName("Default no image");
		image2.insert();
	}
	public String getEntityName() {
		return "image";
	}
	public void setDefaultValues() {
		this.setParentId(-1);
		this.setFromFile("N");
		this.setImageLinkOwner("none");
		this.setDate(new com.idega.util.IWTimestamp().getTimestampRightNow());
	}
	public String getContentType() {
		return (String) getColumnValue("content_type");
	}
	public void setContentType(String contentType) {
		setColumn("content_type", contentType);
	}
	
	/*public BlobWrapper getImageValue() {
		return (BlobWrapper) getColumnValue("image_value");
	}*/
	
	public InputStream getImageValue()throws Exception{
		return getInputStreamColumnValue("image_value");
	}
	
	/*public BlobWrapper getImage() {
		return (BlobWrapper) getImageValue();
	}*/
	
	/*public void setImageValue(BlobWrapper imageValue) {
		setColumn("image_value", imageValue);
	}*/
	
	public void setImageValue(InputStream fileValue){
    setColumn("image_value", fileValue);
  }
  
	public String getName() {
		return getImageName();
	}
	public String getImageName() {
		return (String) getColumnValue("image_name");
	}
	public void setImageName(String name) {
		setColumn("image_name", name);
	}
	public void setName(String name) {
		setImageName(name);
	}
	public String getFromFile() {
		return (String) getColumnValue("from_file");
	}
	public void setFromFile(String fromFile) {
		if (fromFile.toUpperCase().equals("Y")) {
			setFromFile(true);
		}
		else {
			setFromFile(false);
		}
	}
	public void setFromFile(boolean fromFile) {
		setColumn("from_file", fromFile);
	}
	public Timestamp getDateAdded() {
		return (Timestamp) getColumnValue("date_added");
	}
	public Timestamp getDate() {
		return getDateAdded();
	}
	public void setDateAdded(Timestamp dateAdded) {
		setColumn("date_added", dateAdded);
	}
	public void setDate(Timestamp dateAdded) {
		setDateAdded(dateAdded);
	}
	public String getText() {
		return getImageText();
	}
	public String getImageText() {
		return (String) getColumnValue("image_text");
	}
	public void setImageText(String imageText) {
		setColumn("image_text", imageText);
	}
	public String getImageLink() {
		return (String) getColumnValue("image_link");
	}
	public String getLink() {
		return getImageLink();
	}
	public void setImageLink(String imageLink) {
		setColumn("image_link", imageLink);
	}
	public String getWidth() {
		return (String) getColumnValue("width");
	}
	public void setWidth(String imageWidth) {
		setColumn("width", imageWidth);
	}
	public String getHeight() {
		return (String) getColumnValue("height");
	}
	public void setHeight(String imageHeight) {
		setColumn("height", imageHeight);
	}
	public String getImageLinkOwner() {
		return (String) getColumnValue("image_link_owner");
	}
	/*
	* possible option image/text/both/none
	*/
	public void setImageLinkOwner(String imageLinkOwner) {
		setColumn("image_link_owner", imageLinkOwner);
	}
	public void setParentId(int parent_id) {
		setColumn("parent_id", new Integer(parent_id));
	}
	public int getParentId() {
		return getIntColumnValue("parent_id");
	}
	public void setSize(int parent_id) {
		setColumn("image_size", new Integer(parent_id));
	}
	public int getSize() {
		return getIntColumnValue("image_size");
	}
}
