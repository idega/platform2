package com.idega.block.albumcollection.presentation;

import com.idega.presentation.Block;
import com.idega.presentation.IWContext;

/**
 * Title:        idegaWeb
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="gummi@idega.is">Gu�mundur �g�st S�mundsson</a>
 * @version 1.0
 */

public class AlbumCollection extends Block {

  protected final static String IW_BUNDLE_IDENTIFIER="com.idega.block.albumcollection";

  public static final String _PRM_STATE = "ac_presentation_state_prm";
  public static final int _STATE_ALBUMLIST = 0;
  public static final int _STATE_ALBUMINFO = 1;
  //public static final int _STATE_TRACKINFO = 2;
  public static final int _STATE_LYRIC = 3;
  public static final int _STATE_LYRICLIST = 4;

  public static final String _COLOR_DARKEST = "#000000";
  public static final String _COLOR_DARKER = "#333333";
  //public static final String _COLOR_DARK = "#666666";
  public static final String _COLOR_DARK = "#727272";
  //public static final String _COLOR_BRIGHT = "#777777";
  public static final String _COLOR_BRIGHT = "#828282";
  public static final String _COLOR_BRIGHTER = "#AAAAAA";
  public static final String _COLOR_BRIGHTEST = "#FFFFFF";

  private int initialState = _STATE_ALBUMLIST;

  public AlbumCollection() {

  }

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void setInitialState(int state){
    this.initialState = state;
  }

  public void lineUpAlbumListState(IWContext iwc) throws Exception{
    AlbumList l = new AlbumList();
    l.setICObjectID(this.getICObjectID());
    l.setICObjectInstanceID(this.getICObjectInstanceID());
    this.add(l);
  }

  public void lineUpAlbumInfoState(IWContext iwc) throws Exception{
    AlbumDetails d = new AlbumDetails();
    d.setICObjectID(this.getICObjectID());
    d.setICObjectInstanceID(this.getICObjectInstanceID());
    this.add(d);
  }

  public void lineUpLyricListState(IWContext iwc) throws Exception{
    LyricList l = new LyricList();
    l.setICObjectID(this.getICObjectID());
    l.setICObjectInstanceID(this.getICObjectInstanceID());
    this.add(l);
  }

  public void lineUpLyricState(IWContext iwc) throws Exception{
    LyricViewer l = new LyricViewer();
    l.setICObjectID(this.getICObjectID());
    l.setICObjectInstanceID(this.getICObjectInstanceID());
    this.add(l);
  }


  public void main(IWContext iwc) throws Exception {
    String state = iwc.getParameter(_PRM_STATE);
    int nextState;
    if(state != null){
      nextState = Integer.parseInt(state);
    } else {
      nextState = this.initialState;
    }

    switch (nextState) {
      case _STATE_ALBUMLIST:
        lineUpAlbumListState(iwc);
        break;
      case _STATE_ALBUMINFO:
        lineUpAlbumInfoState(iwc);
        break;
      case _STATE_LYRICLIST:
        lineUpLyricListState(iwc);
        break;
      case _STATE_LYRIC:
        lineUpLyricState(iwc);
        break;
      default:
        IllegalStateException e = new IllegalStateException("ac_presentation_state not recognized");
        e.printStackTrace();
    }


  }


}
