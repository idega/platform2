package is.idega.idegaweb.member.isi.block.accounting.data;


public interface ClubTariffHome extends com.idega.data.IDOHome
{
 public ClubTariff create() throws javax.ejb.CreateException;
 public ClubTariff findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByClub(com.idega.user.data.Group p0)throws javax.ejb.FinderException;

}