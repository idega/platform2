package se.idega.idegaweb.commune.accounting.posting.data;


public interface PostingStringHome extends com.idega.data.IDOHome
{
 public PostingString create() throws javax.ejb.CreateException;
 public PostingString findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findKonterignStrings()throws javax.ejb.FinderException;
 public PostingString findPostingStringByDate(java.sql.Date p0)throws javax.ejb.FinderException;

}