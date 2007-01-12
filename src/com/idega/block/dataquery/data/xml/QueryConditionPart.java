/*
 * Created on May 27, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package com.idega.block.dataquery.data.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOEntityField;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.repository.data.RefactorClassRegistry;
import com.idega.util.StringHandler;
import com.idega.xml.XMLElement;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */

public class QueryConditionPart implements QueryPart {
	
	private String id = null;
	private IDOEntityField idoField = null;
	private String field = null;
	private IDOEntityDefinition entityDef = null;
	private String entity = null;
	private String path = null;
	private String type = null;
	
	// pattern variables
	private String pattern = null;
	// variabe above xor variable below is set
	private Collection patterns = null;
	
	private String patternField = null;
	private String patternPath = null;
	
	private String description = null;
	private boolean lock = false;
	private boolean dynamic = false;
	
	public static final String TYPE_LIKE = "like";
	public static final String TYPE_EQ = "equal";
	public static final String TYPE_NEQ = "not-equal";
	public static final String TYPE_LT = "less";
	public static final String TYPE_GT ="greater";
	public static final String TYPE_GEQ = "greater-than-or-equals";
	public static final String TYPE_LEQ = "less-than-or-equals";
	public static final String PREFIX = "Cond";
	private static final String[] TYPES = { TYPE_LIKE,TYPE_EQ,TYPE_NEQ,TYPE_LT,TYPE_GT,TYPE_GEQ,TYPE_LEQ};
	
	public static String[] getConditionTypes(){
		return   TYPES;
	}
	
	public QueryConditionPart() {
	}
	
	
//	public QueryConditionPart(int idNumber, String entity,String path, String field, String type, String pattern, String description) {
//		this(new StringBuffer(PREFIX).append(idNumber).toString(), entity, path, field, type, pattern, description);
//	}
	
//	public QueryConditionPart(String id, String entity,String path, String field, String type, String pattern, String description){
//		this.id = id;
//		this.entity = entity;
//		this.path = path;
//		this.field = field;
//		this.type = type;
//		this.pattern = pattern;
//		this.description = description;
//	}
	
	
//	public QueryConditionPart(int idNumber, String entity,String path, String field, String type, Collection patterns, String description) {
//		this(new StringBuffer(PREFIX).append(idNumber).toString(), entity, path, field, type, patterns, description);
//	}

	
//	public QueryConditionPart(String id, String entity,String path, String field, String type, Collection patterns, String description){
//		this(id, entity, path, field, type, (String) null, description);
//		this.patterns = patterns;
//	}

	
	public QueryConditionPart(XMLElement xml){
		this.id = xml.getAttribute(QueryXMLConstants.ID).getValue();
		this.entity = xml.getAttribute(QueryXMLConstants.ENTITY).getValue();
		this.path = xml.getAttribute(QueryXMLConstants.PATH).getValue();
		this.field = xml.getAttribute(QueryXMLConstants.FIELD).getValue();
		this.type = xml.getAttribute(QueryXMLConstants.TYPE).getValue();
		this.patternField = xml.getTextTrim(QueryXMLConstants.FIELD_AS_PATTERN_FIELD);
		this.patternPath = xml.getTextTrim(QueryXMLConstants.FIELD_AS_PATTERN_PATH);
		if(xml.hasChildren()){
			List xmlPatterns = xml.getChildren(QueryXMLConstants.PATTERN);
			Iterator iterator = xmlPatterns.iterator();
			if (xmlPatterns.size() == 1) {
				this.pattern = ((XMLElement) iterator.next()).getTextTrim();
			}
			else {
				this.patterns = new ArrayList();
				while (iterator.hasNext()) {
					this.patterns.add(((XMLElement) iterator.next()).getTextTrim());
				}
			}
			XMLElement xmlLock = xml.getChild(QueryXMLConstants.LOCK);
			this.lock = xmlLock!=null;
			XMLElement xmlDyna = xml.getChild(QueryXMLConstants.DYNAMIC);
			this.dynamic = xmlDyna!=null;
			this.description = xml.getTextTrim(QueryXMLConstants.DESCRIPTION);
		}
	}
	
	public XMLElement getQueryElement() {
		XMLElement el = new XMLElement(QueryXMLConstants.CONDITION);
		el.setAttribute(QueryXMLConstants.ID, this.id);
		el.setAttribute(QueryXMLConstants.ENTITY,this.entity);
		el.setAttribute(QueryXMLConstants.PATH, this.path);
		el.setAttribute(QueryXMLConstants.FIELD,this.field);
		el.setAttribute(QueryXMLConstants.TYPE,this.type);
		if (this.patterns != null) {
			Iterator iterator = this.patterns.iterator();
			while (iterator.hasNext()) {
				String singlePattern = (String) iterator.next();
				addPattern(singlePattern, el);
			}
		}
		else {
			addPattern(this.pattern, el);
		}
		if (this.description != null) 	{
			XMLElement descriptionElement = new XMLElement(QueryXMLConstants.DESCRIPTION);
			descriptionElement.addContent(this.description);
			el.addContent(descriptionElement);
		}
		if (this.patternField != null) {
			XMLElement patternFieldElement = new XMLElement(QueryXMLConstants.FIELD_AS_PATTERN_FIELD);
			patternFieldElement.addContent(this.patternField);
			el.addContent(patternFieldElement);
		}
		if (this.patternPath != null) {
			XMLElement patternPathElement = new XMLElement(QueryXMLConstants.FIELD_AS_PATTERN_PATH);
			patternPathElement.addContent(this.patternPath);
			el.addContent(patternPathElement);
		}
		if(this.lock){
			el.addContent(new XMLElement(QueryXMLConstants.LOCK));
		}
		if(this.dynamic) {
			el.addContent(new XMLElement(QueryXMLConstants.DYNAMIC));
		}
		return el;
	}
	
