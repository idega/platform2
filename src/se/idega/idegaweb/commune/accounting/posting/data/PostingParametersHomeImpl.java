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

public PostingParameters findPostingParameter(int p0,int p1,int p2,int p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((PostingParametersBMPBean)entity).ejbFindPostingParameter(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findPostingParametersByPeriode(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PostingParametersBMPBean)entity).ejbFindPostingParametersByPeriode(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public PostingParameters findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PostingParameters) super.findByPrimaryKeyIDO(pk);
 }



}