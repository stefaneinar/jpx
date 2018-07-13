package jpx;

import java.util.EventListener;

/**
 * PrologQueryListener
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public interface PrologQueryListener extends EventListener {

	void handle(jpx.PrologEvent event);
	void close(jpx.PrologEvent event);
}
