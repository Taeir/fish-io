package com.github.fishio.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Log handler that outputs in the a txt file.
 */
public class TxtFileHandler implements IHandler {

	private IFormatter format = new DefaultFormat();
	private BufferedWriter bw;
	
	/**
	 * Create ConsoleHandler with default formatter.
	 * @param name
	 * 		Name of logfile, .txt is automatically added.
	 */
	public TxtFileHandler(String name) { 
		try {
			File file = new File("/" + name + ".txt");
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			//Save BufferedWriter
			bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Create ConsoleHandler with custom formatter.
	 * @param formatter
	 * 		The custom format the handler should adapt.
	 * @param name
	 * 		Name of logfile, .txt is automatically added.
	 */
	public TxtFileHandler(IFormatter formatter, String name) {
		format = formatter;
		try {
			File file = new File("/" + name + ".txt");
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			//Save BufferedWriter
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bw == null) ? 0 : bw.hashCode());
		result = prime * result + ((format == null) ? 0 : format.hashCode());
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
}
