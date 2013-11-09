// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server;

import java.io.*;
import java.net.*;

import com.coffeestore.utils.*;


/**
 *  A class to provide a communications endpoint for the server to talk to
 *  the client. This class exports a close method as well, which should
 *  be used by the session manager to close the network connection. Other
 *  callers should be given a ClientSocket reference, which limits them to
 *  only call the methods defined in that interface.
 */
public class ClientEndpoint implements ClientSocket
{
    private BufferedReader fromClient;
    private DataOutputStream toClient;
    private Socket client;

    /**
     *  Create a new endpoint object given a socket that has been 
     *  established between the client and the server.
     */
        public
    ClientEndpoint (Socket clientSocket)
    {
        try
        {
            client = clientSocket;
            fromClient = new BufferedReader (
                    new InputStreamReader (clientSocket.getInputStream ()));
            toClient = new DataOutputStream (clientSocket.getOutputStream ());
        }
        catch (IOException e)
        {
            // an exception here is not recoverable.
            System.err.println ("Error getting IO streams from socket:");
            e.printStackTrace ();
        }
    }

    /**
     *  Read a single line from the client. The string returned will
     *  include the CRLF pair that signals the end of the line.
     */
        public String
    readLine () throws IOException 
    {
        return fromClient.readLine ();
    }

    /**
     *  Write a single line back to the client. The string passed as a
     *  parameter may include multiple lines, but this method will add
     *  the terminating CRLF pair.
     */
        public void
    writeLine (String line) throws IOException
    {
        toClient.writeChars (line + Const.CRLF);
    }

    /**
     *  Close the connection between the client and the server.
     */
        public void
    close ()
    {
        try
        {
            client.close ();
        }
        catch (Exception e)
        {
            System.err.println ("Could not close socket:");
            e.printStackTrace ();
        }
    }
}

// vim: ts=4 sw=4 et
