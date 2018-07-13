package jpx;

/**
 * The base interface of all objects in JPX. 
 *
 * A JPX Term corresponds to a Prolog term.
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public interface Term {
	/**
	 * @return true if term is of type Atom.
	 */
	boolean isAtom();

	/**
	 * @return true if term is of type Compound.
	 */
	boolean isCompound();

	/**
	 * @return true if term is of type Variable.
	 */
	boolean isVariable();

	/**
	 * @return true if term is of type Integer.
	 */
	boolean isInteger();

	/**
	 * @return term as an object of type Compound.
	 */
	Compound asCompound();

	/**
	 * @return term as an object of type Atom.
	 */
	Atom asAtom();

	/**
	 * @return term as an object of type Variable.
	 */
	Variable asVariable();

	/**
	 * @return term as an object of type Integer.
	 */
	Integer asInteger();
}

