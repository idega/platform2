/*
 * $Id:$
 *
 * Copyright (C) 2002 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.help.presentation;

import com.idega.block.help.data.HelpNode;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.ui.IFrame;
import com.idega.presentation.ui.TreeViewer;

/**
 * This class does something very clever.....
 * 
 * @author <a href="palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class HelpTree extends Block {
	public static final String HELP_KEY = Help.HELP_KEY;
	public static final String HELP_BUNDLE = Help.HELP_BUNDLE;
	
	private static final String BUNDLE_IDENTIFIER = "se.idega.idegaweb.commune";
	
	private static final String HELP_FRAME = "hlp_tree_frame";

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
		TreeViewer tree = TreeViewer.getTreeViewerInstance(node,iwc);
		tree.setDefaultOpenLevel(999);
		Link link = new Link();
		link.setURL("#");
		link.setNoTextObject(true);
		link.setClassToInstanciate(DisplayHelp.class);
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
		 * @todo Read this from xml
		 */
		
		HelpNode top = new HelpNode(1,"se.idega.idegaweb.commune","Hjälp - Medborgare");
		HelpNode child1 = new HelpNode(2,"se.idega.idegaweb.commune","Ansökan om medborgarkonto");
		HelpNode child2 = new HelpNode(3,"se.idega.idegaweb.commune","Medborgarkonto - för dig som inte bor i Nacka");
		HelpNode child3 = new HelpNode(4,"se.idega.idegaweb.commune","Min sida");
		HelpNode child4 = new HelpNode(5,"se.idega.idegaweb.commune","Mina inställningar");
		HelpNode child5 = new HelpNode(6,"se.idega.idegaweb.commune","Skolval - att välja skola");
		HelpNode child6 = new HelpNode(7,"se.idega.idegaweb.commune","Byte av skola");
		HelpNode child7 = new HelpNode(8,"se.idega.idegaweb.commune","Val av skolbarnsomsorg med barnomsorgscheck");
		HelpNode child8 = new HelpNode(9,"se.idega.idegaweb.commune","Ansökan om barnomsorgscheck");
		HelpNode child9 = new HelpNode(10,"se.idega.idegaweb.commune","Lämna synpunkter");
		HelpNode child10 = new HelpNode(11,"se.idega.idegaweb.commune","Politiskt ansvar");
		HelpNode child11 = new HelpNode(12,"se.idega.idegaweb.commune","Ditt val");
		HelpNode child12 = new HelpNode(13,"se.idega.idegaweb.commune","Sök på Nacka24");
		HelpNode child13 = new HelpNode(14,"se.idega.idegaweb.commune","Till nacka.se");
		
		HelpNode subchild1 = new HelpNode(15,"se.idega.idegaweb.commune","Mina meddelanden");
		HelpNode subchild2 = new HelpNode(16,"se.idega.idegaweb.commune","Mina ärenden");

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
		top.addChild(child12);
		top.addChild(child13);
		
		child3.addChild(subchild1);
		child3.addChild(subchild2);

		return top;			
	}
}
