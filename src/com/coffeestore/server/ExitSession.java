// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server;


/**
 *  An unchecked exception that permits us to exit the session thread on
 *  the server. In java, a thread can only exit by returning from its run
 *  method. When we are handling a QUIT command some way down the method
 *  call stack, this is rather tricky. Unwinding the stack with an
 *  exception is a good way of accomplishing the task. We decided to use
 *  an unchecked exception because that does not require all our methods
 *  to throw ExitSession.
 */
public class ExitSession extends RuntimeException
{
    // this exception is used for signaling, and does not carry any data
    // or methods.
}

// vim: ts=4 sw=4 et
