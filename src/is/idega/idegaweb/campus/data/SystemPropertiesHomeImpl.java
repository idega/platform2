package is.idega.idegaweb.campus.data;


public class SystemPropertiesHomeImpl extends com.idega.data.IDOFactory implements SystemPropertiesHome
{
 protected Class getEntityInterfaceClass(){
  return SystemProperties.class;
 }

 public SystemProperties create() throws javax.ejb.CreateException{
  return (SystemProperties) super.createIDO();
 }

 public SystemProperties createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public SystemProperties findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (SystemProperties) super.idoFindByPrimaryKey(id);
 }

 public SystemProperties findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SystemProperties) super.findByPrimaryKeyIDO(pk);
 }

 public SystemProperties findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}