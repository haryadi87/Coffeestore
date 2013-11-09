// Authors: Haryadi Herdian, Nouras Fatima, Matthew Signorini

package com.coffeestore.utils;


/**
 *  Constants which are shared by both the client and server components ot
 *  the HTCP2 protocol.
 */
public class Const
{
    // line endings are a CRLF pair.
    public static final String CRLF = "\r\n";

    // status codes used in the replies from the server.
    public static final String REPLY_OK = "+OK ";
    public static final String REPLY_ERR = "-ERR ";
}

// vim: ts=4 sw=4 et
