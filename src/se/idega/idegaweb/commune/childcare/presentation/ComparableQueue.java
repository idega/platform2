package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;

import se.idega.idegaweb.commune.childcare.data.ChildCareQueue;

/**
 * This class is used to sort ChildCareQueue object according to their
 * choice number and granted status.
 * @author <a href="mailto:joakim@idega.is">joakim</a>
 * @version $Id: ComparableQueue.java,v 1.1 2003/04/22 18:48:51 joakim Exp $
 * @since 12.2.2003 
 */
class ComparableQueue implements Comparable {
	private ChildCareQueue _queue;
	private boolean _grantedFirst;
				
	ComparableQueue(Object queue, boolean grantedFirst){
		_queue = (ChildCareQueue) queue;
		_grantedFirst = grantedFirst;
	}
		
	ChildCareQueue getQueue(){
		return _queue;
	}

	/**
	 * Compareing two granted queues will give different result
	 * depending on which queue is used as 'comparator' and parameter.
	 * This situation should never happen though, and the order of granted
	 * queues is not important. Two granted queues will never be
	 * equal.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object queue){
		ChildCareQueue que = ((ComparableQueue) queue).getQueue();
		try {
			int diff = _queue.getChoiceNumber() - que.getChoiceNumber();
			
			if (_grantedFirst){
				if (_queue.getStatus().equals(ChildCareQueueUpdateTable.STATUS_UBEH)){ /**@TODO: is this correct status? */
					return  -1;
				} else if (que.getStatus().equals(ChildCareQueueUpdateTable.STATUS_UBEH)){
					return  1;
				} else {
					return diff;
				}
			} else {
				if (diff == 0 && _queue.getStatus().equals(ChildCareQueueUpdateTable.STATUS_UBEH)){ /**@TODO: is this correct status? */
					return  -1;
				} else if (diff == 0 && que.getStatus().equals(ChildCareQueueUpdateTable.STATUS_UBEH)){
					return  1;
				} else {
					return diff;
				}
			}
		} catch(RemoteException ex){
			return -1;
		} 
	}
}