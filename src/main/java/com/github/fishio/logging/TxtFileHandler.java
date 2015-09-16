package com.github.fishio.logging;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Log handler that outputs in the a txt file.
 */
public class TxtFileHandler implements IHandler, Closeable {

	private IFormatter format = new DefaultFormat();
	private BufferedWriter bw;
	
	/**
	 * Create ConsoleHandler with default formatter.
	 * @param file
	 * 		File of logfile.
	 */
	public TxtFileHandler(File file) { 
		try {
			// If file doesn't exists, then create it
			if (!file.exists()) {
				
				file.createNewFile();
			}
			// Save BufferedWriter
			bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create ConsoleHandler with custom formatter.
	 * @param formatter
	 * 		The custom format the handler should adapt.
	 *  @param file
	 * 		File of logfile.
	 */
	public TxtFileHandler(IFormatter formatter, File file) {
		format = formatter;
		try {
			// If file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// Save BufferedWriter
			bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void output(LogLevel logLvl, String logMessage) {
		try {
			bw.write(format.formatOutput(logLvl, logMessage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setFormat(IFormatter formatter) {
		format = formatter;
	}

	@Override
	public IFormatter getFormat() {
		return format;
	}
	
	/**
	 * Set buffered writer.
	 * @param bw2
	 * 		BufferedWriter to be set.
	 */
	public void setBufferedWriter(BufferedWriter bw2) {
		bw = bw2;
	}
	
	/**
	 * Get buffered writer.
	 * @return bw
	 * 		BufferedWriter that is set for this TxtFileHandler.
	 */
	public BufferedWriter getBufferedWriter() {
		return bw;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		if (bw != null) {
			result = result + bw.hashCode();
		} 		
		if (format == null) {
			return result;
		} else {
			return result + format.hashCode();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TxtFileHandler other = (TxtFileHandler) obj;
		if (bw == null) {
			if (other.bw != null) {
				return false;
			}
		} else if (!bw.equals(other.bw)) {
			return false;
		}
		if (format == null) {
			if (other.format != null) {
				return false;
			}
		} else if (!format.equals(other.format)) {
			return false;
		}
		return true;
	}

	@Override
	public void close() throws IOException {
		if (bw != null) {
			bw.close();
		}
	}
}
