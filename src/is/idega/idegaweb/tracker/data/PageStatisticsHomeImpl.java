package is.idega.idegaweb.tracker.data;


public class PageStatisticsHomeImpl extends com.idega.data.IDOFactory implements PageStatisticsHome
{
 protected Class getEntityInterfaceClass(){
  return PageStatistics.class;
 }

 public PageStatistics create() throws javax.ejb.CreateException{
  return (PageStatistics) super.idoCreate();
 }

 public PageStatistics createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public PageStatistics findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PageStatistics) super.idoFindByPrimaryKey(id);
 }

 public PageStatistics findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PageStatistics) super.idoFindByPrimaryKey(pk);
 }

 public PageStatistics findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}