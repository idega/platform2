package is.idega.idegaweb.travel.data;


public class ResellerDayHomeImpl extends com.idega.data.IDOFactory implements ResellerDayHome
{
 protected Class getEntityInterfaceClass(){
  return ResellerDay.class;
 }

 public ResellerDay create() throws javax.ejb.CreateException{
  return (ResellerDay) super.idoCreate();
 }

 public ResellerDay createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ResellerDay findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ResellerDay) super.idoFindByPrimaryKey(id);
 }

 public ResellerDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ResellerDay) super.idoFindByPrimaryKey(pk);
 }

 public ResellerDay findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}