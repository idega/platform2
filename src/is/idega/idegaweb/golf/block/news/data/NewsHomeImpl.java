package is.idega.idegaweb.golf.block.news.data;


public class NewsHomeImpl extends com.idega.data.IDOFactory implements NewsHome
{
 protected Class getEntityInterfaceClass(){
  return News.class;
 }


 public News create() throws javax.ejb.CreateException{
  return (News) super.createIDO();
 }


 public News createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


 public News findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (News) super.findByPrimaryKeyIDO(pk);
 }


 public News findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (News) super.findByPrimaryKeyIDO(id);
 }


 public News findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }



}