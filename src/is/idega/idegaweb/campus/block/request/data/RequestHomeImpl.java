package is.idega.idegaweb.campus.block.request.data;


public class RequestHomeImpl extends com.idega.data.IDOFactory implements RequestHome
{
 protected Class getEntityInterfaceClass(){
  return Request.class;
 }

 public Request create() throws javax.ejb.CreateException{
  return (Request) super.idoCreate();
 }

 public Request findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Request) super.idoFindByPrimaryKey(id);
 }

 public Request findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Request) super.idoFindByPrimaryKey(pk);
 }


}