package se.idega.idegaweb.commune.accounting.regulations.data;


public interface KeyMappingHome extends com.idega.data.IDOHome
{
 public KeyMapping create() throws javax.ejb.CreateException;
 public KeyMapping findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public KeyMapping findValueByCategoryAndKey(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException;

}