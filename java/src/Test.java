import jpx.*;

/**
 *
 * Author: Stefan Einar Stefansson (stefaneinar<at>gmail.com)
 *
 */
public class Test
{
	public static void main(final String[] argv)
	{
		// Must get an instance of the JPX class.
		JPX jpx = JPX.newInstance();

		/* Initialise the Prolog engine. Must be performed
		 * before attempting to use it. See also init().
		 */
		System.out.println("Pre 2");
		jpx.initWithSavedState("state");
		System.out.println("Post");

		System.out.println("version " + jpx.version());

		// String query = "append(X,Y,[1,2,3,4,5]).";
		String query = "(X,Y) = (1,2).";

		System.out.println("\n?- " + query + "\n");

		// Send a simple append query to the Prolog layer.
		jpx.Query q = jpx.query(query);
		// Determine if there are solutions to the query above
		while( q.next() )
		{
			// Process a solution

			jpx.Term x = q.get("X");
			jpx.Term y = q.get("Y");
			String s = "append("+x+","+y+")";

			System.out.println(s); // print it out
		}
		/* must not forget to close the query afterwards
		 * or do a q.cut() if we wish to retain the state from the last
		 * query for further queries.
		 */
		q.close();

		jpx.release(); // shutdown the Prolog engine gracefully
	}
}
