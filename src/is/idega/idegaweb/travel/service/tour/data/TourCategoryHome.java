package is.idega.idegaweb.travel.service.tour.data;


public interface TourCategoryHome extends com.idega.data.IDOHome
{
 public TourCategory create() throws javax.ejb.CreateException;
 public TourCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;

}