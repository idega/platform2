package is.idega.idegaweb.project.data;


public class IPParticipantGroupHomeImpl extends com.idega.data.IDOFactory implements IPParticipantGroupHome
{
 protected Class getEntityInterfaceClass(){
  return IPParticipantGroup.class;
 }

 public IPParticipantGroup create() throws javax.ejb.CreateException{
  return (IPParticipantGroup) super.idoCreate();
 }

 public IPParticipantGroup createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public IPParticipantGroup findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (IPParticipantGroup) super.idoFindByPrimaryKey(id);
 }

 public IPParticipantGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (IPParticipantGroup) super.idoFindByPrimaryKey(pk);
 }

 public IPParticipantGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}