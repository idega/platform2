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
  
  public ApartmentHolder(){
  
  }
  
  public ApartmentHolder(ApartmentView view){
  		setBuilding(view.getBuilding());
  		setComplex(view.getComplex());
  		setFloor(view.getFloor());
  		setType(view.getType());
  		setApartment(view.getApartment());
  		setCategory(view.getCategory());
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
/**
 * @return Returns the category.
 */
public ApartmentCategory getCategory() {
	return category;
}

/**
 * @param category The category to set.
 */
public void setCategory(ApartmentCategory category) {
	this.category = category;
}

/**
 * @return Returns the type.
 */
public ApartmentType getType() {
	return type;
}

/**
 * @param type The type to set.
 */
public void setType(ApartmentType type) {
	this.type = type;
}

/**
 * @param apartment The apartment to set.
 */
public void setApartment(Apartment apartment) {
	this.apartment = apartment;
}

/**
 * @param building The building to set.
 */
public void setBuilding(Building building) {
	this.building = building;
}

/**
 * @param complex The complex to set.
 */
public void setComplex(Complex complex) {
	this.complex = complex;
}

/**
 * @param floor The floor to set.
 */
public void setFloor(Floor floor) {
	this.floor = floor;
}

}
