/**
 * 
 */
package loach.model.field;

import loach.model.field.object.FieldObject;

/**
 * LoachM - FieldEventListener
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public interface FieldEventListener {
	public void onFieldObjectCreation(final FieldObject fo);
	public void onFieldObjectDisposal(final FieldObject fo);
	public void onFieldDoorsOpen(final Field field);
	public void onFieldDoorsClose(final Field field);
}