package is.idega.idegaweb.travel.service.tour.data;


public interface TourTypeHome extends com.idega.data.IDOHome
{
 public TourType create() throws javax.ejb.CreateException;
 public TourType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByCategory(java.lang.String p0)throws javax.ejb.FinderException;

}