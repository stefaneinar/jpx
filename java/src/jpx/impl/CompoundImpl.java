package jpx.impl;

/**
 * CompoundImpl
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class CompoundImpl extends TermImpl implements jpx.Compound {
	private jpx.Term[] terms = null;
	private String name = null;
	private native String nameImpl();

	public String name() {
		if (name == null) {
			name = nameImpl();
		}
		return name;
	}

	public boolean isName(String name) {
		return name().equals(name);	
	}

	private native int arityImpl();

	public int arity() {
		if( terms == null ) {
			terms = new jpx.Term[arityImpl()];
		}
		return terms.length;
	}

	public boolean isArity(int arity) {
		return arity() == arity;
	}

	public boolean isFunctor(String name, int arity) {
		return name().equals(name) && arity() == arity;	
	}

	private native jpx.Term termImpl(int index);

	public jpx.Term term(int index) {

		if( terms == null ) {
			arity();
		}
		if( terms[index] == null ) {
			terms[index] = termImpl(index);
		}
		return terms[index];
	}

	public boolean isCompound() {
		return true;
	}

	public jpx.Compound asCompound() {
		return this;	
	}
}

