package is.idega.idegaweb.campus.block.application.data;


public interface Priority extends com.idega.data.IDOEntity
{
	public String getPriority();
	public void setPriority(String priority) ;
	public void setDescription(String desc);
	public String getDescription();
	public String getHexColor();
	public void setHexColor(String color);
}
