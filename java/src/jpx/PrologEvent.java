package jpx;

import java.util.EventObject;

/**
 * PrologEvent
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class PrologEvent extends EventObject {

	private jpx.Query query;

	public PrologEvent(PrologQuery query) {
		super(query);
	}

	public jpx.Query getQuery() {
		return query;
	}
	
	void setQuery(jpx.Query query) {
		this.query = query;
	}
}

