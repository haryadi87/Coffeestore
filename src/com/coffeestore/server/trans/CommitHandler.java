// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.trans;

import java.io.IOException;

import static com.coffeestore.utils.Const.*;
import com.coffeestore.server.*;


/**
 *  Handler for the HTCP2 COMMIT command. This results in any pending
 *  orders being immediately released.
 */
public class CommitHandler implements TransactionCommand
{
    private static final String name = "COMMIT";
    private ClientSocket client;
    private SessionManager session;

    /**
     *  Initialise the handler.
     */
        public
    CommitHandler (ClientSocket client, SessionManager session)
    {
        this.client = client;
        this.session = session;
    }

    /**
     *  Handle the COMMIT command.
     */
        public void
    handler (String line) throws IOException
    {
        int nrReleased = session.releaseOrders ();
        client.writeLine (REPLY_OK + nrReleased + " Pending orders " +
                "released.");
    }

    /**
     *  Return the name of the command we are handling.
     */
        public String
    getName ()
    {
        return name;
    }
}

// vim: ts=4 sw=4 et
