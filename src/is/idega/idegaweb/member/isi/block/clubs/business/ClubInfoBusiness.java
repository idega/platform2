package is.idega.idegaweb.member.isi.block.clubs.business;


public interface ClubInfoBusiness extends com.idega.business.IBOService
{
 public java.util.Collection getDivisionsForClub(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
 public java.util.Collection getFlocksForDivision(com.idega.user.data.Group p0) throws java.rmi.RemoteException;
}
