package is.idega.idegaweb.travel.service.hotel.data;


public interface HotelHome extends com.idega.data.IDOHome
{
 public Hotel create() throws javax.ejb.CreateException;
 public Hotel findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection find(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1,java.lang.Object[] p2,java.lang.Object[] p3,java.lang.Object[] p4,java.lang.Object[] p5,float p6,float p7,String p8)throws javax.ejb.FinderException;
 public java.util.Collection find(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1,java.lang.Object[] p2,java.lang.Object[] p3,java.lang.Object[] p4,String p5)throws javax.ejb.FinderException;

}