package is.idega.idegaweb.golf.block.news.data;


public class NewsCategoryAttributesHomeImpl extends com.idega.data.IDOFactory implements NewsCategoryAttributesHome
{
 protected Class getEntityInterfaceClass(){
  return NewsCategoryAttributes.class;
 }


 public NewsCategoryAttributes create() throws javax.ejb.CreateException{
  return (NewsCategoryAttributes) super.createIDO();
 }


 public NewsCategoryAttributes createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public NewsCategoryAttributes findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (NewsCategoryAttributes) super.findByPrimaryKeyIDO(pk);
 }


 public NewsCategoryAttributes findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (NewsCategoryAttributes) super.findByPrimaryKeyIDO(id);
 }


 public NewsCategoryAttributes findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}