package is.idega.idegaweb.golf.entity;


public class StatisticHomeImpl extends com.idega.data.IDOFactory implements StatisticHome
{
 protected Class getEntityInterfaceClass(){
  return Statistic.class;
 }

 public Statistic create() throws javax.ejb.CreateException{
  return (Statistic) super.idoCreate();
 }

 public Statistic createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public Statistic findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Statistic) super.idoFindByPrimaryKey(id);
 }

 public Statistic findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Statistic) super.idoFindByPrimaryKey(pk);
 }

 public Statistic findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}