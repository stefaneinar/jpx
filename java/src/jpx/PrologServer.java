package jpx;

import java.util.LinkedList;
import javax.swing.event.EventListenerList;

/**
 * PrologServer
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class PrologServer implements Runnable {

	private static boolean restart = false;
	private static boolean useSavedState = true;
	private static String state = "state";
	private EventListenerList listeners = new EventListenerList();
	private PrologServerEvent event = null;
	private static final int INIT    = 0;
	private static final int RELEASE = 1;

	private jpx.JPX jpx;
	private LinkedList queryQueue = new LinkedList();

	public PrologServer() {
		System.out.println("PrologServer()");
	}

	public void start() {
		Thread runner = new Thread(this);
		runner.start();
	}

	public JPX getJPX() {
		return jpx;
	}

	public void addPrologServerListener(PrologServerListener listener) {
		listeners.add(PrologServerListener.class, listener);
	}

	public void removePrologServerListener(PrologServerListener listener) {
		listeners.remove(PrologServerListener.class, listener);
	}

	protected void firePrologEventListener(int state) {
		Object[] listeners = this.listeners.getListenerList();
		for(int i = listeners.length-2; i>=0; i-=2) {
			if ( listeners[i] == PrologServerListener.class ) {
				if( event == null ) {
					event = new PrologServerEvent(this);
				}
				if (state == INIT) {
					((PrologServerListener)listeners[i+1]).init(event);
				} else if (state == RELEASE) {
                    ((PrologServerListener) listeners[i + 1]).release(event);
                }
			}
		}
	}

	public synchronized void send(PrologQuery query) {
		if (query == null) {
		    throw new IllegalArgumentException();
        }
		queryQueue.addLast(query);
	}

	public synchronized void execute() {
		notify();
	}

	public synchronized void restart() {
		useSavedState = false;
		notify();
	}

	public synchronized void restart(String state) {
		useSavedState = true;
		restart = true;
		PrologServer.state = state;
		notify();
	}

	public synchronized void run() {
		System.out.println("run()");
		jpx = new jpx.JPX();

		while(true) {
			if (useSavedState) {
				jpx.initWithSavedState(state);
			} else {
				jpx.init();
			}

			firePrologEventListener(INIT);

			try {
				while (true) {
					wait();
					if (restart) {
						restart = false;
						break;
					}
					if (queryQueue.size() == 0 ) {
					    System.out.println("Do something");
					}
					while (queryQueue.size() > 0) {
						PrologQuery query = (PrologQuery) queryQueue.remove();

						jpx.Query q = jpx.query(query.getQuery());
						while (q.next()) {
							query.firePrologQueryEvent(PrologQuery.HANDLE, q);
						}
						query.firePrologQueryEvent(PrologQuery.CLOSE, q);
						if (query.doCut) {
                            q.cut();
                        } else {
                            q.close();
                        }
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}

			firePrologEventListener(RELEASE);
			jpx.release();
		}
	}
}
