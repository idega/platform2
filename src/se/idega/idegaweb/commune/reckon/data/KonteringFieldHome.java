package se.idega.idegaweb.commune.reckon.data;


public interface KonteringFieldHome extends com.idega.data.IDOHome
{
 public KonteringField create() throws javax.ejb.CreateException;
 public KonteringField findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllFieldsByKonteringString(int p0)throws javax.ejb.FinderException;

}