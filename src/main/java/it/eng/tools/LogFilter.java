package it.eng.tools;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

import it.eng.iot.configuration.Conf;

public class LogFilter implements Filter {
	
	private static final String logLevel = Conf.getInstance().getString("log.level");

	@Override
	public boolean isLoggable(LogRecord record) {
		if(record == null) {
			return false;
		}
		
		String messageLevel = record.getLevel() == null ? "" : record.getLevel().toString();
		
		if(messageLevel.contains(logLevel))
			return true;
		
		
		return false;
	}

}
