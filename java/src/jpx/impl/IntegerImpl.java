package jpx.impl;

/**
 * IntegerImpl
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class IntegerImpl extends TermImpl implements jpx.Integer {

	private Integer intValue = null;
	
	private native int intValueImpl();

	public int intValue() {
		if( intValue == null ) {
			intValue = new Integer(intValueImpl());
		}
		return intValue.intValue();
	}

	public boolean isInteger() {
		return true;	
	}

	public jpx.Integer asInteger() {
		return this;	
	}
}

