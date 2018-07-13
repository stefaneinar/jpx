package jpx.impl;

/**
 * AtomImpl
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class AtomImpl extends CompoundImpl implements jpx.Atom {

	private String name = null;

	private native String nameImpl();
	
	public String name() {
		if( name == null ) {
			name = nameImpl();
		}
		return name;
	}

	public boolean isName(String name) {
		return name().equals(name);	
	}

	public boolean isAtom() {
		return true;	
	}

	public boolean isCompound() {
		return false;	
	}

	public jpx.Atom asAtom() {
		return this;
	}

	public jpx.Compound asCompound() {
		return null;
	}
}

