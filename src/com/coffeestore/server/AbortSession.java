// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server;


/**
 *  This exception is used to abort the thread that is handling the current
 *  HTCP2 session. Threads cannot be terminated using System.exit (),
 *  because that would terminate the entire server, which is not the 
 *  behaviour we want. This exception is caught by the run method of
 *  SessionThread, which then returns, exiting the thread.
 */
public class AbortSession extends Exception
{
    // This exception is used for signaling only, and does not contain
    // data or methods.
}

// vim: ts=4 sw=4 et
