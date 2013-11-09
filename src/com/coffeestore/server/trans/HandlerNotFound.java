// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.trans;


/**
 *  An exception used within the transaction state to indicate that no
 *  handler could be found for a given command.
 */
public class HandlerNotFound extends Exception
{
    // This class is used for signaling only, and does not export any
    // methods or variables.
}

// vim: ts=4 sw=4 et
