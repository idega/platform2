package is.idega.idegaweb.tracker.data;


public class PageTotalStatisticsHomeImpl extends com.idega.data.IDOFactory implements PageTotalStatisticsHome
{
 protected Class getEntityInterfaceClass(){
  return PageTotalStatistics.class;
 }

 public PageTotalStatistics create() throws javax.ejb.CreateException{
  return (PageTotalStatistics) super.idoCreate();
 }

 public PageTotalStatistics createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public PageTotalStatistics findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (PageTotalStatistics) super.idoFindByPrimaryKey(id);
 }

 public PageTotalStatistics findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PageTotalStatistics) super.idoFindByPrimaryKey(pk);
 }

 public PageTotalStatistics findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}