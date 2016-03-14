/**
 * 
 */
package util.struct;

import java.util.Vector;

import util.Util;



/**
 * Loach V (Java) - OccupierGrid
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class Grid {
	public static final IllegalArgumentException
		OUT_OF_GRID_BOUNDS_EXCEPTION = new IllegalArgumentException(
			"Given grid reference was outside grid bounds."
		)
	;
	
	private final Object[][] objects;
	private final int rows, cols;
	
	/**
	 * @param rows 
	 * @param cols 
	 */
	public Grid(final int rows, final int cols) {
		this.rows = rows;
		this.cols = cols;
		
		objects = new Object[rows][cols];
	}
	
	/**
	 * @param row
	 * @param col
	 * @param o
	 */
	public void setAtRowCol(final int row, final int col, final Object o) {
		if (!rowColInBounds(row, col)) throw OUT_OF_GRID_BOUNDS_EXCEPTION;
		objects[row][col] = o;	
	}
	
	/**
	 * @param row
	 * @param col
	 * @return
	 */
	public Object getAtRowCol(final int row, final int col) {
		return objects[row][col];
	}
	
	/**
	 * @param startRow 
	 * @param startCol 
	 * @param rows 
	 * @param cols 
	 * 
	 * @return Returns the first object found within this area.
	 */
	public Object getFirstInRowsCols(
		final int startRow, final int startCol,
		final int rows, final int cols
	) throws IllegalArgumentException {
		if (
			!rowsColsInBounds(startRow, startCol, rows, cols)
		) throw OUT_OF_GRID_BOUNDS_EXCEPTION;
		
		Object obj;
		int row = 0, col;
		
		for (; row < rows; row++)
			for (col = 0; col < cols; col++) {
				obj = objects[startRow + row][startCol + col];
				if (obj != null) return obj;
			}
		return null;
	}
	
	/** 
	 * @param startRow 
	 * @param startCol 
	 * @param rows 
	 * @param cols 
	 * 
	 * @return Returns all objects found within this area.
	 */
	public Object[] getInRowsCols(
		final int startRow, final int startCol,
		final int rows, final int cols
	) {
		if (
			!rowsColsInBounds(startRow, startCol, rows, cols)
		) throw OUT_OF_GRID_BOUNDS_EXCEPTION;
		
		final Vector objs = new Vector();
		int row = 0, col;
		Object obj;
		
		for (; row < rows; row++)
			for (col = 0; col < cols; col++) {				
				obj = objects[startRow + row][startCol + col];
				if (obj != null) objs.addElement(obj);
		}
		
		final Object[] objArr = new Object[objs.size()];
		objs.copyInto(objArr);
		return objArr;
	}
	
	
	
	/**
	 * @param startRow 
	 * @param startCol 
	 * @param rows 
	 * @param cols 
	 * @param obj
	 */
	public void setInRowsCols(
		final int startRow, final int startCol,
		final int rows, final int cols,
		final Object obj
	) {		
		int row = 0, col;
		for (; row < rows; row++)
			for (col = 0; col < cols; col++)
				objects[startRow + row][startCol + col] = obj;
	}
	
	/**
	 * @param row 
	 * @param col 
	 * 
	 * @return
	 */
	public boolean rowColInBounds(final int row, final int col) {
		return Util.rectContainsPoint(
			0, 0, cols, rows,
			col, row
		);
	}
	
	/**
	 * Checks whether the given area is within the boundary of the grid.
	 * 
	 * @param startRow 
	 * @param startCol 
	 * @param rows 
	 * @param cols 
	 * 
	 * @return Returns true if grid boundary contains area, false otherwise.
	 */
	public boolean rowsColsInBounds(
		final int startRow, final int startCol,
		final int rows, final int cols
	) {
		return Util.rectContainsRect(
			0, 0, this.cols, this.rows,
			startCol, startRow, cols, rows
		);
	}
	
	/**
	 * @return the number of rows
	 */
	public int getRows() {
		return rows;
	}
	
	/**
	 * @return the number of cols
	 */
	public int getCols() {
		return cols;
	}
}