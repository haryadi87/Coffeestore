// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.trans;

import java.io.*;

import static com.coffeestore.utils.Const.*;
import com.coffeestore.server.*;


/**
 *  Handler for the GET command.
 */
public class GetHandler implements TransactionCommand
{
    private static final String name = "GET";

    private SessionManager session;
    private ClientSocket client;

    /**
     *  Initialise the handler object.
     */
        public
    GetHandler (ClientSocket client, SessionManager session)
    {
        this.client = client;
        this.session = session;
    }

    /**
     *  Handle the GET command. This method will send back the contents of
     *  a file specified by the client as a parameter to the command. The
     *  resource name may not contain space characters.
     */
        public void
    handler (String fromClient) throws IOException
    {
        String [] words = fromClient.split (" ");
        String fileName = session.getResourcesRoot () + words [1];
        BufferedReader fd = new BufferedReader (new FileReader (fileName));
        String line;

        // send the command status.
        client.writeLine (REPLY_OK + "Resource follows...");

        while ((line = fd.readLine ()) != null)
        {
            client.writeLine (line);
        }

        // multi-line replies are terminated by a line containing a single
        // '.' char and nothing else.
        client.writeLine (".");
    }

    /**
     *  Returns the name of the command handled by this handler.
     */
        public String
    getName ()
    {
        return name;
    }
}

// vim: ts=4 sw=4 et
