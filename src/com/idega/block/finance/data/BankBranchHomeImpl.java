package com.idega.block.finance.data;


public class BankBranchHomeImpl extends com.idega.data.IDOFactory implements BankBranchHome
{
 protected Class getEntityInterfaceClass(){
  return BankBranch.class;
 }


 public BankBranch create() throws javax.ejb.CreateException{
  return (BankBranch) super.createIDO();
 }


 public BankBranch findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (BankBranch) super.findByPrimaryKeyIDO(pk);
 }



}