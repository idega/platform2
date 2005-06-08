/*
 * $Id: Building.java,v 1.14 2005/06/08 11:42:06 palli Exp $
 * Created on Jun 6, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.building.data;

import java.util.Collection;


/**
 * 
 *  Last modified: $Date: 2005/06/08 11:42:06 $ by $Author: palli $
 * 
 * @author <a href="mailto:palli@idega.com">palli</a>
 * @version $Revision: 1.14 $
 */
public interface Building extends BuildingEntity {

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getComplexId
	 */
	public int getComplexId();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getComplex
	 */
	public Complex getComplex();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setComplexId
	 */
	public void setComplexId(int complex_id);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getImageId
	 */
	public int getImageId();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setImageId
	 */
	public void setImageId(int image_id);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setImageId
	 */
	public void setImageId(Integer image_id);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getStreet
	 */
	public String getStreet();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setStreet
	 */
	public void setStreet(String street);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getStreetNumber
	 */
	public String getStreetNumber();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setStreetNumber
	 */
	public void setStreetNumber(String street_number);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getSerie
	 */
	public String getSerie();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setSerie
	 */
	public void setSerie(String serie);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getDivision
	 */
	public String getDivision();

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#setDivision
	 */
	public void setDivision(String division);

	/**
	 * @see com.idega.block.building.data.BuildingBMPBean#getFloors
	 */
	public Collection getFloors();

}
