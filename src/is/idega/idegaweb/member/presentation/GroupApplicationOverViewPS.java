package is.idega.idegaweb.member.presentation;

import com.idega.user.event.*;
import com.idega.core.builder.data.ICDomain;
import com.idega.presentation.event.ResetPresentationEvent;
import com.idega.user.data.Group;
import com.idega.presentation.event.TreeViewerEvent;
import com.idega.idegaweb.browser.event.IWBrowseEvent;
import java.util.Iterator;
import java.util.List;
import com.idega.presentation.Page;
import javax.swing.event.ChangeListener;
import com.idega.idegaweb.IWException;
import com.idega.event.*;


/**
 * @author <a href="mailto:eiki@idega.is">Eirikur Hrafnsson</a>
 * @version 1.0
 */


public class GroupApplicationOverViewPS extends IWPresentationStateImpl implements IWActionListener {

  Group _selectedGroup = null;


  public GroupApplicationOverViewPS() {

  }

 
  public void reset(){
    super.reset();
    _selectedGroup = null;
  }

  public void actionPerformed(IWPresentationEvent e)throws IWException{

    if(e instanceof ResetPresentationEvent){
      this.reset();
      this.fireStateChanged();
    }
    
    if(e instanceof SelectGroupEvent){
      _selectedGroup = ((SelectGroupEvent)e).getSelectedGroup();
       this.fireStateChanged();
    }

/*
    if(e instanceof SelectGroupEvent){
      _selectedGroup = ((SelectGroupEvent)e).getSelectedGroup();
      _selectedDomain = null;
      _selectedPartition = _selectedPartitionDefaultValue;
      _partitionSize = _partitionSizeDefaultValue;
      _firstPartitionIndex = _firstPartitionIndexDefaultValue;
      this.fireStateChanged();
    }

    }*/

  }

  public Group getSelectedGroup(){
    return _selectedGroup;
  }
}