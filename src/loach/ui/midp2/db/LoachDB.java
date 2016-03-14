/**
 * 
 */
package loach.ui.midp2.db;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import util.Util;

/**
 * LoachM - LoachDB
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public final class LoachDB {
	private boolean disposed = false;
	
	private static final char
		NEW_LINE = '\n',
		DELIMITER = '='
	;
	
	private static final String
		RECORD_STORE_NAME = "LoachM",
		RECORD_STORE_HEADER_START = "_LOACHM_HEADER_START_" + NEW_LINE,
		RECORD_STORE_HEADER_END = "_LOACHM_HEADER_END_" + NEW_LINE
	;
	
	private final RecordStore recordStore;
	private final Hashtable map = new Hashtable();
	private final int recordID;
	
	private long creationTime, lastModificationTime;
	
	/**
	 * 
	 */
	public LoachDB() {
		try {
			//RecordStore.deleteRecordStore("LoachM");
			recordStore = RecordStore.openRecordStore(
				"LoachM", true, RecordStore.AUTHMODE_PRIVATE, true
			);
			final RecordEnumeration re = recordStore.enumerateRecords(
				new LoachDBRecordFilter(this),
				null, false
			);
			if (re.numRecords() > 0) {
				recordID = re.nextRecordId();
			} else {
				// initialise record
				creationTime = System.currentTimeMillis();
				final String serialised = serialise();
				final byte[] serialisedData = serialised.getBytes();
				recordID = recordStore.addRecord(
					serialisedData, 0, serialisedData.length
				);
			}
			pull();
		} catch (final RecordStoreException e) {
			throw new RuntimeException(
				"Couldn't set up the record store: " + e.toString()
			);
		}
	}
	
	/**
	 * @return the creationTime
	 */
	public long getCreationTime() {
		return creationTime;
	}
	
	/**
	 * @return the lastModificationTime
	 */
	public long getLastModificationTime() {
		return lastModificationTime;
	}
	
	/**
	 * @throws InvalidRecordIDException
	 * @throws RecordStoreException
	 */
	public void pull() throws InvalidRecordIDException, RecordStoreException {
		try {
			final byte[] data = recordStore.getRecord(recordID);
			
			/* if data is null, record is empty, 
			 * and there is nothing to deserialise! */
			if (data != null) deserialise(new String(data));
		} catch (final RecordStoreNotOpenException e) {
			throw new RuntimeException(
				"The record store should be open! " + e.toString()
			);
		}
	}
	
	/**
	 */
	public void commit() {		
		final String stringData = serialise();
		final byte[] data = stringData.getBytes();
		
		try {
			recordStore.setRecord(
				recordID,
				data,
				0,
				data.length
			);
		} catch (final RecordStoreException e) {
			throw new RuntimeException(e.toString());
		}
	}
	
	private String serialise() {
		lastModificationTime = System.currentTimeMillis();
		
		StringBuffer sb = new StringBuffer();
		String key, val;
		
		sb.append(RECORD_STORE_HEADER_START);
		sb.append(creationTime);
		sb.append(NEW_LINE);
		sb.append(lastModificationTime);
		sb.append(NEW_LINE);
		sb.append(RECORD_STORE_HEADER_END);
		
		for (final Enumeration e = map.keys(); e.hasMoreElements();) {
			key = (String)e.nextElement();
			val = (String)map.get(key);
			
			sb.append(key);
			sb.append(DELIMITER);
			sb.append(val);
			sb.append(NEW_LINE);
		}
		
		return sb.toString();
	}
	
	private void deserialise(final String str) {
		final int length = str.length();
		int index = RECORD_STORE_HEADER_START.length(), nextNLIndex, delimIndex;
		
		nextNLIndex = str.indexOf(NEW_LINE, index);
		creationTime = Long.parseLong(str.substring(index, nextNLIndex));
		index = nextNLIndex + 1;
		
		nextNLIndex = str.indexOf(NEW_LINE, index);
		lastModificationTime = Long.parseLong(
			str.substring(index, nextNLIndex)
		);
		index = nextNLIndex + 1 + RECORD_STORE_HEADER_END.length();
		
		String label, value;
		
		while (index < length) {
			nextNLIndex = str.indexOf(NEW_LINE, index);
			delimIndex = str.indexOf(DELIMITER, index);
			label = str.substring(index, delimIndex);
			value = str.substring(delimIndex + 1, nextNLIndex);
			index = nextNLIndex + 1;
			
			map.put(label, value);
		}
	}
	
	/**
	 * @param label
	 * @return
	 */
	public String get(final String label) {
		return (String)map.get(label);
	}

	/**
	 * @param label
	 * @param value
	 * @return
	 */
	public Object put(final String label, final String value) {
		return map.put(label, value);
	}

	/**
	 * 
	 */
	public void clear() {
		map.clear();
	}
	
	/* (non-Javadoc)
	 * @see util.sys.Disposable#dispose()
	 */
	public void dispose() {
		if (disposed) throw Util.NEEDS_UNDISPOSED_STATE_EXCEPTION;
		try {
			recordStore.closeRecordStore();
		} catch (final Exception e) {}
		
		disposed = true;
	}

	/* (non-Javadoc)
	 * @see util.sys.Disposable#isDisposed()
	 */
	public boolean isDisposed() {
		return disposed;
	}
	
	/**
	 * LoachM - LoachDBRecordFilter
	 * @version 1.0
	 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
	 */
	public class LoachDBRecordFilter implements RecordFilter {
		private final LoachDB loachDB;
		private String recordDataStr;
		
		/**
		 * @param loachDB
		 */
		public LoachDBRecordFilter(final LoachDB loachDB) {
			this.loachDB = loachDB;
		}
		
		/* (non-Javadoc)
		 * @see javax.microedition.rms.RecordFilter#matches(byte[])
		 */
		public boolean matches(final byte[] data) {
			recordDataStr = new String(data);
			return recordDataStr.startsWith(LoachDB.RECORD_STORE_HEADER_START);
		}
	}
}