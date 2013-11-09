// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.trans;

import java.io.IOException;

import static com.coffeestore.utils.Const.*;
import com.coffeestore.utils.*;
import com.coffeestore.server.*;


/**
 *  Handler for the HTCP2 STATUS command, which lists orders which are
 *  pending commit.
 */
public class StatusHandler implements TransactionCommand
{
    private static final String name = "STATUS";
    private ClientSocket client;
    private StagingArea orders;

    /**
     *  Initialise the handler.
     */
        public
    StatusHandler (ClientSocket client, StagingArea orders)
    {
        this.client = client;
        this.orders = orders;
    }

    /**
     *  Carry out the STATUS command. This method will fetch a listing of
     *  all the orders in the staging area, including the unique id's that
     *  the server assigns to each one, and send the details back in a
     *  multi-line reply.
     */
        public void
    handler (String line) throws IOException
    {
        boolean isFirstOrder = true;

        client.writeLine (REPLY_OK + "Listing follows:");

        // step through each order in the staging area.
        for (RemoteOrder order : orders.listOrders ())
        {
            // we will write empty lines in between each order so that
            // the client can tell where one ends and the next begins.
            // Write an empty line before the order, except for the very
            // first one.
            if (isFirstOrder != true)
                client.writeLine ("");

            isFirstOrder = false;

            // write the server assigned id on the first line.
            client.writeLine ("" + order.id);

            // then write out the list of items.
            client.writeLine (order.toString ());
        }

        // the multi-line reply is terminated by a line with a single
        // "." char on it.
        client.writeLine (".");
    }

    /**
     *  Return the command name that this class handles.
     */
        public String
    getName ()
    {
        return name;
    }
}

// vim: ts=4 sw=4 et
