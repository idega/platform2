package se.idega.idegaweb.commune.accounting.school.data;


public class ProviderAccountingPropertiesHomeImpl extends com.idega.data.IDOFactory implements ProviderAccountingPropertiesHome
{
 protected Class getEntityInterfaceClass(){
  return ProviderAccountingProperties.class;
 }


 public ProviderAccountingProperties create() throws javax.ejb.CreateException{
  return (ProviderAccountingProperties) super.createIDO();
 }


 public ProviderAccountingProperties findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ProviderAccountingProperties) super.findByPrimaryKeyIDO(pk);
 }



}