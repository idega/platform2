package is.idega.idegaweb.member.isi.block.accounting.data;


public interface ClubTariffTypeHome extends com.idega.data.IDOHome
{
 public ClubTariffType create() throws javax.ejb.CreateException;
 public ClubTariffType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByClub(com.idega.user.data.Group p0)throws javax.ejb.FinderException;

}