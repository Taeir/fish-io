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
	private static final int FLUSH_NUMBER = 10;
	
	private IFormatter format = new DefaultFormat();
	private BufferedWriter bufferedWriter;
	private int flushCounter = 0;
	
	/**
	 * Create ConsoleHandler with default formatter.
	 * @param file
	 * 		File of logfile.
	 */
	public TxtFileHandler(File file) { 
		try {
			// If file doesn't exists, then create it
			if (!file.exists()) {
				if (!file.getParentFile().exists()) {
					file.getAbsoluteFile().getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			// Save BufferedWriter
			bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
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
				if (!file.getParentFile().exists()) {
					file.getAbsoluteFile().getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			// Save BufferedWriter
			bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Logs to a file specified by the BufferedWriter.<br>
	 * Flushes every now and then specified by the flushNumber
	 * final attribute. This is so that not the entire BufferedWriter
	 * has to be full before flushed, and will log even when the game
	 * crashes early.
	 * @param logLvl
	 * 		LogLevel of the log.
	 * @param logMessage
	 * 		Log message of the log.
	 */
	@Override
	public void output(LogLevel logLvl, String logMessage) {
		try {
			bufferedWriter.write(format.formatOutput(logLvl, logMessage));
			bufferedWriter.newLine();
			
			// Flush every now and then to ensure 
			flushCounter++;
			if (flushCounter >= FLUSH_NUMBER) {
				bufferedWriter.flush();
			}
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
		bufferedWriter = bw2;
	}
	
	/**
	 * Get buffered writer.
	 * @return bw
	 * 		BufferedWriter that is set for this TxtFileHandler.
	 */
	public BufferedWriter getBufferedWriter() {
		return bufferedWriter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		if (bufferedWriter != null) {
			result = prime * result + bufferedWriter.hashCode();
		}
		
		if (format != null) {
			result = prime * result + format.hashCode();
		}
		
		return result;
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
		if (bufferedWriter != null) {
			bufferedWriter.close();
		}
	}
}
