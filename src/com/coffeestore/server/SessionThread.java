// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server;

import java.util.ArrayList;
import java.io.*;
import java.net.*;

import com.coffeestore.server.auth.*;
import com.coffeestore.server.trans.TransactionState;
import com.coffeestore.server.update.UpdateState;
import com.coffeestore.utils.*;


public class SessionThread implements Runnable, SessionManager
{
    // message that will be echoed back to the client when they connect.
    private static final String greetMessage = "Greetings, Earthlings...";

    private boolean authenticated = false;
    private ClientEndpoint client;
    private PendingOrders pending;
    private String passwdDirectory;
    private String menuPath;

    /**
     *  Create a new session, given a socket that the client has connected
     *  to us with.
     */
        public
    SessionThread (Socket clientSocket, String passwdPath, String menuDir)
    {
        client = new ClientEndpoint (clientSocket);
        passwdDirectory = passwdPath;
        menuPath = menuDir;
        pending = new PendingOrders ();
    }

    /**
     *  Entry point for the session thread. This thread is responsible for
     *  serving an individual client, and such threads are created based on
     *  a thread-per-connection server architecture.
     */
        public void
    run ()
    {
        try
        {
            // upon establishment of a connection, the server shall send a
            // positive status reply and a greeting message, on a single
            // line.
            client.writeLine (Const.REPLY_OK + greetMessage);

            // the session then enters the authorisation state when the server
            // verifies the client's credentials.
            AuthState.doLogin (client, this);

            if (authenticated != true)
                return;

            // begin handling transaction state commands.
            TransactionState.start (client, this, pending);
        }
        catch (ExitSession ex)
        {
            return;
        }
        catch (Exception e)
        {
            System.err.println ("Session Aborted.");
            e.printStackTrace ();
        }
    }

    /**
     *  The client has authenticated successfully. This method causes the
     *  session to enter the transaction state.
     */
        public void
    hasValidCredentials ()
    {
        authenticated = true;
    }

    /**
     *  Retrieve the server's copy of the password for a given client.
     *  This method is used by the authentication state to verify the
     *  client's password.
     *
     *  @throws LoginFailed If the user does not exist on the server.
     */
        public byte []
    getPassword (String id) throws LoginFailed
    {
        String fileName = passwdDirectory + "table_" + id + ".passwd";
        byte [] passwd = null;

        try
        {
            RandomAccessFile passwdFile = 
                new RandomAccessFile (fileName, "r");
            passwd = new byte [(int) passwdFile.length ()];
            passwdFile.read (passwd);
        }
        catch (FileNotFoundException e)
        {
            // this will be thrown if the client has given us an invalid
            // username.
            throw new LoginFailed ();
        }        
        catch (IOException ioe)
        {
            System.err.println ("Could not read passwd file:");
            ioe.printStackTrace ();
        }

        return passwd;
    }

    /**
     *  Returns the path to the directory where menu resources are stored.
     *  This is conceptually the root directory for GET requests.
     */
        public String
    getResourcesRoot ()
    {
        return menuPath;
    }

    /**
     *  Release pending orders.
     */
        public int
    releaseOrders ()
    {
        ArrayList <RemoteOrder> orders = pending.clearList ();
        UpdateState.recordReleasedOrders (System.out, orders);
        return orders.size ();
    }

    /**
     *  Terminate the HTCP2 session.
     */
        public void
    quitSession ()
    {
        // close the network connection, then exit the thread. Note that
        // java does not have a thread exit method (unlike, say, pthreads,
        // which does), instead we must transfer execution back to the run
        // method, which will then return to the runtime.
        client.close ();
        throw new ExitSession ();
    }
}

// vim: ts=4 sw=4 et
