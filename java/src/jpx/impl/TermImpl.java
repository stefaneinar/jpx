package jpx.impl;

/**
 * TermImpl
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class TermImpl implements jpx.Term {
	private long id;

	boolean isRealised = false;

	public boolean isCompound() {
		return false;	
	}

	public boolean isAtom() {
		return false;	
	}

	public boolean isVariable() {
		return false;	
	}

	public boolean isInteger() {
		return false;	
	}

	public jpx.Compound asCompound() {
		return null;	
	}

	public jpx.Atom asAtom() {
		return null;	
	}

	public jpx.Variable asVariable() {
		return null;	
	}

	public jpx.Integer asInteger() {
		return null;	
	}

	private native String termToString();

	public String toString() {
		if (!isRealised) {
			return termToString();
		} else {
			StringBuffer buffer = new StringBuffer();
			toStringImpl(buffer, this);
			return buffer.toString();
		}
	}

	private void toStringImpl(StringBuffer buffer, jpx.Term term) {
		if (term.isAtom()) {
			buffer.append(term.asAtom().name());
		} else if (term.isVariable()) {
			buffer.append("Var");
		} else if (term.isInteger()) {
			buffer.append(term.asInteger().intValue());
		} else if (term.isCompound()) {
			jpx.Compound comp = term.asCompound();
			buffer.append( comp.name() + "(");
			jpx.Term item;
			for (int i = 0, arity = comp.arity(); i < arity; i++) {
				item = comp.term(i);
				toStringImpl(buffer, item);
				if (i < arity - 1) {
					buffer.append(", ");
				}
			}
			buffer.append(")");
		}
	}
	
	protected void setIsRealised(boolean flag) {
		isRealised = flag;
	}
}



