package is.idega.idegaweb.tracker.data;


public class DomainStatisticsHomeImpl extends com.idega.data.IDOFactory implements DomainStatisticsHome
{
 protected Class getEntityInterfaceClass(){
  return DomainStatistics.class;
 }

 public DomainStatistics create() throws javax.ejb.CreateException{
  return (DomainStatistics) super.idoCreate();
 }

 public DomainStatistics createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public DomainStatistics findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (DomainStatistics) super.idoFindByPrimaryKey(id);
 }

 public DomainStatistics findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (DomainStatistics) super.idoFindByPrimaryKey(pk);
 }

 public DomainStatistics findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}