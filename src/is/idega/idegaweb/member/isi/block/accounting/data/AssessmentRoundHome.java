package is.idega.idegaweb.member.isi.block.accounting.data;


public interface AssessmentRoundHome extends com.idega.data.IDOHome
{
 public AssessmentRound create() throws javax.ejb.CreateException;
 public AssessmentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllByClubAndDivision(com.idega.user.data.Group p0,com.idega.user.data.Group p1)throws javax.ejb.FinderException;

}