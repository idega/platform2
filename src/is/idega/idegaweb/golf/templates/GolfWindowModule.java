package is.idega.idegaweb.golf.templates;

import com.idega.presentation.ui.Window;


public class GolfWindowModule extends JmoduleWindowModule {


	public void setWindow(Window window){
		setPage(window);
	}
	
	

	
	public void initializePage(){
		setPage(new Window());
	}



	public Window getWindow(){
		return (Window) getPage();
	}

}
