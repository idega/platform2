package is.idega.experimental.idotest;


public class ResponseHomeImpl extends com.idega.data.IDOFactory implements ResponseHome
{
 protected Class getEntityInterfaceClass(){
  return Response.class;
 }

 public Response create() throws javax.ejb.CreateException{
  return (Response) super.idoCreate();
 }

 public Response findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Response) super.idoFindByPrimaryKey(id);
 }

 public Response findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Response) super.idoFindByPrimaryKey(pk);
 }


}