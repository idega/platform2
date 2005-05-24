/*
 * $Id: Runner.java,v 1.1 2005/05/24 12:06:29 laddi Exp $ Created on May 16, 2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package is.idega.idegaweb.marathon.business;

import is.idega.idegaweb.marathon.data.Distance;
import is.idega.idegaweb.marathon.data.Run;
import java.sql.Date;
import com.idega.core.location.data.Country;
import com.idega.user.data.Gender;
import com.idega.user.data.User;

/**
 * A holder class for information about runners and their selection when
 * registering.
 * 
 * Last modified: $Date: 2005/05/24 12:06:29 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class Runner {

	private User user;
	private Run run;
	private Distance distance;

	private boolean rentChip;
	private boolean ownChip;
	private boolean buyChip;
	private String chipNumber;
	private String shirtSize;

	private String name;
	private String personalID;
	private Date dateOfBirth;
	private String address;
	private String city;
	private String postalCode;
	private Country country;
	private Gender gender;
	private Country nationality;
	private String email;
	private String homePhone;
	private String mobilePhone;
	private boolean agree;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Run getRun() {
		return run;
	}

	public void setRun(Run run) {
		this.run = run;
	}

	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	public boolean isRentChip() {
		return rentChip;
	}

	public void setRentChip(boolean rentChip) {
		this.rentChip = rentChip;
		if (rentChip) {
			setOwnChip(false);
			setBuyChip(false);
		}
	}

	public boolean isOwnChip() {
		return ownChip;
	}

	public void setOwnChip(boolean ownChip) {
		this.ownChip = ownChip;
		if (ownChip) {
			setRentChip(false);
			setBuyChip(false);
		}
	}

	public boolean isBuyChip() {
		return buyChip;
	}

	public void setBuyChip(boolean buyChip) {
		this.buyChip = buyChip;
		if (buyChip) {
			setOwnChip(false);
			setRentChip(false);
		}
	}

	public String getChipNumber() {
		return chipNumber;
	}

	public void setChipNumber(String chipNumber) {
		this.chipNumber = chipNumber;
	}

	public String getShirtSize() {
		return shirtSize;
	}

	public void setShirtSize(String shirtSize) {
		this.shirtSize = shirtSize;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Country getNationality() {
		return nationality;
	}

	public void setNationality(Country nationality) {
		this.nationality = nationality;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPersonalID() {
		return personalID;
	}

	public void setPersonalID(String personalID) {
		this.personalID = personalID;
	}
	
	public Gender getGender() {
		return gender;
	}
	
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	
	public boolean isAgree() {
		return agree;
	}

	
	public void setAgree(boolean agree) {
		this.agree = agree;
	}
}