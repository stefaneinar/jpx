package jpx;

import java.util.EventObject;

/**
 * PrologServerEvent
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class PrologServerEvent extends EventObject {

	private jpx.PrologServer server;

	public PrologServerEvent(PrologServer server) {
		super(server);
	}
}
