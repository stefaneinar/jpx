package jpx;

import javax.swing.event.EventListenerList;

/**
 * PrologQuery
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class PrologQuery {

	protected static final int HANDLE = 0;
	protected static final int CLOSE  = 1;
	protected boolean doCut = false;
	protected String query;
	protected PrologEvent prologEvent = new PrologEvent(this);
	protected EventListenerList listeners = new EventListenerList();


	public PrologQuery(String query, boolean doCut) {
		this.query = query;
	}

	public PrologQuery(String query) {
		this(query, false);
	}

	public void setQuery(String query, boolean doCut) {
		this.query = query;
		this.doCut = doCut;
	}

	public String getQuery() {
		return query;
	}

	public void addPrologQueryListener(PrologQueryListener listener) {
		listeners.add(PrologQueryListener.class, listener);
	}

	public void removePrologQueryListener(PrologQueryListener listener) {
		listeners.remove(PrologQueryListener.class, listener);
	}

	protected void firePrologQueryEvent(final int selector, jpx.Bindings bindings) {
		Object[] listeners = this.listeners.getListenerList();

		for (int i = listeners.length-2; i >= 0; i-=2) {
			prologEvent.setQuery((jpx.Query)bindings);

			switch(selector) {
				case HANDLE:
					((PrologQueryListener)listeners[i+1]).handle(prologEvent);
					break;
				case CLOSE:
					((PrologQueryListener)listeners[i+1]).close(prologEvent);
					break;
			}
		}
	}
}

