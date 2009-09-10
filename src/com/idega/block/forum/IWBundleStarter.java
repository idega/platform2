package com.idega.block.forum;

import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWBundleStartable;
import com.idega.idegaweb.include.GlobalIncludeManager;

public class IWBundleStarter implements IWBundleStartable {

    private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.forum";

    public void start(IWBundle arg0) {      
        GlobalIncludeManager includeManager = GlobalIncludeManager.getInstance();
        includeManager.addBundleStyleSheet(IW_BUNDLE_IDENTIFIER, "/style/forum.css");
    }

    public void stop(IWBundle arg0) {}
}
