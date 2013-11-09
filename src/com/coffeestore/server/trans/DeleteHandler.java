// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.trans;

import java.io.IOException;

import static com.coffeestore.utils.Const.*;
import com.coffeestore.server.*;


/**
 *  Handler for the HTCP2 DELETE command. This command deletes an order
 *  specified by an id value given as a command parameter. The id's are
 *  specified by the server, and are provided by the STATUS command, which
 *  lists all pending orders, with their id values.
 */
public class DeleteHandler implements TransactionCommand
{
    private static final String name = "DELETE";
    private ClientSocket client;
    private StagingArea orders;

    /**
     *  Initialise the delete handler.
     */
        public
    DeleteHandler (ClientSocket client, StagingArea orders)
    {
        this.client = client;
        this.orders = orders;
    }

    /**
     *  Carry out the delete command, given the line received from the
     *  client. This method sends back a positive status reply if the
     *  specified order was successfully deleted, and an error reply if it
     *  wasn't, for some reason.
     */
        public void
    handler (String line) throws IOException
    {
        String [] words = line.split (" ");
        int id = Integer.valueOf (words [1]).intValue ();

        // try to delete the specified order.
        if (orders.deleteOrder (id) != true)
        {
            client.writeLine (REPLY_ERR + "I couldn't delete order " +
                    id + ". That id is wrong.");
        }
        else
        {
            client.writeLine (REPLY_OK + "Deleted order " + id + ".");
        }
    }

    /**
     *  Return the command name.
     */
        public String
    getName ()
    {
        return name;
    }
}

// vim: ts=4 sw=4 et
