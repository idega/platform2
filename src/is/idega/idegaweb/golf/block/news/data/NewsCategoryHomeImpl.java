package is.idega.idegaweb.golf.block.news.data;


public class NewsCategoryHomeImpl extends com.idega.data.IDOFactory implements NewsCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return NewsCategory.class;
 }


 public NewsCategory create() throws javax.ejb.CreateException{
  return (NewsCategory) super.createIDO();
 }


 public NewsCategory createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public NewsCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (NewsCategory) super.findByPrimaryKeyIDO(pk);
 }


 public NewsCategory findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (NewsCategory) super.findByPrimaryKeyIDO(id);
 }


 public NewsCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}