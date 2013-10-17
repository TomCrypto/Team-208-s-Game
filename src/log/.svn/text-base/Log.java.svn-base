package log;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.logging.Formatter;
import org.apache.commons.lang3.StringEscapeUtils;;

/**
 * This is  a simple HTML logger  which can
 * be used to easily log program events.
 * 
 * @author Thomas Beneteau (300250968)
 */
public final class Log
{
	private final static String defaultPath = "log.html";
	private final static Logger logger = Logger.global;

	/**
	 * Initializes   the  logger   instance  to
	 * output formatted log  records to a given
	 * file. This  should be called  before any
	 * logging occurs.
	 *
	 * @param target The program being logged.
	 * @param path The file to which to log.
	 */
	public final static void setupLogger(final String target, final String path) throws IOException
	{
		final FileHandler handler = new FileHandler(path, false);
		handler.setFormatter(new HTMLFormatter(target));
		logger.setUseParentHandlers(false);
		logger.addHandler(handler);
		logger.setLevel(Level.ALL);
	}

	/**
	 * Convenience  setup  method which  writes
	 * the  log  to  a   default  file  in  the
	 * program's working directory.
	 *
	 * @param target The program being logged.
	 */
	public final static void setupLogger(final String target) throws IOException
	{
		setupLogger(target, defaultPath);
	}

	/**
	 * Disables logging completely.
	 */
	public final static void disableLogger()
	{
		logger.setLevel(Level.OFF);
	}

	private final static void log(final Level level, final Throwable error, final String component, final String message, final Object... args)
	{
		logger.logp(level, component, null, String.format(message, args), error);
	}

	public final static void severe(final String component, final String message, final Object... args)
	{
		log(Level.SEVERE, null, component, String.format(message, args));
	}

	public final static void severe(final Throwable error, final String component, final String message, final Object... args)
	{
		log(Level.SEVERE, error, component, String.format(message, args));
	}

	public final static void warning(final String component, final String message, final Object... args)
	{
		log(Level.WARNING, null, component, String.format(message, args));
	}

	public final static void warning(final Throwable error, final String component, final String message, final Object... args)
	{
		log(Level.WARNING, error, component, String.format(message, args));
	}

	public final static void info(final String component, final String message, final Object... args)
	{
		log(Level.INFO, null, component, String.format(message, args));
	}

	public final static void info(final Throwable error, final String component, final String message, final Object... args)
	{
		log(Level.INFO, error, component, String.format(message, args));
	}

	public final static void debug(final String component, final String message, final Object... args)
	{
		log(Level.FINER, null, component, String.format(message, args));
	}

	public final static void debug(final Throwable error, final String component, final String message, final Object... args)
	{
		log(Level.FINER, error, component, String.format(message, args));
	}

	/**
	 * This is a custom formatter for producing
	 * nicely formatted HTML log tables.
	 */
	public static class HTMLFormatter extends Formatter
	{
		private final long startTime;
		private final String target;
		private int row = 0;

		public HTMLFormatter(final String target)
		{
			startTime = System.currentTimeMillis();
			this.target = target;
		}

		@Override
		public final String format(final LogRecord record)
		{
			final StringBuffer log = new StringBuffer(65536);
			if((row += 1) % 2 == 0) log.append("<tr>");
			else log.append("<tr class=\"alt\">");

			log.append("<td>"); log.append(formatRecordTime(record));      log.append("</td>");
			log.append("<td>"); log.append(formatRecordComponent(record)); log.append("</td>");
			log.append("<td>"); log.append(formatRecordSeverity(record));  log.append("</td>");
			log.append("<td>"); log.append(formatRecordMessage(record));   log.append("</td>");
			log.append("<td>"); log.append(formatRecordDetails(record));   log.append("</td>");

			log.append("</tr>\n");
			return log.toString();
		}

		private final String formatRecordTime(final LogRecord record)
		{
			return escape(getFormattedDate(startTime, record.getMillis()));
		}

		private final String formatRecordComponent(final LogRecord record)
		{
			if (record.getSourceClassName() == null) return escape("(unknown)");
			else return escape(record.getSourceClassName());
		}

