package is.idega.idegaweb.member.isi.block.accounting.data;


public interface AssessmentRoundHome extends com.idega.data.IDOHome
{
 public AssessmentRound create() throws javax.ejb.CreateException;
 public AssessmentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}