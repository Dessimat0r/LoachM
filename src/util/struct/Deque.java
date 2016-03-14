/**
 * 
 */
package util.struct;

import java.util.Enumeration;
import java.util.NoSuchElementException;

import util.Util;

/**
 * LoachM - Deque
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class Deque {
	private final int capacity;
	private int size, first, last;
	private Object[] elements;
	
	/**
	 * @param capacity 
	 */
	public Deque(final int capacity) {
		this.capacity = capacity;
		elements = new Object[capacity];
		empty();
	}
	
	private boolean hasWraparound() {
		return (last < first);
	}
	
	/**
	 * @return the size
	 */
	public int size() {
		return size;
	}
	
	/**
	 * 
	 */
	public void empty() {
		size = 0;
		first = -1;
		last = -1;
	}
	
	/**
	 * @return
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * @return the capacity
	 */
	public int capacity() {
		return elements.length;
	}
	
	private void addSingleton(final Object o) {
		if (o == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		first = 0;
		last = 0;
		
		elements[first] = o;
		size++;
	}
	
	/**
	 * @param o
	 */
	public void addFirst(final Object o) {
		if (o == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		if (size == 0) {
			addSingleton(o);
			return;
		}
		
		int prevIndex = getPrevArrayIndex(first);
		if (prevIndex == last)
			throw new IllegalStateException(
				"No more space"
			);
		
		elements[prevIndex] = o;
		
		first = prevIndex;
		size++;
	}
	
	/**
	 * @param o
	 */
	public void addLast(final Object o) {
		if (o == null) throw Util.NULL_ARGUMENT_EXCEPTION;
		
		if (size == 0) {
			addSingleton(o);
			return;
		}
		
		int nextIndex = getNextArrayIndex(last);
		if (nextIndex == first)
			throw new IllegalStateException(
				"No more space"
			);
		
		elements[nextIndex] = o;
		
		last = nextIndex;
		size++;
	}
	
	/**
	 * @return
	 */
	public Object removeFirst() {
		if (isEmpty()) throw new NoSuchElementException();
		
		final Object temp = elements[first];
		elements[first] = null;
		first = getNextArrayIndex(first);
		size--;
		
		if (size == 0) empty();
		
		return temp;
	}
	
	/**
	 * @return
	 */
	public Object removeLast() {
		if (isEmpty()) throw new NoSuchElementException();
		
		final Object temp = elements[last];
		elements[last] = null;
		last = getPrevArrayIndex(last);
		size--;
		
		if (size == 0) empty();
		
		return temp;
	}
	
	/**
	 * @return Returns the first element.
	 */
	public Object getFirst() {
		if (isEmpty()) throw new NoSuchElementException();
		
		return elements[first];
	}
	
	/**
	 * @return Returns the last element.
	 */
	public Object getLast() {
		if (isEmpty()) throw new NoSuchElementException();
		
		return elements[last];
	}
	
	private int getPrevArrayIndex(final int arrayIndex) {
		int tempFirst = arrayIndex - 1;
		if (tempFirst < 0) tempFirst = elements.length - 1;
		return tempFirst;
	}
	
	private int getNextArrayIndex(final int arrayIndex) {
		int tempLast = arrayIndex + 1;
		if (tempLast >= elements.length) tempLast = 0;
		return tempLast;
	}
	
	private int getArrayIndex(final int index) {
		if (index > size) throw new IndexOutOfBoundsException();
		
		if (!hasWraparound()) return first + index;
		return index - first;
	}
	
	//private int getWrapIndex() {
	//	return 
	//}
	
	/**
	 * @param index
	 * @return
	 */
	public Object get(final int index) {
		if (!indexIsInBounds(index)) throw new IndexOutOfBoundsException();
		return elements[getArrayIndex(index)];
	}
	
	/**
	 * @param index
	 * @return
	 */
	public boolean indexIsInBounds(final int index) {
		return index > 0 && index < size;
	}
	
	private boolean arrayIndexIsInBounds(final int arrayIndex) {
		if (arrayIndex < 0 || arrayIndex >= elements.length) return false;
		
		if (!hasWraparound()) return arrayIndex >= first && arrayIndex <= last;
		return arrayIndex >= first || arrayIndex <= last;
	}
	
	/**
	 * An enumeration that iterates forwards over the elements in this deque.
	 * @return Returns a forward direction enumeration.
	 */
	public Enumeration elements() {
		return new Enumeration() {
			protected int index = first;
			protected int nextIndex = getNextArrayIndex(index);

			public boolean hasMoreElements() {
				return nextIndex <= last;
			}

			public Object nextElement() {
				index = nextIndex;
				nextIndex = getNextArrayIndex(index);
				if (!arrayIndexIsInBounds(index))
					throw new ArrayIndexOutOfBoundsException();
				return elements[index];
			}
		};
	}
	
	/**
	 * An enumeration that iterates backwards over the elements in this deque.
	 * @return Returns a backward direction enumeration.
	 */
	public Enumeration elementsDescending() {
		return new Enumeration() {
			protected int index = last;
			protected int nextIndex = getPrevArrayIndex(index);

			public boolean hasMoreElements() {
				return nextIndex >= first;
			}

			public Object nextElement() {
				index = nextIndex;
				nextIndex = getPrevArrayIndex(index);
				if (!arrayIndexIsInBounds(index))
					throw new ArrayIndexOutOfBoundsException();
				return elements[index];
			}
		};
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		for (final Enumeration e = elements(); e.hasMoreElements();) {
			buf.append(e.nextElement().toString() + " ");
		}
		return buf.toString();
	}

	private String debug() {
		final StringBuffer buf = new StringBuffer();
		buf.append("First = ");
		buf.append(first);
		buf.append(" last = ");
		buf.append(last);
		buf.append(" size = ");
		buf.append(size);
		buf.append(" elements: [");
		for (int i = 0; i < capacity; ++i) {
			buf.append(elements[i]);
			if (i < (capacity - 1)) buf.append(" ");
		}
		buf.append("]");
		return buf.toString();
	}
}