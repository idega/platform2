package is.idega.idegaweb.travel.data;



public interface Inquery extends com.idega.data.IDOEntity
{
 public void setAnswered(boolean p0) throws java.rmi.RemoteException;
 public void setAnswerDate(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setBookingId(int p0) throws java.rmi.RemoteException;
 public java.util.List getMultibleInquiries(is.idega.idegaweb.travel.data.Inquery p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.sql.Timestamp getInqueryDate() throws java.rmi.RemoteException;
 public int getNumberOfSeats() throws java.rmi.RemoteException;
 public void setServiceID(int p0) throws java.rmi.RemoteException;
 public boolean getIfAnswered() throws java.rmi.RemoteException;
 public void setEmail(java.lang.String p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.GeneralBooking getBooking()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.lang.String getInquery() throws java.rmi.RemoteException;
 public java.lang.String getEmail() throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.Service getService() throws java.rmi.RemoteException;
 public java.sql.Timestamp getInqueryPostDate() throws java.rmi.RemoteException;
 public java.util.Collection getResellers()throws com.idega.data.IDORelationshipException, java.rmi.RemoteException;
 public int getBookingId() throws java.rmi.RemoteException;
 public java.sql.Timestamp getAnswerDate() throws java.rmi.RemoteException;
 public void setIfAnswered(boolean p0) throws java.rmi.RemoteException;
 public boolean getAnswered() throws java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void addReseller(com.idega.block.trade.stockroom.data.Reseller p0)throws com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public int getServiceID() throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setNumberOfSeats(int p0) throws java.rmi.RemoteException;
 public void setInqueryPostDate(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setInquery(java.lang.String p0) throws java.rmi.RemoteException;
 public void setInqueryDate(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setAuthorizationString(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getAuthorizationString() throws java.rmi.RemoteException;
 public void setInqueryType(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getInqueryType() throws java.rmi.RemoteException;
}
