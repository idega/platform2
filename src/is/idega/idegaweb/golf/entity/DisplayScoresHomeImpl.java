package is.idega.idegaweb.golf.entity;


public class DisplayScoresHomeImpl extends com.idega.data.IDOFactory implements DisplayScoresHome
{
 protected Class getEntityInterfaceClass(){
  return DisplayScores.class;
 }

 public DisplayScores create() throws javax.ejb.CreateException{
  return (DisplayScores) super.idoCreate();
 }

 public DisplayScores createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public DisplayScores findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (DisplayScores) super.idoFindByPrimaryKey(id);
 }

 public DisplayScores findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (DisplayScores) super.idoFindByPrimaryKey(pk);
 }

 public DisplayScores findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}