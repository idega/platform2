package se.idega.idegaweb.commune.accounting.regulations.data;


public interface RegulationHome extends com.idega.data.IDOHome
{
 public Regulation create() throws javax.ejb.CreateException;
 public Regulation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}