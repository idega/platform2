package is.idega.idegaweb.golf.tournament.presentation;

public class TournamentTourEditorWindow extends TournamentAdministratorWindow {
	public TournamentTourEditorWindow() {
		this("TournamentTourAdmin",850,600);
	}

	/**
	 * @param name
	 * @param width
	 * @param height
	 */
	public TournamentTourEditorWindow(String name, int width, int height) {
		super(name, width, height);
		this.setGolfClassToInstanciate(TournamentTourEditor.class);
	}

}
