package com.idega.block.building.business;


public class BuildingServiceHomeImpl extends com.idega.business.IBOHomeImpl implements BuildingServiceHome
{
 protected Class getBeanInterfaceClass(){
  return BuildingService.class;
 }


 public BuildingService create() throws javax.ejb.CreateException{
  return (BuildingService) super.createIBO();
 }



}