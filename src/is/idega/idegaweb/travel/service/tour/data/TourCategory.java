package is.idega.idegaweb.travel.service.tour.data;


public interface TourCategory extends com.idega.data.IDOEntity
{
	public static String CATEGORY_TRANSPORTATION = "TRANSPORTATION";
	public static String CATEGORY_EXCURSION = "EXCURSION";

 public java.lang.String getLocalizationKey();
 public java.lang.String getName();
 public java.lang.Class getPrimaryKeyClass();
 public void setLocalizationKey(java.lang.String p0);
 public void setName(java.lang.String p0);
}
