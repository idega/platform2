package se.idega.idegaweb.commune.reckon.data;


public interface KonteringStringHome extends com.idega.data.IDOHome
{
 public KonteringString create() throws javax.ejb.CreateException;
 public KonteringString findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findKonterignStrings()throws javax.ejb.FinderException;

}