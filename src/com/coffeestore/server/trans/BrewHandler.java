// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.trans;

import java.io.IOException;

import static com.coffeestore.utils.Const.*;
import com.coffeestore.utils.*;
import com.coffeestore.server.*;


/**
 *  Handler for the HTCP2 BREW command.
 */
public class BrewHandler implements TransactionCommand
{
    private static final String name = "BREW";
    private ClientSocket client;
    private StagingArea orders;

    /**
     *  Initialise the handler object.
     */
        public
    BrewHandler (ClientSocket client, StagingArea orders)
    {
        this.client = client;
        this.orders = orders;
    }

    /**
     *  Handle the BREW command. The protocol specifies that this is a
     *  multi-line command, consisting of the keyword BREW, on a single
     *  line, then multiple lines consisting of items in the order being
     *  submitted. Only one order is submitted in each brew command.
     */
        public void
    handler (String line) throws IOException
    {
        Order request = new Order ();
        String item;

        // keep adding items until the client sends a line with just a
        // single '.' char on it.
        while ((item = client.readLine ()).equals (".") != true)
        {
            request.addItem (new OrderItem (item));
        }

        // add the received order to the list of pending orders.
        orders.addOrder (request);

        // send a success reply to the client.
        client.writeLine (REPLY_OK + "Would you like fries with that?");
    }

    /**
     *  Returns the keyword for this command.
     */
        public String
    getName ()
    {
        return name;
    }
}

// vim: ts=4 sw=4 et
