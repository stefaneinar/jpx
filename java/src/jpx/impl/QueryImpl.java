package jpx.impl;

/**
 * QueryImpl
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class QueryImpl implements jpx.Query {

	private long id;
	private long bindings;

	public native boolean next();

	public native void cut();

	public native void close();

	private native jpx.Term getImpl(String key);
	
	public jpx.Term get(String key) {
		jpx.Term term = getImpl(key);
		realiseTerm(term);
		return term;
	}
	
	private void realiseTerm(jpx.Term term) {
		((TermImpl)term).setIsRealised(true);
		
		if (term.isAtom()) {
			term.asAtom().name();
		} else if (term.isInteger()) {
			term.asInteger().intValue();
		} else if (term.isCompound()) {
			jpx.Compound comp = term.asCompound();
			comp.name();
			for (int arity = comp.arity(), i = 0; i < arity; i++) {
				realiseTerm(comp.term(i));
			}
		}
	}
}

