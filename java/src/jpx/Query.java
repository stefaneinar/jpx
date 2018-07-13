package jpx;

/**
 * Query
 *
 * For example:
 * jpx.Query q = j.query("append(X,Y,[1,2])");
 * while( q.next() ) {
 *   jpx.Term x = q.get("X");
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public interface Query extends Bindings {
	/**
	 * @return true if there is a another solution available
	 * and makes it available in the query, accessible through
	 * the get method. 
	 *
	 * If next() returns true, another solution was found else
	 * false is returned.
	 */
	boolean next();

	/**
	 * Cuts the query.  
	 */
	void cut();

	/**
	 * Closes the query.  
	 */
	void close();

	/**
	 * @return the term associated with a variable in
	 * the query string.
	 */
	Term get(String key);
}

