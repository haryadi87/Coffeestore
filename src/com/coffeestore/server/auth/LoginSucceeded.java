// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.auth;


/**
 *  This exception is thrown when the client supplies valid credentials.
 *  It permits us to unwind the stack of static method calls, which
 *  releases any objects referenced on the stack for garbage collection.
 *  Without this unwinding, those objects would be held in memory until
 *  the session thread itself is terminated.
 */
public class LoginSucceeded extends Exception
{
    // This class exists for signaling, and is not intended to carry any
    // data or method payload. It is intentionally empty.
}

// vim: ts=4 sw=4 et
