package jpx;

/**
 * Compound
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public interface Compound extends Term {
	/**
	 * @return the name of the Compound as a String.
	 */
	String name();

	/**
	 * @return true if name is literally equal to
	 * this compounds name; 
	 */
	boolean isName(String name);

	/**
	 * @return the arity of the Compound.
	 */
	int arity();

	/**
	 * @return true if arity is equal to
	 * this compounds arity;
	 */
	boolean isArity(int arity);

	/**
	 * @return true if name and arity match the
	 * respective name and arity of this compound.
	 */
	boolean isFunctor(String name, int arity);

	/**
	 * @return the subterm of the Compound at index.
	 */
	Term term(int index);
}

