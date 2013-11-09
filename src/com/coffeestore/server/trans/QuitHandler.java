// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.trans;

import java.io.IOException;

import static com.coffeestore.utils.Const.*;
import com.coffeestore.server.*;


/**
 *  Handler for the HTCP2 QUIT command. This command terminates the
 *  session.
 */
public class QuitHandler implements TransactionCommand
{
    private static final String name = "QUIT";
    private ClientSocket client;
    private SessionManager session;

    /**
     *  Create a new instance of the handler.
     */
        public
    QuitHandler (ClientSocket client, SessionManager session)
    {
        this.client = client;
        this.session = session;
    }

    /**
     *  Carry out the QUIT command. This method will send the OK reply to
     *  the client, then close down the network connection.
     */
        public void
    handler (String line) throws IOException
    {
        client.writeLine (REPLY_OK + "Thank you, please come again.");
        session.quitSession ();
    }

    /**
     *  Return the keyword of the command we are handling.
     */
        public String
    getName ()
    {
        return name;
    }
}

// vim: ts=4 sw=4 et
