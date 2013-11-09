// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.trans;

import java.io.IOException;

import static com.coffeestore.utils.Const.*;
import com.coffeestore.server.*;


/**
 *  Main class for handling HTCP2 transaction state commands for an
 *  authenticated client. The commands themselves are handled by several
 *  other classes that implement the TransactionCommand interface; this
 *  class stores an array of those objects, one for each command. When we
 *  receive a command from the client, we will dispatch the request to
 *  one of the command handlers; whichever one matches the keyword.
 */
public class TransactionState
{
    private static final int NR_HANDLERS = 7;

    private TransactionCommand [] handlers;
    private ClientSocket socket;

    /**
     *  Initialise a new transaction state.
     */
        public
    TransactionState (ClientSocket socket, SessionManager session, 
        StagingArea pending)
    {
        this.socket = socket;

        // initialise the array of command handlers.
        this.handlers = new TransactionCommand [NR_HANDLERS];

        handlers [0] = new NoopHandler (socket);
        handlers [1] = new GetHandler (socket, session);
        handlers [2] = new BrewHandler (socket, pending);
        handlers [3] = new StatusHandler (socket, pending);
        handlers [4] = new DeleteHandler (socket, pending);
        handlers [5] = new CommitHandler (socket, session);
        handlers [6] = new QuitHandler (socket, session);
    }

    /**
     *  This method is used to enter the transaction state from the top
     *  level SessionThread class.
     */
        public static void
    start (ClientSocket socket, SessionManager session, StagingArea area)
        throws AbortSession
    {
        TransactionState ts = new TransactionState (socket, session, area);

        try
        {
            ts.mainLoop ();
        }
        catch (IOException e)
        {
            System.err.println ("Error: unable to communicate with " +
                    "client:");
            e.printStackTrace ();
            throw new AbortSession ();
        }
    }

    /**
     *  Main loop of the transaction state. This method will sit in a loop
     *  of receiving a command from the client, and dispatching to the
     *  corresponding handler to carry out the command.
     */
        private void
    mainLoop () throws IOException
    {
        TransactionCommand cmd;
        String [] words;
        String line;

        while (true)
        {
            // get a command.
            line = socket.readLine ();
            words = line.split (" ");

            // step through the handlers until we find the one to handle
            // this command.
            try
            {
                cmd = getMatchingHandler (words [0]);
                cmd.handler (line);
            }
            catch (HandlerNotFound e)
            {
                socket.writeLine (REPLY_ERR + words [0] + ": I have no " +
                        "knowledge of this command.");
            }
        }
    }

    /**
     *  Search through the list of command handlers for the handler
     *  corresponding to a given keyword.
     *
     *  @param keyword Command name to search for.
     *
     *  @throws HandlerNotFound If no such command exists.
     */
        private TransactionCommand
    getMatchingHandler (String keyword) throws HandlerNotFound
    {
        TransactionCommand handler = null;
        boolean found = false;

        keyword = keyword.toUpperCase ();

        for (TransactionCommand h : handlers)
        {
            if (keyword.equals (h.getName ()))
            {
                found = true;
                handler = h;
                break;
            }
        }

        if (found != true)
            throw new HandlerNotFound ();

        return handler;
    }
}

// vim: ts=4 sw=4 et
