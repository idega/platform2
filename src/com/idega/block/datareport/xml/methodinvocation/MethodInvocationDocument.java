/*
 * Created on 10.8.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.idega.block.datareport.xml.methodinvocation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.DocType;

import com.idega.xml.XMLDocument;
import com.idega.xml.XMLElement;
import com.idega.xml.XMLException;

/**
 * Title:		MethodInvocationDocument
 * Description:
 * Copyright:	Copyright (c) 2003
 * Company:		idega Software
 * @author		2003 - idega team - <br><a href="mailto:gummi@idega.is">Gudmundur Agust Saemundsson</a><br>
 * @version		1.0
 */
public class MethodInvocationDocument extends XMLDocument {
	
	
	private List _methodDescriptions = new ArrayList();

	private XMLElement _rootElement=null;


	/**
	 * @param element
	 */
	private MethodInvocationDocument(XMLElement element) {
		super(element);
		setDocType();
	}
	
	/**
	 * @param element
	 */
	public MethodInvocationDocument() {
		this(new XMLElement("invoke"));
	}

	/**
	 * @param document
	 */
	public MethodInvocationDocument(XMLDocument document) throws XMLException {
		this();
		initialize(document);
	}
	
	private void setDocType(){
		DocType docType = new DocType("invoke");
		this.setDocType(docType);
	}
	
	private void initialize(XMLDocument document) throws XMLException {
		List methodDescriptions = document.getRootElement().getChildren(MethodDescription.NAME);
		Iterator iter = methodDescriptions.iterator();
		if(iter != null){
			while (iter.hasNext()) {
				XMLElement element = (XMLElement)iter.next();
				_methodDescriptions.add(new MethodDescription(element));
			}
		}
	}

	
	public XMLElement getRootElement() {
		if (_rootElement == null)
			_rootElement = super.getRootElement();

		return _rootElement;
	}
	
	public void setRootElement(XMLElement element) {
		_rootElement = element;
		super.setRootElement(element);
	}
	
	/**
	 * Use this method to close the document before writing it to file
	 */
	public void close(){
		Iterator iter = _methodDescriptions.iterator();
		while (iter.hasNext()) {
			MethodDescription element = (MethodDescription)iter.next();
			element.close();
			this.getRootElement().addContent(element);
		}
	}
	
	public List getMethodDescriptions(){
		return _methodDescriptions;
	}

}
