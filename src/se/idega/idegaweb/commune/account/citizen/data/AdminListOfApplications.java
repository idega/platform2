/*
 * $Id: AdminListOfApplications.java,v 1.2 2002/11/01 10:51:00 staffan Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.idegaweb.commune.account.citizen.data;

/**
 * @author palli
 * @version 1.0
 */
public class AdminListOfApplications {
	private String _id;
	private String _name;
	private String _pid;
	private String _address;
	
	public AdminListOfApplications() {
		this(null,null,null,null);
	}
	
	public AdminListOfApplications(String id, String name, String pid, String address) {
		_id = id;
		_name = name;
		_pid = pid;
		_address = address;	
	}
	
	public String getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	public String getPID() {
		return _pid;
	}
	
	public String getAddress() {
		return _address;
	}
	
	public void setId(String id) {
		_id = id;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public void setPID(String pid) {
		_pid = pid;
	}
	
	public void setAddress(String address) {
		_address = address;
	}
}
