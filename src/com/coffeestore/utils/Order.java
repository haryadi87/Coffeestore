/**
 *  Authors: Matthew Signorini, Haryadi Herdian, Nouras Fatima.
 */

package com.coffeestore.utils;

import java.util.ArrayList;


/**
 *  A class that keeps a list of which items a customer wishes to order.
 */
public class Order
{
    private ArrayList <OrderItem> items;
    private int nrItems;

    /**
     *  Constructor. Initialises an empty order.
     */
        public
    Order ()
    {
        items = new ArrayList <OrderItem> ();
        nrItems = 0;
    }

    /**
     *  Construct a text representation of the order, appropriate for
     *  being transmitted over a TCP socket. Each order item is converted
     *  to a URL encoded string, and the overall order is a list of such
     *  strings, one per line.
     */
        public String
    toString ()
    {
        String orderString = "";

        // step through each item in this order, and convert it to a URL
        // encoded string.
        for (OrderItem item : items)
        {
            orderString += item.toUrlEncoded () + Const.CRLF;
        }

        return orderString;
    }

    /**
     *  Add a new item to the order list.
     *
     *  @param item The new item to add to this order.
     */
        public void
    addItem (OrderItem item)
    {
        items.add (item);
        nrItems += 1;
    }

    /**
     *  Return a list of items in the order.
     */
        public OrderItem []
    getItemsList ()
    {
        return (OrderItem []) items.toArray ();
    }
}

// vim: ts=4 sw=4 et
