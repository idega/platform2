package com.idega.block.building.business;

import com.idega.block.building.data.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * @version 1.0
 */

public class ApartmentHolder {

  Complex complex;
  Building building;
  Floor floor;
  Apartment apartment;
  ApartmentType type;
  ApartmentCategory category;

  public ApartmentHolder(int iApartmentId) {
    apartment = BuildingCacher.getApartment(iApartmentId);
    floor = BuildingCacher.getFloor(apartment.getFloorId());
    type = BuildingCacher.getApartmentType(apartment.getApartmentTypeId());
    category = BuildingCacher.getApartmentCategory(type.getApartmentCategoryId());
    building = BuildingCacher.getBuilding(floor.getBuildingId());
    complex = BuildingCacher.getComplex(building.getComplexId());
  }

  public Apartment getApartment(){
    return apartment;
  }

  public Floor getFloor(){
    return floor;
  }

  public Building getBuilding(){
    return building;
  }

  public Complex getComplex(){
    return complex;
  }

  public ApartmentType getApartmentType(){
    return type;
  }

  public ApartmentCategory getApartmentCategory(){
    return category;
  }
}