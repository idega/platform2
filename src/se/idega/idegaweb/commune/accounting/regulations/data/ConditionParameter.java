package se.idega.idegaweb.commune.accounting.regulations.data;

/**
 * @author Joakim
 * Wrapper for the parameter pair that need to go in the Collection that is 
 * sent as an in parameter to RegulationBusinessBean whenever a 'condition' is used.
 */
public class ConditionParameter {
	private String condition;
	private Object interval;
	
	
	public ConditionParameter(){
	}
	
	public ConditionParameter(String c, Object i){
		condition = c;
		interval = i;
	}
	
	public String getCondition(){
		return condition;
	}
	
	public Object getInterval() {
		return interval;
	}

	public void setCondition(String string) {
		condition = string;
	}

	public void setInterval(Object object) {
		interval = object;
	}

}
