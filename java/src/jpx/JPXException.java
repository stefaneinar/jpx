package jpx;

/**
 * JPXException
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class JPXException extends RuntimeException {
	public JPXException() {
		super();	
	}

	public JPXException(Throwable t) {
		super(t);	
	}

	public JPXException(String msg) {
		super(msg);	
	}

	public JPXException(String msg, Throwable t) {
		super(msg, t);	
	}
}

