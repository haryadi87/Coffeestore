// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server;

import java.util.*;

import com.coffeestore.utils.*;


/**
 *  An object to provide a staging area for orders that have been
 *  submitted to the server, but not yet committed. This class implements
 *  the methods defined in the StagingArea interface, as well as an extra
 *  clear() method, which allows the SessionManager to remove all the
 *  orders from the staging area after they have been committed.
 */
public class PendingOrders implements StagingArea
{
    private HashMap <Integer, Order> orders;
    private Set <Integer> ids;
    private int nextIdentifier;

    /**
     *  Instantiate a new staging area. The server should only create
     *  a single staging area for each session, which should be constructed
     *  at the start of the session. This reccommended practice is to
     *  ensure that the unique identifiers assigned to each order are truly
     *  unique.
     */
        public
    PendingOrders ()
    {
        initMapping ();
        nextIdentifier = 1;
    }

    /**
     *  Initialise the hash map and key set. These two objects are bound
     *  together, such that modifications to one will affect the other.
     *  For more information, refer to the API javadoc pages for HashMap,
     *  specifically the keySet method.
     */
        private void
    initMapping ()
    {
        orders = new HashMap <Integer, Order> ();
        ids = orders.keySet ();
    }

    /**
     *  Add a new order to the list of orders awaiting commit.
     */
        public void
    addOrder (Order fromClient)
    {
        // add the order to the hash map, and assign the next available
        // unique id.
        orders.put (nextIdentifier, fromClient);

        // make sure that the next add operation uses a different id.
        nextIdentifier += 1;
    }

    /**
     *  Returns a listing of all the orders that have been submitted in
     *  the current session, and are still awaiting commit.
     */
        public ArrayList <RemoteOrder>
    listOrders ()
    {
        ArrayList <RemoteOrder> listing = new ArrayList <RemoteOrder> ();

        // step through each key in the orders mapping.
        for (int id : (Integer []) ids.toArray ())
        {
            listing.add (new RemoteOrder (orders.get (id), id));
        }

        return listing;
    }

    /**
     *  Remove a given order from the list of orders awaiting commit.
     *  If there is no order that matches the id provided, this method
     *  will return false. If the order is successfully removed, this
     *  method returns true.
     */
        public boolean
    deleteOrder (int id)
    {
        // check if the specified id exists in the mapping.
        if (orders.containsKey (id) != true)
            return false;

        // remove the order from the list.
        orders.remove (id);
        return true;
    }

    /**
     *  Remove all the orders currently awaiting commit. This method is
     *  not defined by the StagingArea interface, because it should not be
     *  called from the transaction state. This method is called from the
     *  session manager, when the HTCP2 session enters the update state.
     *
     *  @return A listing, provided by listOrders, of all the Orders that
     *      were previously awaiting commit.
     */
        public ArrayList <RemoteOrder>
    clearList ()
    {
        ArrayList <RemoteOrder> cleared = listOrders ();

        // create a new hash map, but don't reset the unique ids.
        initMapping ();

        return cleared;
    }
}

// vim: ts=4 sw=4 et
