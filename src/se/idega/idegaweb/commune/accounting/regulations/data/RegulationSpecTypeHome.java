package se.idega.idegaweb.commune.accounting.regulations.data;


public interface RegulationSpecTypeHome extends com.idega.data.IDOHome
{
 public RegulationSpecType create() throws javax.ejb.CreateException;
 public RegulationSpecType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllRegulationSpecTypes()throws javax.ejb.FinderException;
 public RegulationSpecType findRegulationSpecType(int p0)throws javax.ejb.FinderException;

}