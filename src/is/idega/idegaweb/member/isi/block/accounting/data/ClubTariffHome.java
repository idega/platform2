package is.idega.idegaweb.member.isi.block.accounting.data;


public interface ClubTariffHome extends com.idega.data.IDOHome
{
 public ClubTariff create() throws javax.ejb.CreateException;
 public ClubTariff findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByClub(com.idega.user.data.Group p0)throws javax.ejb.FinderException;
 public java.util.Collection findAllByClubAndDivision(com.idega.user.data.Group p0,com.idega.user.data.Group p1)throws javax.ejb.FinderException;
 public java.util.Collection findAllValidByGroup(com.idega.user.data.Group p0)throws javax.ejb.FinderException;
 public java.util.Collection findByGroupAndTariffType(com.idega.user.data.Group p0,is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException;
 public java.util.Collection findByGroupAndTariffType(com.idega.user.data.Group p0,is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType p1)throws javax.ejb.FinderException;

}