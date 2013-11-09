// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.auth;


/**
 *  An exception used to indicate that the credentials supplied by the
 *  client are not valid. This class is used solely for the purpose of
 *  signaling the aforementioned condition. It does not implement any
 *  additional methods or member variables on top of Exception.
 */
public class LoginFailed extends Exception
{
    // As stated above, this class is intentionally empty.
}

// vim: ts=4 sw=4 et
