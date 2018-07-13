package jpx;

/**
 * JPX
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class JPX {
	static {
		// Cygwin
		//System.loadLibrary("JPX");
		// Linux
		System.loadLibrary("jpx");
	}

	protected JPX() {}

	public static JPX newInstance() {
		return new JPX();
	}

	/**
	 * Initialise the JPX system. Must be called before
	 * any other operation is attempted.
	 *
	 * @see #initWithSavedState
	 */
	public native void init();

	/**
	 * Initialise the JPX system with a saved state. Must be
	 * called before any other operations is attempted.
	 *
	 * @see #init
	 */
	public native void initWithSavedState(String savedState);

	/**
	 * Finish a JPX session. The JPX system must have been inited
	 * before release().
	 * It is possible to restart the JPX system by using the init
	 * method but all terms from the prior session have then
	 * been invalidated.
	 */
	public native void release();

	/**
	 * Returns the version of the Prolog engine
	 */
	public native long version();

	private native jpx.Query query(byte[] query) throws jpx.JPXException;

	/**
	 * Perform a deterministic call to prolog (single call).
	 * If there are more than one solution to this query only
	 * the first one will be handled.
	 * 'str' is a query string to the Prolog engine.
	 */
	public void call(String str) throws jpx.JPXException {
		jpx.Query q = query(str);
		if( q.next() ) {}
		q.close();
	}

	/**
	 * Send a query to the Prolog engine and return a Query object.
	 * The query object contains all the solutions for the query
	 * and can be iterated over.
	 */
	public jpx.Query query(String query) throws jpx.JPXException {
		try {
			return query(query.getBytes("ISO-8859-1"));
		} catch (java.io.UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
