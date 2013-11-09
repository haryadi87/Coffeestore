// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.trans;

import java.io.IOException;


/**
 *  Defines the interface that must be implemented by a transaction state
 *  command handler object.
 */
public interface TransactionCommand
{
    /**
     *  The name of the command that the handler handles. This is the
     *  keyword that is sent over the socket. It should be in capital
     *  letters.
     */
    public String getName ();

    /**
     *  The method to invoke to handle the command.
     */
    public void handler (String line) throws IOException;
}

// vim: ts=4 sw=4 et
