package is.idega.idegaweb.travel.service.tour.data;


public interface TourHome extends com.idega.data.IDOHome
{
 public Tour create() throws javax.ejb.CreateException;
 public Tour findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection find(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1,java.lang.Object[] p2,java.lang.Object[] p3,java.lang.Object[] p4,String p5)throws javax.ejb.FinderException;

}