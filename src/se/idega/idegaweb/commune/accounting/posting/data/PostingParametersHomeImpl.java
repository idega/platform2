package se.idega.idegaweb.commune.accounting.posting.data;


public class PostingParametersHomeImpl extends com.idega.data.IDOFactory implements PostingParametersHome
{
 protected Class getEntityInterfaceClass(){
  return PostingParameters.class;
 }


 public PostingParameters create() throws javax.ejb.CreateException{
  return (PostingParameters) super.createIDO();
 }


public java.util.Collection findAllPostingParameters()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PostingParametersBMPBean)entity).ejbFindAllPostingParameters();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public PostingParameters findPostingParameter(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PostingParametersBMPBean)entity).ejbFindPostingParameter(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public PostingParameters findPostingParameter(java.sql.Date p0,int p1,int p2,int p3,int p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PostingParametersBMPBean)entity).ejbFindPostingParameter(p0,p1,p2,p3,p4);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public PostingParameters findPostingParameter(java.sql.Date p0,int p1,int p2,java.lang.String p3,int p4,int p5,int p6)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PostingParametersBMPBean)entity).ejbFindPostingParameter(p0,p1,p2,p3,p4,p5,p6);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public PostingParameters findPostingParameter(java.sql.Date p0,java.sql.Date p1,java.lang.String p2,java.lang.String p3,int p4,int p5,java.lang.String p6,int p7,int p8,int p9,int p10)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PostingParametersBMPBean)entity).ejbFindPostingParameter(p0,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findPostingParametersByDate(java.sql.Date p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PostingParametersBMPBean)entity).ejbFindPostingParametersByDate(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findPostingParametersByPeriode(java.sql.Date p0,java.sql.Date p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PostingParametersBMPBean)entity).ejbFindPostingParametersByPeriode(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public PostingParameters findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PostingParameters) super.findByPrimaryKeyIDO(pk);
 }



}