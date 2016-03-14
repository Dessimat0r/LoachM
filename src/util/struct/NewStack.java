/**
 * 
 */
package util.struct;

import util.Util;

/**
 * LoachM - NewStack
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class NewStack {
	private final Object[] arr;
	private final int capacity;
	
	private int size = 0;
	
	/**
	 * @param capacity 
	 */
	public NewStack(final int capacity) {
		this.capacity = capacity;
		arr = new Object[capacity];
	}
	
	/**
	 * @return the noOfElements
	 */
	public int size() {
		return size;
	}
	
	/**
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}
	
	/**
	 * @param o
	 */
	public void push(final Object o) {
		if (o == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		if (size >= capacity) throw Util.TOO_MANY_ELEMENTS_EXCEPTION;
		arr[size++] = o;
	}
	
	/**
	 * @return
	 */
	public Object pop() {
		if (size == 0) throw Util.NO_ELEMENTS_EXCEPTION;
		return arr[size--];
	}
	
	/**
	 * @return
	 */
	public Object peek() {
		return arr[size];
	}
	
	/**
	 * @return
	 */
	public boolean empty() {
		return size == 0;
	}
	
	/**
	 * @return
	 */
	public boolean full() {
		return size == capacity;
	}
	
	public void copyInto(final Object[] array) {
		System.arraycopy(arr, 0, array, 0, arr.length);
	}
}
