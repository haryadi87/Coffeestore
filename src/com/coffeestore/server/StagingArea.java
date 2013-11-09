// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server;

import java.util.ArrayList;

import com.coffeestore.utils.*;


/**
 *  An interface to define the methods that must be exported by an object
 *  that provides a staging area, to keep orders until they are released.
 */
public interface StagingArea
{
    /**
     *  The client has submitted a new order, add it to the staging area.
     *  This method is called from the transaction state, and causes the
     *  session manager to add the order to the list of orders that are
     *  pending commit.
     *
     *  @param fromClient Order that was received from the client.
     */
    public void addOrder (Order fromClient);

    /**
     *  List orders that will be released on the next commit. This method
     *  returns a list of pairs, which assign each order a unique id that
     *  is used as a reference to deleteOrder.
     */
    public ArrayList <RemoteOrder> listOrders ();

    /**
     *  Remove a given order from the list of orders pending commit. The
     *  order is referenced using the id provided by listOrders.
     *
     *  @param id Unique id of the order that is to be removed.
     *
     *  @return true if the order was deleted, false otherwise (eg. if
     *      the order could not be found).
     */
    public boolean deleteOrder (int id);
}

// vim: ts=4 sw=4 et
