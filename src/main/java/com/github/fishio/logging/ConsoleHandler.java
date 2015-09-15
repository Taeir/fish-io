package com.github.fishio.logging;

/**
 * Log handler that outputs in the console.
 */
public class ConsoleHandler implements IHandler{

	private IFormater format;
	
	@Override
	public void output(LogLevel logLvl, String logMessage) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setFormat(IFormater format) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IFormater getFormat() {
		return format;
	}
	
	@Override
	public boolean equals(IHandler that) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
