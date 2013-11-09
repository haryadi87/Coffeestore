// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server;

import java.util.ArrayList;

import com.coffeestore.server.auth.LoginFailed;


/**
 *  Interface to be implemented by the class which is responsible for
 *  managing the HTCP2 session. The methods defined here will be called
 *  by the classes handling the authorisation and transaction states for
 *  state transition, and maintaining the list of orders staged for commit
 *  here on the server.
 */
public interface SessionManager
{
    /**
     *  The client has successfully authenticated. This method is called
     *  from the authorisation state handler, and causes the session to
     *  enter the transaction state.
     */
    public void hasValidCredentials ();

    /**
     *  Fetch the actual password for a given user/table, for the purpose
     *  of verifying the digest that they have sent over the network.
     */
    public byte [] getPassword (String user) throws LoginFailed;

    /**
     *  Returns the path to the root directory where menu files are
     *  stored.
     */
    public String getResourcesRoot ();

    /**
     *  Release any pending orders. This method will cause the session to
     *  enter the update state, when orders are released, then return to
     *  transaction state. This method will return. If there are no pending
     *  orders, this method will have no effect.
     */
    public int releaseOrders ();

    /**
     *  Terminate the HTCP2 session. This method will send a success reply
     *  to the client, then close down the connection and exit the session
     *  thread, carrying out any clean up required by that process. This
     *  method may be called from either the authentication state (where
     *  QUIT is a valid command) or transaction state, and it will not
     *  return.
     */
    public void quitSession ();
}

// vim: ts=4 sw=4 et
