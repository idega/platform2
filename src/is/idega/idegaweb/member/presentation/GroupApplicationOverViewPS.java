package is.idega.idegaweb.member.presentation;

import com.idega.event.IWActionListener;
import com.idega.event.IWPresentationEvent;
import com.idega.event.IWPresentationStateImpl;
import com.idega.idegaweb.IWException;
import com.idega.presentation.event.ResetPresentationEvent;
import com.idega.user.data.Group;
import com.idega.user.event.SelectGroupEvent;


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