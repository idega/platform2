package is.idega.idegaweb.campus.block.phone.data;


public class PhoneFileInfoHomeImpl extends com.idega.data.IDOFactory implements PhoneFileInfoHome
{
 protected Class getEntityInterfaceClass(){
  return PhoneFileInfo.class;
 }

 public PhoneFileInfo create() throws javax.ejb.CreateException{
  return (PhoneFileInfo) super.createIDO();
 }

 public PhoneFileInfo createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public PhoneFileInfo findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PhoneFileInfo) super.idoFindByPrimaryKey(id);
 }

 public PhoneFileInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PhoneFileInfo) super.findByPrimaryKeyIDO(pk);
 }

 public PhoneFileInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}