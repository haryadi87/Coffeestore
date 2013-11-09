// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.update;

import java.io.*;
import java.util.ArrayList;

import com.coffeestore.utils.*;


/**
 *  A class to handle the update state of an HTCP2 session. This is when
 *  any orders that a customer has submitted to the server are released.
 *
 *  A more sophisticated system would probably have some sort of GUI,
 *  perhaps, to display orders as they are released. Another option would
 *  be for the HTCP2 server to relay orders back to a "thin client" 
 *  terminal (such as a tablet or phone), allowing HTCP2 to be provided as
 *  a cloud service to cafes.
 *
 *  In this simple demo, the server will simply dump the details of the
 *  orders being released onto a designated output stream.
 */
public class UpdateState
{
    private PrintStream output;

    /**
     *  Initialise the update state, using a given stream for output.
     */
        public
    UpdateState (PrintStream output)
    {
        this.output = output;
    }

    /**
     *  Release a list of orders, using a given output stream to print
     *  the salient details.
     */
        public static void
    recordReleasedOrders (PrintStream output, 
        ArrayList <RemoteOrder> orders)
    {
        UpdateState u = new UpdateState (output);
        u.printReleasedOrders (orders);
    }

    /**
     *  Step through the list of orders being released and print the
     *  details of each one.
     */
        private void
    printReleasedOrders (ArrayList <RemoteOrder> orders)
    {
        for (RemoteOrder r : orders)
        {
            output.println ("Order id: " + r.id);
            output.println ("");

            for (OrderItem item : r.order.getItemsList ())
            {
                printAttributes (item);
                output.println ("");
            }

            output.println ("========================================");
        }
    }

    /**
     *  For a given order item, print out each attribute name, and the
     *  correspnding value.
     */
        private void
    printAttributes (OrderItem item)
    {
        for (String name : item.getAttributeNames ())
        {
            output.println (name + ": " + item.getValue (name));
        }
    }
}

// vim: ts=4 sw=4 et
