package is.idega.idegaweb.campus.block.application.data;


public class WaitingListHomeImpl extends com.idega.data.IDOFactory implements WaitingListHome
{
 protected Class getEntityInterfaceClass(){
  return WaitingList.class;
 }

 public WaitingList create() throws javax.ejb.CreateException{
  return (WaitingList) super.idoCreate();
 }

 public WaitingList createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public WaitingList findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (WaitingList) super.idoFindByPrimaryKey(id);
 }

 public WaitingList findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (WaitingList) super.idoFindByPrimaryKey(pk);
 }

 public WaitingList findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}