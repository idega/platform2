package is.idega.idegaweb.golf.block.text.data;


public class TextModuleHomeImpl extends com.idega.data.IDOFactory implements TextModuleHome
{
 protected Class getEntityInterfaceClass(){
  return TextModule.class;
 }


 public TextModule create() throws javax.ejb.CreateException{
  return (TextModule) super.createIDO();
 }


 public TextModule createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public TextModule findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (TextModule) super.findByPrimaryKeyIDO(pk);
 }


 public TextModule findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (TextModule) super.findByPrimaryKeyIDO(id);
 }


 public TextModule findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}