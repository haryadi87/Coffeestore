// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.utils;


/**
 *  A class to represent exceptions that may arise during an htcp2 session.
 */
public class Htcp2Exception extends Exception
{
    private String message;

    /**
     *  Create a new exception object, with an error message received
     *  from the server.
     */
        public
    Htcp2Exception (String error)
    {
        message = error;
    }

    /**
     *  Return a description of the error that has occured.
     */
        public String
    what ()
    {
        return message;
    }
}

// vim: set ts=4 sw=4 et
