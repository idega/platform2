package is.idega.idegaweb.tracker.data;


public class UserAgentStatisticsHomeImpl extends com.idega.data.IDOFactory implements UserAgentStatisticsHome
{
 protected Class getEntityInterfaceClass(){
  return UserAgentStatistics.class;
 }

 public UserAgentStatistics create() throws javax.ejb.CreateException{
  return (UserAgentStatistics) super.idoCreate();
 }

 public UserAgentStatistics createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public UserAgentStatistics findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (UserAgentStatistics) super.idoFindByPrimaryKey(id);
 }

 public UserAgentStatistics findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UserAgentStatistics) super.idoFindByPrimaryKey(pk);
 }

 public UserAgentStatistics findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}