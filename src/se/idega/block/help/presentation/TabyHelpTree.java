/*
 * $Id: TabyHelpTree.java,v 1.6 2004/01/22 23:06:02 tryggvil Exp $
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package se.idega.block.help.presentation;

import com.idega.block.help.data.HelpNode;
import com.idega.block.help.presentation.DisplayHelp;
import com.idega.block.help.presentation.HelpTreeViewer;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.IFrame;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class TabyHelpTree extends Block {
	public static final String HELP_KEY = Help.HELP_KEY;
	public static final String HELP_BUNDLE = Help.HELP_BUNDLE;
	
	public static final String TITLE_STYLE = "hlp_ttl_sty";
	public static final String TITLE_CLASS = "hlp_ttl_cla";
	public static final String BODY_STYLE = "hlp_bdy_sty";
	public static final String BODY_CLASS = "hlp_bdy_cla";
	
	private static final String BUNDLE_IDENTIFIER = "se.idega.block.help";
	
	private static final String HELP_FRAME = "hlp_tree_frame";

	protected String _titleStyleAttribute = null;
	protected String _titleStyleClass = null;
	protected String _bodyStyleAttribute = null;
	protected String _bodyStyleClass = null;
	protected String _linkStyleAttribute = null;
	protected String _linkStyleClass = null;
	protected String _seeAlsoStyleAttribute = null;
	protected String _seeAlsoStyleClass = null;

	public void main(IWContext iwc) throws Exception {
		Table t = new Table(2,1);
		t.setHeight("100%");
		t.setWidth("100%");
		t.setWidth(1, "170");
		t.setCellpadding(3);
		t.setCellspacing(0);
		t.setAlignment(1, 1, "center");
		t.setVerticalAlignment(1, 1, "top");
		t.setVerticalAlignment(2, 1, "top");
		
		IFrame f = new IFrame(HELP_FRAME,DisplayHelp.class);
		f.setStyleAttribute("width:100%;height:100%");
		f.setScrolling(IFrame.SCROLLING_YES);

		HelpNode node = loadTree(iwc);		
		HelpTreeViewer tree = (HelpTreeViewer)HelpTreeViewer.getTreeViewerInstance(node,iwc);
		tree.setDefaultOpenLevel(1);
		tree.setWrap(true);
		tree.setMaxNodeNameLength(20);
		Link link = new Link();
		link.setURL("#");
		link.setNoTextObject(true);
		link.setClassToInstanciate(DisplayHelp.class);
		if (_titleStyleAttribute != null)
			link.addParameter(TITLE_STYLE,_titleStyleAttribute);
		if (_titleStyleClass != null)
			link.addParameter(TITLE_CLASS,_titleStyleClass);
		if (_bodyStyleAttribute != null)
			link.addParameter(BODY_STYLE,_bodyStyleAttribute);
		if (_bodyStyleClass != null)
			link.addParameter(BODY_CLASS,_bodyStyleClass);
		tree.setLinkPrototype(link);
		
		tree.setTarget(HELP_FRAME);
		tree.setNodeActionParameter(HELP_KEY);
				
		add(t);
		
		t.add(tree,1,1);
		t.add(f,2,1);
	}

	public String getBundleIdentifier() {
		return BUNDLE_IDENTIFIER;
	}
	
	private HelpNode loadTree(IWContext iwc) {
		/**
		 * @todo Read this from xml and remove the fucking hardcoding.
		 */
		HelpNode top = null;
		HelpNode child1 = null;
		HelpNode child2 = null;
		HelpNode child3 = null;
		HelpNode child4 = null;
		HelpNode child5 = null;
		HelpNode child6 = null;
		HelpNode child7 = null;
		HelpNode child8 = null;
		HelpNode child9 = null;
		HelpNode child10 = null;
		HelpNode child11 = null;
		HelpNode child12 = null;
		HelpNode child13 = null;
		//HelpNode child14 = null;
		//HelpNode child15 = null;

		/*HelpNode child16 = null;
		HelpNode child17 = null;
		HelpNode child18 = null;
*/

		//HelpNode citizen = null;
		//HelpNode schoolChoice = null;
		//HelpNode other = null;

		if (iwc.getCurrentLocale().getLanguage().equals("en")) {
			top = new HelpNode(1,"se.idega.idegaweb.commune","Help - Citizens");
			child1 = new HelpNode(2,"se.idega.idegaweb.commune","Application for citizen account");
			child2 = new HelpNode(3,"se.idega.idegaweb.commune","Citizen account - for you who do not live in Täby");
			child3 = new HelpNode(4,"se.idega.idegaweb.commune","My page");
			child4 = new HelpNode(5,"se.idega.idegaweb.commune","My settings");
			child5 = new HelpNode(6,"se.idega.idegaweb.commune","School selection - to choose school");
			child6 = new HelpNode(7,"se.idega.idegaweb.commune","School selection - placing message");
			child7 = new HelpNode(8,"se.idega.idegaweb.commune","Selection of after school care");
			child8 = new HelpNode(9,"se.idega.idegaweb.commune","Application for after school care check");
			child9 = new HelpNode(10,"se.idega.idegaweb.commune","Change your personal settings");
			child10 = new HelpNode(11,"se.idega.idegaweb.commune","Search");
			child11 = new HelpNode(12,"se.idega.idegaweb.commune","To taby.se");
			child12 = new HelpNode(13,"se.idega.idegaweb.commune","My messages");
			child13 = new HelpNode(14,"se.idega.idegaweb.commune","My errands");
			
			/*citizen = new HelpNode(15,"se.idega.idegaweb.commune","Citizen account");
			schoolChoice = new HelpNode(16,"se.idega.idegaweb.commune","School selection");
			other = new HelpNode(17,"se.idega.idegaweb.commune","Other");
			*/
			top.addChild(child1);
			top.addChild(child2);
			top.addChild(child3);
			top.addChild(child4);
			top.addChild(child5);			
			top.addChild(child6);
			top.addChild(child7);
			top.addChild(child8);
			top.addChild(child9);
			top.addChild(child10);
			top.addChild(child11);
			
			child3.addChild(child12);
			child3.addChild(child13);

			
			/*schoolChoice.addChild(child5);
			schoolChoice.addChild(child6);
			schoolChoice.addChild(child7);
			schoolChoice.addChild(child8);
			schoolChoice.addChild(child9);
			
			other.addChild(child10);
			other.addChild(child11);
			other.addChild(child12);
			other.addChild(child13);
			
			top.addChild(citizen);
			top.addChild(schoolChoice);
			top.addChild(other);
			*/
		}
		else {
			HelpNode citizenTop = new HelpNode(1,"se.idega.idegaweb.commune","Hjälp - Medborgare");
			child1 = new HelpNode(2,"se.idega.idegaweb.commune","Ansökan om medborgarkonto");
			child2 = new HelpNode(3,"se.idega.idegaweb.commune","Medborgarkonto - för dig som inte bor i Täby");
			child3 = new HelpNode(4,"se.idega.idegaweb.commune","Min sida");
			child4 = new HelpNode(5,"se.idega.idegaweb.commune","Mina inställningar");
			child5 = new HelpNode(6,"se.idega.idegaweb.commune","Skolval - att välja skola");
			child6 = new HelpNode(7,"se.idega.idegaweb.commune","Skolval - platsmeddelande");
			child7 = new HelpNode(8,"se.idega.idegaweb.commune","Val av skolbarnsomsorg");
			child8 = new HelpNode(9,"se.idega.idegaweb.commune","Ansökan om fritidshemspeng");
			child9 = new HelpNode(10,"se.idega.idegaweb.commune","Ändra dina personliga uppgifter");
			child10 = new HelpNode(11,"se.idega.idegaweb.commune","Sök");
			child11 = new HelpNode(12,"se.idega.idegaweb.commune","Till taby.se");
			child12 = new HelpNode(13,"se.idega.idegaweb.commune","Mina meddelanden");
			child13 = new HelpNode(14,"se.idega.idegaweb.commune","Mina ärenden");		

			citizenTop.addChild(child1);
			citizenTop.addChild(child2);
			citizenTop.addChild(child3);
			citizenTop.addChild(child4);
			citizenTop.addChild(child5);			
			citizenTop.addChild(child6);
			citizenTop.addChild(child7);
			citizenTop.addChild(child8);
			citizenTop.addChild(child9);
			citizenTop.addChild(child10);
			citizenTop.addChild(child11);
				
			
			child3.addChild(child12);
			child3.addChild(child13);

//			citizen = new HelpNode(17,"se.idega.idegaweb.commune","Medborgarkonto");
//			schoolChoice = new HelpNode(18,"se.idega.idegaweb.commune","Skolval");
//			other = new HelpNode(19,"se.idega.idegaweb.commune","Övrig");

//			citizen.addChild(child1);
//			citizen.addChild(child2);
//			citizen.addChild(child3);
//			citizen.addChild(child14);
//			citizen.addChild(child15);
//			citizen.addChild(child4);
//			
//			schoolChoice.addChild(child5);
//			schoolChoice.addChild(child6);
//			schoolChoice.addChild(child7);
//			schoolChoice.addChild(child8);
//			schoolChoice.addChild(child9);
//			
//			other.addChild(child10);
//			other.addChild(child11);
//			other.addChild(child12);
//			other.addChild(child13);
//
//			citizenTop.addChild(citizen);
//			citizenTop.addChild(schoolChoice);
//			citizenTop.addChild(other);
			
			return citizenTop;
			
//			top = new HelpNode(20,"se.idega.idegaweb.commune","Hjälp");
//			top.addChild(citizenTop);
			
//			HelpNode adminTop = new HelpNode(21,"se.idega.idegaweb.commune","Hjälp - Anordnare");
//			HelpNode adminMyPage = new HelpNode(22,"se.idega.idegaweb.commune","Min sida");
//			HelpNode adminSchoolGroup = new HelpNode(23,"se.idega.idegaweb.commune","Administrera skolgruppe");
//			HelpNode adminSchoolChoice = new HelpNode(24,"se.idega.idegaweb.commune","Administrera skolval");
//			HelpNode adminStudents = new HelpNode(25,"se.idega.idegaweb.commune","Elevöversikt");
//			HelpNode adminLeasureTime = new HelpNode(26,"se.idega.idegaweb.commune","Fritidslista");
//			
//			adminTop.addChild(adminMyPage);
//			adminTop.addChild(adminSchoolGroup);
//			adminTop.addChild(adminSchoolChoice);
//			adminTop.addChild(adminStudents);
//			adminTop.addChild(adminLeasureTime);
//			
//			top.addChild(adminTop);
//
//			HelpNode bunTop = new HelpNode(27,"se.idega.idegaweb.commune","Hjälp - BUN-administratör");
//			HelpNode bunMyPage = new HelpNode(28,"se.idega.idegaweb.commune","Min sida");
//			HelpNode bunChecks = new HelpNode(29,"se.idega.idegaweb.commune","Checkhantering");
//			HelpNode bunCitizen = new HelpNode(30,"se.idega.idegaweb.commune","Medborgarkonto");
//			HelpNode bunStatistics = new HelpNode(31,"se.idega.idegaweb.commune","Statistik skolval");
//			HelpNode bunReminder = new HelpNode(32,"se.idega.idegaweb.commune","Påminnelse skolval");
//			
//			bunTop.addChild(bunMyPage);
//			bunTop.addChild(bunChecks);
//			bunTop.addChild(bunCitizen);
//			bunTop.addChild(bunStatistics);
//			bunTop.addChild(bunReminder);
//
//			top.addChild(bunTop);
		}
		
		return top;			
	}
	
	public void setTitleStyleAttribute(String styleAttribute) {
		_titleStyleAttribute = styleAttribute;
	}
	
	public void setTitleStyleClass(String styleClass) {
		_titleStyleClass = styleClass;	
	}
	
	public void setBodyStyleAttribute(String styleAttribute) {
		_bodyStyleAttribute = styleAttribute;		
	}
	
	public void setBodyStyleClass(String styleClass) {
		_bodyStyleClass = styleClass;			
	}
	
	public void setLinkStyleAttribute(String styleAttribute) {
		_linkStyleAttribute = styleAttribute;
	}
	
	public void setLinkStyleClass(String styleClass) {
		_linkStyleClass = styleClass;			
	}
	
	public void setSeeAlsoStyleAttribute(String styleAttribute) {
		_seeAlsoStyleAttribute = styleAttribute;		
	}
	
	public void setSeeAlsoStyleClass(String styleClass) {
		_seeAlsoStyleClass = styleClass;			
	}		
}