package com.idega.block.finance.data;


public class FinanceCategoryHomeImpl extends com.idega.data.IDOFactory implements FinanceCategoryHome
{
 protected Class getEntityInterfaceClass(){
  return FinanceCategory.class;
 }


 public FinanceCategory create() throws javax.ejb.CreateException{
  return (FinanceCategory) super.createIDO();
 }


 public FinanceCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (FinanceCategory) super.findByPrimaryKeyIDO(pk);
 }



}