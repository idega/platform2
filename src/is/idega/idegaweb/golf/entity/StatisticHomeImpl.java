package is.idega.idegaweb.golf.entity;


public class StatisticHomeImpl extends com.idega.data.IDOFactory implements StatisticHome
{
 protected Class getEntityInterfaceClass(){
  return Statistic.class;
 }


 public Statistic create() throws javax.ejb.CreateException{
  return (Statistic) super.createIDO();
 }


 public Statistic createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }


public java.util.Collection findByTeeID(java.util.Collection p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((StatisticBMPBean)entity).ejbFindByTeeID(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Statistic findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Statistic) super.findByPrimaryKeyIDO(pk);
 }


 public Statistic findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (Statistic) super.findByPrimaryKeyIDO(id);
 }


 public Statistic findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


public int getCountByTeeId(java.util.Collection p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((StatisticBMPBean)entity).ejbHomeGetCountByTeeId(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOnFairwayByTeeID(java.util.Collection p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((StatisticBMPBean)entity).ejbHomeGetNumberOnFairwayByTeeID(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getNumberOnGreenByTeeID(java.util.Collection p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((StatisticBMPBean)entity).ejbHomeGetNumberOnGreenByTeeID(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public double getPuttAverageByTeeID(java.util.Collection p0)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	double theReturn = ((StatisticBMPBean)entity).ejbHomeGetPuttAverageByTeeID(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}