package jpx.impl;

/**
 * VariableImpl
 *
 * @version 1.0
 * @author Stefan Einar Stefansson
 */
public class VariableImpl extends TermImpl implements jpx.Variable {

	public boolean isVariable() {
		return true;	
	}

	public jpx.Variable asVariable() {
		return this;	
	}
}

