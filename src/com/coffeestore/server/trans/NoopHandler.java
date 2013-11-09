// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.trans;

import java.io.IOException;

import static com.coffeestore.utils.Const.*;
import com.coffeestore.server.ClientSocket;


/**
 *  Handler for the HTCP2 NOOP command. This command does nothing, and
 *  succeeds at doing it. It could conceivably be used to check the
 *  connectivity between the client and server.
 */
public class NoopHandler implements TransactionCommand
{
    private static final String name = "NOOP";
    private ClientSocket client;

    /**
     *  This handler only needs a connection to send back a success reply
     *  on.
     */
        public
    NoopHandler (ClientSocket client)
    {
        this.client = client;
    }

    /**
     *  Carrying out the NOOP command simply requires us to send back a
     *  reply to the client.
     */
        public void
    handler (String line) throws IOException
    {
        client.writeLine (REPLY_OK + "Done nothing.");
    }

    /**
     *  Returns the command name.
     */
        public String
    getName ()
    {
        return name;
    }
}

// vim: ts=4 sw=4 et
