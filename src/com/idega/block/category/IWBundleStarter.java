package com.idega.block.category;

import com.idega.block.category.presentation.CategoryBlock;
import com.idega.block.category.presentation.CategoryMetaDataWindow;
import com.idega.block.category.presentation.CategoryWindow;
import com.idega.block.category.presentation.FolderBlock;
import com.idega.block.category.presentation.FolderBlockCategoryWindow;
import com.idega.block.category.presentation.FolderBlockComponent;
import com.idega.block.category.presentation.FolderBlockComponentIWAdminWindowLegacy;
import com.idega.block.category.presentation.FolderBlockFolderEditor;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.repository.data.RefactorClassRegistry;

/**
 * <p>Title: idegaWeb</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: idega Software</p>
 * @author <a href="thomas@idega.is">Thomas Hilbig</a>
 * @version 1.0
 * Created on Jun 10, 2004
 */
public class IWBundleStarter implements IWBundleStartable {

	public void start(IWBundle starterBundle) {
		
		// refactoring
		RefactorClassRegistry registry =  RefactorClassRegistry.getInstance();
		registry.registerRefactoredClass("com.idega.block.presentation.CategoryBlock", CategoryBlock.class);
		registry.registerRefactoredClass("com.idega.block.presentation.CategoryMetaDataWindow", CategoryMetaDataWindow.class);
		registry.registerRefactoredClass("com.idega.block.presentation.CategoryWindow", CategoryWindow.class);
		
		registry.registerRefactoredClass("com.idega.idegaweb.block.presentation.FolderBlock", FolderBlock.class);
		registry.registerRefactoredClass("com.idega.idegaweb.block.presentation.FolderBlockCategoryWindow", FolderBlockCategoryWindow.class);
		registry.registerRefactoredClass("com.idega.idegaweb.block.presentation.FolderBlockComponent", FolderBlockComponent.class);
		registry.registerRefactoredClass("com.idega.idegaweb.block.presentation.FolderBlockComponentIWAdminWindowLegacy", FolderBlockComponentIWAdminWindowLegacy.class);
		registry.registerRefactoredClass("com.idega.idegaweb.block.presentation.FolderBlockFolderEditor", FolderBlockFolderEditor.class);
	}
	
	public void stop(IWBundle starterBundle) {
		// nothing to do
	}
}
