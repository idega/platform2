package com.idega.block.dataquery.data.sql;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: Container class</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Dec 3, 2003
 */
public class InputDescription {
	
	private String description = null;
	private String handlerDescription = null;
	private String inputHandler = null;
	
	public InputDescription(String description, String inputHandler, String handlerDescription) {
		this.description = description;
		this.inputHandler = inputHandler;
		this.handlerDescription = handlerDescription;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getInputHandler() {
		return this.inputHandler;
	}

	public String getHandlerDescription() {
		return this.handlerDescription;
	}

}
