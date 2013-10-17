package ecs.entity;

import java.security.*;

/**
 * This simple static class  is responsible
 * for  generating  unbiased and  uniformly
 * distributed  64-bit ID  numbers for  new
 * entities.  It is  never serialized,  and
 * only  maintains  the state  of  whatever
 * PRNG is used to generate random ID's.
 * 
 * @author Thomas Beneteau (300250968)
 */
public final class IDProvider
{
	private final static transient SecureRandom prng = new SecureRandom();

	public final static synchronized Long generateID()
	{
		return prng.nextLong();
	}
}
