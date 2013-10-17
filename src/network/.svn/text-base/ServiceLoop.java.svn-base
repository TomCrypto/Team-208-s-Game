package network;

import naga.*;

/**
 * Implements   a  network   service  loop,
 * useful for having  network events happen
 * on a separate thread.
 * 
 * @author Thomas Beneteau (300250968)
 */
public class ServiceLoop implements Runnable
{
	private final ExceptionObserver errorHandler;
	private final NIOService service;
	
	public ServiceLoop(NIOService service, ExceptionObserver errorHandler)
	{
		this.errorHandler = errorHandler;
		this.service = service;
	}
	
	@Override
	public void run()
	{
		try
		{
			while (service.isOpen()) service.selectBlocking();
		}
		catch (Exception e)
		{
			errorHandler.notifyExceptionThrown(e);
		}
	}
	
	/**
	 * Starts  a service  loop  with the  given
	 * service and  error handler.  The service
	 * loop will continue  until the service is
	 * no longer open (for instance, by calling
	 * {@code service.close()}).
	 * 
	 * This method will not block.
	 * 
	 * @param service The NIO service.
	 * @param errorHandler The error handler.
	 */
	public static void execute(NIOService service, ExceptionObserver errorHandler)
	{
		new Thread(new ServiceLoop(service, errorHandler)).start();
	}
}