	private void addPattern(String pattern, XMLElement el) {
		XMLElement xmlPattern = new XMLElement(QueryXMLConstants.PATTERN);
		xmlPattern.addContent(pattern);
		el.addContent(xmlPattern);
	}

		
		
	/**
	 * @return
	 */
	private String getEntityClassName() {
		return this.entity;
	}
	
	public String getPath()	{
		return this.path;
	}
	
	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	private IDOEntityDefinition getIDOEntityDefinition() throws IDOLookupException, ClassNotFoundException{
		if(this.entityDef==null){
			this.entityDef = IDOLookup.getEntityDefinitionForClass(RefactorClassRegistry.forName(getEntityClassName()));
		}
		return this.entityDef;
	}
	
	public IDOEntityField getIDOEntityField() throws IDOLookupException, ClassNotFoundException{
		if(this.idoField==null){
			IDOEntityDefinition def = getIDOEntityDefinition();
			if(def != null){
				IDOEntityField[] fields = def.getFields();
				for (int i = 0; i < fields.length; i++) {
					if(fields[i].getUniqueFieldName().equals(this.field)){
						this.idoField = fields[i];
						return this.idoField;
					}
				}
			}
		}
		return this.idoField;
	}

	public String getId()	{
		return this.id;
	}
	
	public void setIdUsingPrefix(int id) {
		this.id = StringHandler.concat(PREFIX,Integer.toString(id));
	}
		
	
	public int getIdNumber()	{
		return Integer.parseInt(this.id.substring(QueryConditionPart.PREFIX.length()));
	}
	
	/**
	 * @return
	 */
	public String getField() {
		return this.field;
	}

	public Collection getPatterns() {
		return this.patterns;
	}
	
	
	/**
	 * @return
	 */
	public String getPattern() {
		return this.pattern;
	}

	public String getDescription()	{
		return this.description;
	} 

	/**
	 * @return
	 */
	public String getType() {
		return this.type;
	}


	/**
	* @param string
	*/
	public void setEntity(String string) {
	  this.entity = string;
	}
	
	/**
	 * @param string
	 */
	public void setField(String string) {
		this.field = string;
	}

	/**
	 * @param string
	 */
	public void setPattern(String string) {
		this.pattern = string;
		this.patterns = null;
		this.patternField = null;
		this.patternPath =null;
	}
	
	/**
	 * @param patterns The patterns to set.
	 */
	public void setPatterns(Collection patterns) {
		this.patterns = patterns;
		this.pattern = null;
		this.patternField = null;
		this.patternPath = null;
	}

	public void setDescription(String description)	{
		this.description = description;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		this.type = string;
	}
	
	public String encode(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.id).append(';');
		buffer.append(this.entity).append(';');
		buffer.append(this.path).append(';');
		buffer.append(this.field).append(';');
		buffer.append(this.type).append(';');
		if (this.patterns != null) {
			Iterator iterator = this.patterns.iterator();
			while (iterator.hasNext()) {
				String singlePattern = (String) iterator.next();
				buffer.append(singlePattern).append(';');
			}
		}
		else {
			buffer.append(this.pattern).append(';');
		}
		buffer.append(this.description);
		return buffer.toString();
	}
	
//	public static QueryConditionPart decode(String encoded){
//		StringTokenizer toker = new StringTokenizer(encoded,";");
//		int tokenNumber = toker.countTokens();
//		String id = toker.nextToken();
//		String entity = toker.nextToken();
//		String path = toker.nextToken();
//		String field = toker.nextToken();
//		String type = toker.nextToken();
//		if (tokenNumber == 7) {
//			String pattern = toker.nextToken();
//			String description = toker.nextToken();
//			return new QueryConditionPart(id, entity, path, field, type, pattern, description);
//		}
//		else {
//			List patterns = new ArrayList();
//			int counter = 6;
//			while (toker.hasMoreTokens() && counter < tokenNumber) {
//				patterns.add(toker.nextToken());
//				counter++;
//			}
//			String description = toker.nextToken();
//			return new QueryConditionPart(id, entity, path, field, type, patterns, description);
//		}
//	}
	

	/* (non-Javadoc)
	* @see com.idega.block.dataquery.business.QueryPart#isLocked()
	*/
	public boolean isLocked() {
		return this.lock;
	}

	/* (non-Javadoc)
	 * @see com.idega.block.dataquery.business.QueryPart#setLocked(boolean)
	 */
	public void setLocked(boolean locked) {
		this.lock = locked;
	}

	public boolean hasMoreThanOnePattern() {
		return this.patterns !=  null;
	}

	/**
	 * @return
	 */
	public boolean isDynamic() {
		return this.dynamic;
	}

	/**
	 * @param b
	 */
	public void setDynamic(boolean b) {
		this.dynamic = b;
	}

	/**
	 * @return Returns the patternField.
	 */
	public String getPatternField() {
		return this.patternField;
	}

	/**
	 * @param patternField The patternField to set.
	 */
	public void setPatternField(String patternField) {
		this.patternField = patternField;
		this.pattern = null;
		this.patterns = null;
	}

	/**
	 * @return Returns the patternPath.
	 */
	public String getPatternPath() {
		return this.patternPath;
	}

	/**
	 * @param patternPath The patternPath to set.
	 */
	public void setPatternPath(String patternPath) {
		this.patternPath = patternPath;
		this.pattern = null;
		this.patterns = null;
	}

}