		private final String formatRecordSeverity(final LogRecord record)
		{
			if (record.getLevel().intValue() == Level.SEVERE.intValue())  return "<font color=\"#FF0000\">" + escape("Severe");
			if (record.getLevel().intValue() == Level.WARNING.intValue()) return "<font color=\"#FF0000\">" + escape("Warning");
			if (record.getLevel().intValue() == Level.INFO.intValue())    return "<font color=\"#000000\">" + escape("Info");
			if (record.getLevel().intValue() == Level.FINER.intValue())   return "<font color=\"#008000\">" + escape("Debug");
			return record.getLevel().toString();
		}

		private final String formatRecordMessage(final LogRecord record)
		{
			return escape(record.getMessage());
		}

		private final String formatRecordDetails(final LogRecord record)
		{
			if (record.getThrown() == null) return escape("");
			return escape(record.getThrown().toString());
		}

		@Override
		public final String getHead(final Handler h)
		{
			return "<HTML>\n<HEAD><title>" + escape(target) + " Log</title>\n"
				 + "<link rel=\"stylesheet\" type=\"text/css\" href=\"log.css\" />"
				 + "<style type=\"text/css\">\n" // some custom CSS for the HTML table
				 + ".datagrid table { border-collapse: collapse; text-align: left; width: 100%; }"
				 + ".datagrid {font: normal 12px/150% Arial, Helvetica, sans-serif; background: #fff; overflow: hidden; border: 1px solid #000000; -webkit-border-radius: 7px; -moz-border-radius: 7px; border-radius: 7px; }"
				 + ".datagrid table td, "
				 + ".datagrid table th { padding: 5px 5px; }"
				 + ".datagrid table thead th {background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #998671), color-stop(1, #7F766D) ); background:-moz-linear-gradient( center top, #998671 5%, #7F766D 100% ); filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#998671', endColorstr='#7F766D'); background-color:#998671; color:#FFFFFF; font-size: 12px; font-weight: bold; border-left: 1px solid #A3A3A3; } "
				 + ".datagrid table thead th:first-child { border: none; }"
				 + ".datagrid table tbody td { color: #000000; border-left: 1px solid #000000;font-size: 12px;border-bottom: 1px solid #000000;font-weight: normal; }"
				 + ".datagrid table tbody .alt td { background: #FFF8D6; color: #000000; }"
				 + ".datagrid table tbody td:first-child { border-left: none; }"
				 + ".datagrid table tbody tr:last-child td { border-bottom: none; }"
				 + "</style>\n"
				 + "</HEAD>\n"
				 + "<script type=\"text/javascript\">" // refresh the log every 20 seconds
				 + "function refresh() {"
				 + "setTimeout(\"location.reload(true);\",20000);"
				 + "}"
				 + "window.onload = refresh;"
				 + "</script>\n"
				 + "<BODY>\n"
				 + "<heading><center>" + escape(new Date(startTime).toString()) + "</center></heading>\n"
				 + "\n<PRE>\n"
				 + "<div class=\"datagrid\"><table>\n"
				 + "<thead>"
				 + "<tr>"
				 + "<th>Time</th>"
				 + "<th>Component</th>"
				 + "<th>Severity</th>"
				 + "<th>Message</th>"
				 + "<th>Details</th>"
				 + "</tr>"
				 + "</thead>"
				 + "<tbody>";
		 }

		@Override
		public final String getTail(final Handler h)
		{
			return "</tbody></table></div>\n</PRE>\n</BODY>\n</HTML>\n";
		}
		
		private final String escape(String text)
		{
			return StringEscapeUtils.escapeHtml4(text);
		}
	}
	
	public static final String getFormattedDate(final long startTime, final long time)
	{
		final long MS_PER_HOUR   = 1000 * 60 * 60;
		final long MS_PER_MINUTE = 1000 * 60;
		final long MS_PER_SECOND = 1000;

		long delta = time - startTime;
		long hours = 0, minutes = 0, seconds = 0;

		if (delta > MS_PER_HOUR)
		{
			hours = delta / MS_PER_HOUR;
			delta %= MS_PER_HOUR;
		}

		if (delta > MS_PER_MINUTE)
		{
			minutes = delta / MS_PER_MINUTE;
			delta %= MS_PER_MINUTE;
		}

		if (delta > MS_PER_SECOND)
		{
			seconds = delta / MS_PER_SECOND;
			delta %= MS_PER_SECOND;
		}

		return String.format("%02d:%02d:%02d (+%03d ms)", hours, minutes, seconds, delta);
	}
}
