/*
 * Created on May 21, 2003
 *
 * QueryBuilder is a wizard that constructs a ReportQuery from the user input.
 */
package com.idega.block.dataquery.presentation;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.idega.block.dataquery.business.QueryConditionPart;
import com.idega.block.dataquery.business.QueryEntityPart;
import com.idega.block.dataquery.business.QueryFieldPart;
import com.idega.block.dataquery.business.QueryHelper;
import com.idega.block.dataquery.business.QueryPart;
import com.idega.block.dataquery.business.QueryService;
import com.idega.block.dataquery.business.QuerySession;
import com.idega.block.dataquery.data.Query;
import com.idega.business.IBOLookup;
import com.idega.core.ICTreeNode;
import com.idega.core.IWTreeNode;
import com.idega.core.data.ICFile;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SelectionDoubleBox;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.TreeViewer;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author aron 
 * @version 1.0
 */
public class QueryBuilder extends Block {
	private IWBundle iwb = null;
	private IWResourceBundle iwrb = null;
	private static final String IWBUNDLE_IDENTIFIER = "com.idega.block.dataquery";
	private QueryHelper helper = null;
	private boolean hasPermission = false;
	private static final String PARAM_STEP = "step";
	private static final String PARAM_NEXT = "next";
	private static final String PARAM_LAST = "last";
	private static final String PARAM_SAVE = "save";
	private static final String PARAM_SOURCE = "source_entity";
	private static final String PARAM_RELATED = "related_entity";
	private static final String PARAM_FIELDS = "entity_fields";
	private static final String PARAM_CONDITION = "field_pattern";
	private int step = 1;
	private int queryFolderID = -1;
	private int relationDepth = 4;
	private int investigationLevel = 3;
	private boolean allowEmptyConditions = true;
	private QuerySession sessionBean;
	public void control(IWContext iwc) {
		if (hasPermission) {
			try {
				sessionBean = (QuerySession) IBOLookup.getSessionInstance(iwc, QuerySession.class);
				helper = sessionBean.getQueryHelper();
				// if not moving around we stay at entity tree
				if (iwc.isParameterSet("tree_action"))
					step = 2;
				else
					step =	iwc.isParameterSet(PARAM_STEP)? Integer.parseInt(iwc.getParameter(PARAM_STEP)): helper.getStep();
				step = step==0?1:step;
				System.err.println("helper step is " + helper.getStep());
				System.err.println("this step is before process" + step);
				processForm(iwc);
				System.err.println("this step is after process" + step);
				Table table = new Table();
				table.setWidth(getWidth());
				table.setBorder(2);
				table.setBorderColor("#FF0000");
				table.add(getStep(iwc), 1, 1);
				Form form = new Form();
				table.add(getLastAndNext(step), 1, 2);
				form.addParameter(PARAM_STEP, step);
				form.add(table);
				add(form);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			add(iwrb.getLocalizedString("no_permission", "You don't have permission !!"));
		}
	}
	private void processForm(IWContext iwc) throws ClassNotFoundException,IOException {
		if (iwc.isParameterSet(PARAM_NEXT)) {
			boolean proceed = processNextStep(iwc);
			if (proceed) {
				step++;
				System.out.println(" proceed to next step");
			}
			else
				System.out.println(" do not proceed to next step");
		}
		else if (iwc.isParameterSet(PARAM_LAST)){
			processPreviousStep(iwc);
		}
		else if(iwc.isParameterSet(PARAM_SAVE)){
			ICFile q  = sessionBean.storeQuery(queryFolderID);
			if(q!=null)
				add("Query was saved "+q.getPrimaryKey().toString()  );
			else
				add("Query was not saved");
		}
	}
	private boolean processNextStep(IWContext iwc) throws ClassNotFoundException {
		int currentStep = iwc.isParameterSet(PARAM_STEP) ? Integer.parseInt(iwc.getParameter(PARAM_STEP)) : 1;
		System.out.println("current processing step " + currentStep);
		switch (currentStep) {
			case 1 :
				if (iwc.isParameterSet(PARAM_SOURCE)) {
					String sourceEntity = iwc.getParameter(PARAM_SOURCE);
					if (sourceEntity.length() > 0 && !sourceEntity.equalsIgnoreCase("empty")) {
						QueryEntityPart part = QueryEntityPart.decode(sourceEntity);
						helper.setSourceEntity(part);
					}
					return helper.hasSourceEntity();
				}
				break;
			case 2 :
				helper.clearRelatedEntities();
				if (iwc.isParameterSet(PARAM_RELATED)) {
					String[] entities = iwc.getParameterValues(PARAM_RELATED);
					for (int i = 0; i < entities.length; i++) {
						QueryEntityPart part = QueryEntityPart.decode(entities[i]);
						if (part != null)
							helper.addRelatedEntity(part);
					}
					return helper.hasRelatedEntities();
				}
				// if we allow to  work with source entity fields alone
				else
					return helper.hasSourceEntity();
			case 3 :
				helper.clearFields();
				String[] fields =null;
				if (iwc.isParameterSet(PARAM_FIELDS)) {
					fields = iwc.getParameterValues(PARAM_FIELDS);
				}
				// allow to select from the left box only ( no ordering ), shortcut !
				else if(iwc.isParameterSet(PARAM_FIELDS+"_left")){
					fields = iwc.getParameterValues(PARAM_FIELDS+"_left");
				}
				if(fields!=null){
					for (int i = 0; i < fields.length; i++) {
						QueryFieldPart part = QueryFieldPart.decode(fields[i]);
						if (part != null)
							helper.addField(part);
					}
					return helper.hasFields();
				}
				break;
				case 4:
				helper.clearConditions();
				String[] conditions = iwc.getParameterValues(PARAM_CONDITION);;
				if (conditions!=null) {
					List listOfFields  = helper.getListOfFields();
					if(listOfFields.size() == conditions.length){
						conditions = iwc.getParameterValues(PARAM_CONDITION);
						for (int i = 0; i < conditions.length; i++) {
							if(conditions[i].length()>0){
								QueryFieldPart fieldPart = (QueryFieldPart) listOfFields.get(i);
								QueryConditionPart part = new QueryConditionPart(fieldPart.getName(),fieldPart.getTypeClass(),conditions[i]);
								helper.addCondition(part);
							}
						}						
					}
					else{
						System.out.println("field count and condition count dont match !");
						return false;
					}
				}
				return helper.hasConditions() || this.allowEmptyConditions;
		}
		return false;
	}
	private void processPreviousStep(IWContext iwc) {
		switch (step) {
			case 2 :
				//helper.clearRelatedEntities();
				//helper.clearSourceEntity(); 
				break;
			case 3 :
				//helper.clearRelatedEntities();
				break;
			case 4 :
				//helper.clearFields();
				break;
			case 5 :
				//helper.clearConditions();
				break;
		}
		step--;
		//step = helper.getStep()-1;
	}
	public PresentationObject getStep(IWContext iwc) throws ClassNotFoundException, RemoteException {
		switch (step) {
			case 1 :
				return getStep1(iwc);
			case 2 :
				return getStep2(iwc);
			case 3 :
				return getStep3(iwc);
			case 4 :
				return getStep4(iwc);
			case 5 :
				return getStep5(iwc);
			default :
				return getStep1(iwc);
		}
	}
	public PresentationObject getStep1(IWContext iwc) throws RemoteException {
		Table table = new Table(1, 5);
		table.setWidth(table.HUNDRED_PERCENT);
		table.add(iwrb.getLocalizedString("step_1_msg", "Choose a source entity"), 1, 1);
		DropdownMenu drp = new DropdownMenu(PARAM_SOURCE);
		//TODO get available source entities !!!
		Collection sourceEntities = getQueryService(iwc).getSourceQueryEntityParts();
		drp.addDisabledMenuElement("empty", "Entity");
		Iterator iter = sourceEntities.iterator();
		while (iter.hasNext()) {
			QueryEntityPart element = (QueryEntityPart) iter.next();
			drp.addMenuElement(element.encode(), iwrb.getLocalizedString(element.getName(), element.getName()));
		}
		if (helper.hasSourceEntity()) {
			QueryEntityPart source = helper.getSourceEntity();
			drp.setSelectedElement(source.encode());
		}
		table.add(drp, 1, 3);
		return table;
	}
	public PresentationObject getStep2(IWContext iwc) throws ClassNotFoundException, RemoteException {
		Table table = new Table(1, 5);
		table.setWidth(table.HUNDRED_PERCENT);
		table.add(iwrb.getLocalizedString("step_2_msg", "Choose related entities"), 1, 1);
		//table.add(getRelatedChoice(iwc),1,2);
		IWTreeNode root = getQueryService(iwc).getEntityTree(helper, investigationLevel);
		EntityChooserTree tree = new EntityChooserTree(root, iwc);
		tree.setUI(tree._UI_WIN);
		Link treeLink = new Link();
		treeLink.addParameter(PARAM_STEP,step);
		tree.setLinkOpenClosePrototype(treeLink);
		tree.addOpenCloseParameter(PARAM_STEP,"2");
		//tree.setToMaintainParameter(PARAM_STEP,iwc);
		//viewer.setNestLevelAtOpen(4);
		table.add(tree, 1, investigationLevel);
		return table;
	}
	public PresentationObject getStep3(IWContext iwc) throws RemoteException {
		QueryService service = getQueryService(iwc);
		Table table = new Table();
		table.setWidth(table.HUNDRED_PERCENT);
		table.mergeCells(1, 1, 2, 1);
		table.add(iwrb.getLocalizedString("step_3_msg", "Choose entity fields and order "), 1, 1);
		int row = 2;
		table.add(iwrb.getLocalizedString("entity_field", "Entity field"), 2, row++);
		QueryEntityPart entityPart = helper.getSourceEntity();
		List entities = helper.getListOfRelatedEntities();
		if (entities == null)
			entities = new Vector();
		Iterator iterator = entities.iterator();
		List listOfFields = helper.getListOfFields();
		if (listOfFields == null)
			listOfFields = new Vector();
		Map fieldMap = getQueryPartMap(listOfFields);
		SelectionDoubleBox box =new SelectionDoubleBox(PARAM_FIELDS+"_left",PARAM_FIELDS);
		box.setLeftLabel(iwrb.getLocalizedString("available_fields", "Available fields"));
		box.setRightLabel(iwrb.getLocalizedString("chosen_fields", "Chosen fields"));
		box.getRightBox().addUpAndDownMovers();
		box.getRightBox().setWidth("300");
		box.getLeftBox().setWidth("300");
		box.getRightBox().setHeight("20");
		box.getLeftBox().setHeight("20");
		box.getRightBox().selectAllOnSubmit();
		fillFieldSelectionBox(service,entityPart,fieldMap,box);
		while (iterator.hasNext()) {
			entityPart = (QueryEntityPart) iterator.next();
			fillFieldSelectionBox(service,entityPart,fieldMap,box);
		}
		table.add(box, 2, row);
		return table;
	}
	private void fillFieldSelectionBox(QueryService service,QueryEntityPart entityPart, Map fieldMap,SelectionDoubleBox box)throws RemoteException {
		//System.out.println("filling box with fields from " + entityPart.getName());
		Iterator iter = service.getListOfFieldParts(iwrb, entityPart).iterator();
		while (iter.hasNext()) {
			QueryFieldPart part = (QueryFieldPart) iter.next();
			//System.out.println(" " + part.getName());
			if (fieldMap.containsKey(part.encode())) {
				box.getRightBox().addElement(
					part.encode(),
					iwrb.getLocalizedString(entityPart.getName(), entityPart.getName()) + " -> " + part.getDisplay());
			}
			else {
				box.getLeftBox().addElement(
					part.encode(),
					iwrb.getLocalizedString(entityPart.getName(), entityPart.getName()) + " -> " + part.getDisplay());
			}
		}
	}
	
	public PresentationObject getStep4(IWContext iwc) {
		Table table = new Table();
		table.setWidth(table.HUNDRED_PERCENT);
		
		table.add(iwrb.getLocalizedString("step_4_msg", "Define field conditions"), 1, 1);
		table.mergeCells(1, 1, 5, 1);
		int row = 2;
		table.add(iwrb.getLocalizedString("field_display", "Display"), 2, row);
		table.add(iwrb.getLocalizedString("field_entity", "Entity"), 3, row);
		table.add(iwrb.getLocalizedString("field_type", "Data type"), 4, row);
		table.add(iwrb.getLocalizedString("field_pattern", "Pattern"), 5, row);
		row++;
		Iterator iter = helper.getListOfFields().iterator();
		while (iter.hasNext()) {
			QueryFieldPart part = (QueryFieldPart) iter.next();
			table.add(part.getDisplay(), 2, row);
			table.add(iwrb.getLocalizedString(part.getEntity(), part.getEntity()), 3, row);
			table.add(iwrb.getLocalizedString(part.getTypeClass(), part.getTypeClass()), 4, row);
			table.add(new TextInput(PARAM_CONDITION), 5, row);
			row++;
		}
		return table;
	}
	
	public PresentationObject getStep5(IWContext iwc) {
			Table table = new Table();
			table.setWidth(table.HUNDRED_PERCENT);
			table.add(iwrb.getLocalizedString("step_5_msg", "Take proper action"), 1, 1);
			
			return table;
	}
	
	private Table getLastAndNext(int currentStep) {
		Table T = new Table(2, 1);
		T.setWidth(getWidth());
		T.setAlignment(1, 1, T.HORIZONTAL_ALIGN_RIGHT);
		T.setAlignment(2, 1, T.HORIZONTAL_ALIGN_LEFT);
		//T.setAlignment(T.HORIZONTAL_ALIGN_CENTER);
		if (currentStep <= 4) {
			SubmitButton next =
				new SubmitButton(iwrb.getLocalizedImageButton("btn_next", "next >>"), PARAM_NEXT, "true");
			T.add(next, 2, 1);
		}
		if (currentStep > 1) {
			SubmitButton last =
				new SubmitButton(iwrb.getLocalizedImageButton("btn_previous", "<< previous"), PARAM_LAST, "true");
			T.add(last, 1, 1);
		}
		if(currentStep > 4){
			SubmitButton save = new SubmitButton(iwrb.getLocalizedImageButton("btn_save","Save"),PARAM_SAVE,"true");
			T.add(save, 2, 1);
		}
		return T;
	}
	private PresentationObject getRelatedChoice(IWContext iwc) throws ClassNotFoundException, RemoteException {
		Table T = new Table();
		T.setWidth(T.HUNDRED_PERCENT);
		Collection coll = getQueryService(iwc).getRelatedQueryEntityParts(helper.getSourceEntity(), relationDepth);
		Iterator iter = coll.iterator();
		int row = 1;
		CheckBox checkAll = new CheckBox("checkall");
		checkAll.setToCheckOnClick(PARAM_RELATED, "this.checked");
		T.add(checkAll, 1, row);
		T.add(iwrb.getLocalizedString("entity_name", "Entity name"), 2, row++);
		Map entityMap = getEntityMap(helper.getListOfRelatedEntities());
		while (iter.hasNext()) {
			QueryEntityPart entityPart = (QueryEntityPart) iter.next();
			CheckBox checkBox = new CheckBox(PARAM_RELATED, entityPart.encode());
			checkBox.setChecked(entityMap.containsKey(entityPart.getName()));
			T.add(checkBox, 1, row);
			T.add(
				iwrb.getLocalizedString(entityPart.getName(), entityPart.getName())
					+ " "
					+ entityPart.getBeanClassName(),
				2,
				row);
			row++;
		}
		return T;
	}
	public void main(IWContext iwc) throws Exception {
		debugParameters(iwc);
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		hasPermission = hasEditPermission();
		control(iwc);
	}
	public QueryService getQueryService(IWContext iwc) throws RemoteException {
		return (QueryService) IBOLookup.getServiceInstance(iwc, QueryService.class);
	}
	public String getBundleIdentifier() {
		return "com.idega.block.dataquery";
	}
	public Map getQueryPartMap(List listOfQueryParts) {
		int size = 0;
		if (listOfQueryParts != null)
			size = listOfQueryParts.size();
		Map map = new HashMap(size);
		if (listOfQueryParts != null) {
			Iterator iter = listOfQueryParts.iterator();
			while (iter.hasNext()) {
				QueryPart element = (QueryPart) iter.next();
				map.put(element.encode(), element);
				System.out.println("putting into map : " + element.encode());
			}
		}
		return map;
	}
	public Map getEntityMap(List listOfEntityParts) {
		int size = 0;
		if (listOfEntityParts != null)
			size = listOfEntityParts.size();
		Map map = new HashMap(size);
		if (listOfEntityParts != null) {
			Iterator iter = listOfEntityParts.iterator();
			while (iter.hasNext()) {
				QueryEntityPart element = (QueryEntityPart) iter.next();
				if (element.getPath() != null)
					map.put(element.getPath(), element);
				else
					map.put(element.getName(), element);
			}
		}
		return map;
	}
	public class EntityChooserTree extends TreeViewer {
		/**
		 * 
		 */
		Map entityMap = null;
		public EntityChooserTree() {
			super();
			setParallelExtraColumns(2);
			entityMap = getEntityMap(helper.getListOfRelatedEntities());
		}
		public EntityChooserTree(ICTreeNode node, IWContext iwc) {
			this();
			setRootNode(node);
		}
		/* (non-Javadoc)
		 * @see com.idega.presentation.ui.AbstractTreeViewer#getObjectToAddToParallelExtraColumn(int, com.idega.core.ICTreeNode, com.idega.presentation.IWContext, boolean, boolean, boolean)
		 */
		public PresentationObject getObjectToAddToParallelExtraColumn(
			int colIndex,
			ICTreeNode node,
			IWContext iwc,
			boolean nodesOpen,
			boolean nodeHasChild,
			boolean isRootNode) {
			QueryEntityPart entityNode = (QueryEntityPart) node;
			if (entityNode != null) {
				if (entityNode.getParentNode() != null) {
					switch (colIndex) {
						case 1 :
							return new Text(entityNode.getBeanClassName());
						case 2 :
							CheckBox checkBox = new CheckBox(PARAM_RELATED, entityNode.encode());
							if (entityNode.getPath() != null) {
								checkBox.setChecked(entityMap.containsKey(entityNode.getPath()));
								//System.out.println("using path " + entityNode.getPath());
							}
							else {
								checkBox.setChecked(entityMap.containsKey(entityNode.getName()));
								//System.out.println("using name " + entityNode.getName());
							}
							return checkBox;
					}
				}
			}
			return null;
		}
		/* (non-Javadoc)
		 * @see com.idega.presentation.ui.TreeViewer#getSecondColumnObject(com.idega.core.ICTreeNode, com.idega.presentation.IWContext, boolean)
		 */
		public PresentationObject getSecondColumnObject(ICTreeNode node, IWContext iwc, boolean fromEditor) {
			String nodeName = iwrb.getLocalizedString(node.getNodeName(), node.getNodeName());
			getShortenedNodeName(nodeName);
			return new Text(nodeName);
		}
	}
	/**
	 * @param i
	 */
	public void setInvestigationLevel(int i) {
		investigationLevel = i;
	}

}
