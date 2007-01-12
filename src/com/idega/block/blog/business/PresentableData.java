/*
 * Created on 19.2.2004
 */
package com.idega.block.blog.business;

import java.util.HashMap;

import com.idega.core.localisation.data.ICLocale;
import com.idega.data.IDOEntity;

/**
 * Title:		GenericEntry
 * Description:
 * Copyright:	Copyright (c) 2004
 * Company:		idega Software
 * @author		2004 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */

//TODO Temporary location.  If it turns out to be useble it could be moved to com.idega.business

public abstract class PresentableData {
	
	private ICLocale _locale;
	private HashMap _data = new HashMap();
	
	public PresentableData(ICLocale locale){
		this._locale = locale;
	}
	
	protected void setData(String key, String data){
		if(data!=null){
			this._data.put(key,data);
		} else {
			System.out.println("[Warning!]: trying to set null data in GenericEntry");
		}
	}
	
	protected String getData(String key){
		return (String)this._data.get(key);
	}
	
	public abstract void load(IDOEntity entry);
	
	/* (non-Javadoc)
	 * @see com.idega.block.blog.business.DataEntryWrapper#getPrimaryKey()
	 */
	public abstract Object getPrimaryKey();
	
	public ICLocale getLocale(){
		return this._locale;
	}

}
