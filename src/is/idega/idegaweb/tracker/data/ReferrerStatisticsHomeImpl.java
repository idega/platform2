package is.idega.idegaweb.tracker.data;


public class ReferrerStatisticsHomeImpl extends com.idega.data.IDOFactory implements ReferrerStatisticsHome
{
 protected Class getEntityInterfaceClass(){
  return ReferrerStatistics.class;
 }

 public ReferrerStatistics create() throws javax.ejb.CreateException{
  return (ReferrerStatistics) super.idoCreate();
 }

 public ReferrerStatistics createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public ReferrerStatistics findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (ReferrerStatistics) super.idoFindByPrimaryKey(id);
 }

 public ReferrerStatistics findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ReferrerStatistics) super.idoFindByPrimaryKey(pk);
 }

 public ReferrerStatistics findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}