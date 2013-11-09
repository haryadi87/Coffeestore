// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server;

import java.io.*;


/**
 *  This interface defines methods which allow another object to 
 *  communicate with the client. The caller in this case is the classes
 *  which handle the HTCP2 authorisation and transaction states, which
 *  both involve two way communication with the client.
 */
public interface ClientSocket
{
    /**
     *  Read a single line from the client. The line is terminated with
     *  a CRLF pair, which will be included in the string returned by this
     *  method.
     *
     *  @return The CRLF pair terminated line received from the client.
     */
    public String readLine () throws IOException;

    /**
     *  Write a stream of characters back to the client, followed by a
     *  CRLF pair. It is safe to use this method to send multiple lines
     *  in a single call; this method does not check for CRLF pairs inside
     *  the string that is to be sent. This method will add a single CRLF
     *  pair after sending the string passed as a parameter.
     *
     *  @param line Text to send to the client. May contain multiple lines.
     */
    public void writeLine (String line) throws IOException;
}

// vim: ts=4 sw=4 et
