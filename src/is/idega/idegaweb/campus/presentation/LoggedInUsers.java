/*
 * Created on Jul 29, 2003
 *
 */
package is.idega.idegaweb.campus.presentation;


/**
 * LoggedInUsers
 * @author aron 
 * @version 1.0
 */
public class LoggedInUsers extends com.idega.block.login.presentation.OnlineUsers {
	
	
	/* (non-Javadoc)
	 * @see com.idega.presentation.PresentationObject#main(com.idega.presentation.IWContext)
	 */
	/*
	public void main(IWContext iwc) throws Exception {
		add(getLoggedInUsers(iwc));
	}
	
	public PresentationObject getLoggedInUsers(IWContext iwc){
		Table table = new Table();
		int row = 1;
		Text tUsers = new Text("Online users:");
		tUsers.setBold();
		table.add(tUsers,1,row++);
		Collection usersLoggedIn = LoginBusinessBean.getLoggedOnInfoList(iwc);
		if(usersLoggedIn!=null && !usersLoggedIn.isEmpty()){
			for (Iterator iter = usersLoggedIn.iterator(); iter.hasNext();) {
				LoggedOnInfo info = (LoggedOnInfo) iter.next();
				table.addText(info.getUser().getName(),1,row++);
			
			}
		}
		else{
			Text tNone = new Text("nobody!!");
			table.add(tNone,1,row);
		}
		
		return table;
	}
*/
}
