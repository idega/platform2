package com.idega.block.school.data;


public class SchoolUserPaymentHomeImpl extends com.idega.data.IDOFactory implements SchoolUserPaymentHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolUserPayment.class;
 }


 public SchoolUserPayment create() throws javax.ejb.CreateException{
  return (SchoolUserPayment) super.createIDO();
 }


 public SchoolUserPayment findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolUserPayment) super.findByPrimaryKeyIDO(pk);
 }



}