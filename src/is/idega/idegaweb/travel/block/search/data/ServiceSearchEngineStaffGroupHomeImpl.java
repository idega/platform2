package is.idega.idegaweb.travel.block.search.data;


public class ServiceSearchEngineStaffGroupHomeImpl extends com.idega.data.IDOFactory implements ServiceSearchEngineStaffGroupHome
{
 protected Class getEntityInterfaceClass(){
  return ServiceSearchEngineStaffGroup.class;
 }


 public ServiceSearchEngineStaffGroup create() throws javax.ejb.CreateException{
  return (ServiceSearchEngineStaffGroup) super.createIDO();
 }


 public ServiceSearchEngineStaffGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ServiceSearchEngineStaffGroup) super.findByPrimaryKeyIDO(pk);
 }



}