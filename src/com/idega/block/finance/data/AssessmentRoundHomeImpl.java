package com.idega.block.finance.data;


public class AssessmentRoundHomeImpl extends com.idega.data.IDOFactory implements AssessmentRoundHome
{
 protected Class getEntityInterfaceClass(){
  return AssessmentRound.class;
 }

 public AssessmentRound create() throws javax.ejb.CreateException{
  return (AssessmentRound) super.idoCreate();
 }

 public AssessmentRound createLegacy(){
	try{
		return create();
	}
	catch(javax.ejb.CreateException ce){
		throw new RuntimeException("CreateException:"+ce.getMessage());
	}

 }

 public AssessmentRound findByPrimaryKey(int id) throws javax.ejb.FinderException{
  return (AssessmentRound) super.idoFindByPrimaryKey(id);
 }

 public AssessmentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (AssessmentRound) super.idoFindByPrimaryKey(pk);
 }

 public AssessmentRound findByPrimaryKeyLegacy(int id) throws java.sql.SQLException{
	try{
		return findByPrimaryKey(id);
	}
	catch(javax.ejb.FinderException fe){
		throw new java.sql.SQLException(fe.getMessage());
	}

 }


}