package network.server.front_end.commands;

import java.io.IOException;

import network.server.back_end.GameServer;

/**
 * This represents an  abstract command for
 * the server's  command-line interface. It
 * has  an {@code  execute()} method  which
 * will perform the command given a list of
 * arguments and a server instance.
 * <p>
 * Commands  may  have arbitrary  arguments
 * except for the special cases below:
 * <p>
 * <i>{@code [command] -h}</i>
 * <b>and</b>
 * <i>{@code [command] --help}</i>
 * <p>
 * Which   are  defined   to  display   the
 * command's usage.  The {@code validate()}
 * method need  not take into  account this
 * special case, as it is intercepted while
 * parsing the command.
 */
public interface ServerCommand
{
	/**
	 * Validates  the arguments  passed to  the
	 * command. This method should be silent.
	 * 
	 * @param args The arguments provided.
	 * 
	 * @return  Should return  {@code true}  if
	 * and  only if  the  passed arguments  are
	 * valid  and  descriptive enough  for  the
	 * command to be carried out meaningfully.
	 */
	public boolean validate(String[] args);

	/**
	 * Returns a  helpful message  string about
	 * how to use the command (preferably as an
	 * argument format synopsis).
	 * <p>
	 * This  method  is   used  when  the  user
	 * provides   invalid   arguments  to   the
	 * command (verified through the validate()
	 * method) or  when the  <i>{@code -h}</i>,
	 * <i>{@code   --help}</i>  arguments   are
	 * provided.
	 * <p>
	 * Because of command aliasing, the command
	 * entered  by  the  user may  not  be  the
	 * canonical  command name  associated with
	 * this Command instance. The user-provided
	 * command is provided as a parameter.
	 * 
	 * @param command The  command, as typed in
	 * by the user.
	 * 
	 * @return Returns the usage string.
	 */
	public String usage(String command);
	
	/**
	 * Executes the command. This method should
	 * assume  the   arguments  passed   to  it
	 * are  valid  (in  the sense  that  {@code
	 * validate()} returns {@code true}) and is
	 * allowed to print to standard output, and
	 * to the logger instance.
	 * 
	 * @param args The command arguments.
	 * @param server The server instance.
	 * 
	 * @return   Returns    {@code   true}   if
	 * the   command   should   lead   to   the
	 * server    shutting    down    and    the
	 * program  terminating, and  {@code false}
	 * otherwise.
	 */
	public boolean execute(String[] args, GameServer server) throws IOException;
}