package jpx;

import java.util.EventListener;

/**
 * PrologServerListener
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public interface PrologServerListener extends EventListener {

	void init(PrologServerEvent event);
	
	void release(PrologServerEvent event);
}
