package se.idega.idegaweb.commune.accounting.posting.data;


public interface PostingFieldHome extends com.idega.data.IDOHome
{
 public PostingField create() throws javax.ejb.CreateException;
 public PostingField findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllFieldsByPostingString(int p0)throws javax.ejb.FinderException;

}