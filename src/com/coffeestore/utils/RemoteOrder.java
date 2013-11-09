/**
 *  Authors: Haryadi Herdian, Nouras Fatima, Matthew Signorini
 */

package com.coffeestore.utils;


/**
 *  A class to provide a mapping, such that each order that is staged
 *  for commit on the server has a corresponding unique id value. This
 *  allows deleting an order from the staged orders to be an idempotent
 *  operation, which is a desirable property.
 */
public class RemoteOrder
{
    public Order order;
    public int id;

    /**
     *  Initialise a new mapping, given an Order object, and a unique
     *  id value.
     *
     *  @param order Order object, as per the server's records.
     *  @param id Unique id, as specified by the server.
     */
        public
    RemoteOrder (Order order, int id)
    {
        this.order = order;
        this.id = id;
    }
}

// vim: ts=4 sw=4 et
