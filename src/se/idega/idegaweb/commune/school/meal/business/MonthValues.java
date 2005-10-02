/*
 * $Id: MonthValues.java,v 1.2 2005/10/02 13:44:24 laddi Exp $ Created on Aug 10, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package se.idega.idegaweb.commune.school.meal.business;


/**
 * Last modified: $Date: 2005/10/02 13:44:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.2 $
 */
public class MonthValues {

	private boolean monday = false;
	private boolean tuesday = false;
	private boolean wednesday = false;
	private boolean thursday = false;
	private boolean friday = false;

	private boolean milk = false;
	private boolean fruits = false;

	private float amount = 0;
	private float milkAmount = 0;
	private float fruitAmount = 0;
	private float mealAmount = 0;

	public float getMealAmount() {
		return mealAmount;
	}

	public void setMealAmount(float amount) {
		this.mealAmount = amount;
	}

	public float getMilkAmount() {
		return milkAmount;
	}

	public void setMilkAmount(float amount) {
		this.milkAmount = amount;
	}

	public float getFruitAmount() {
		return fruitAmount;
	}

	public void setFruitAmount(float amount) {
		this.fruitAmount = amount;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public float getAmount() {
		if (amount > 0) {
			return amount;
		}
		return getMealAmount() + getMilkAmount() + getFruitAmount();
	}

	public boolean isFriday() {
		return friday;
	}

	public void setFriday(boolean friday) {
		this.friday = friday;
	}

	public boolean isFruits() {
		return fruits;
	}

	public void setFruits(boolean fruits) {
		this.fruits = fruits;
	}

	public boolean isMilk() {
		return milk;
	}

	public void setMilk(boolean milk) {
		this.milk = milk;
	}

	public boolean isMonday() {
		return monday;
	}

	public void setMonday(boolean monday) {
		this.monday = monday;
	}

	public boolean isThursday() {
		return thursday;
	}

	public void setThursday(boolean thursday) {
		this.thursday = thursday;
	}

	public boolean isTuesday() {
		return tuesday;
	}

	public void setTuesday(boolean tuesday) {
		this.tuesday = tuesday;
	}

	public boolean isWednesday() {
		return wednesday;
	}

	public void setWednesday(boolean wednesday) {
		this.wednesday = wednesday;
	}
}