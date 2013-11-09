/**
 *  Authors: Haryadi Herdian, Nouras Fatima, Matthew Signorini
 */

package com.coffeestore.client;

import java.net.*;
import java.io.*;
//import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.regex.*;

import com.coffeestore.utils.*;


/**
 *  Provide a high level interface to connect to an htcp2 server, and
 *  submit orders. Htcp2 is the HyperText Coffee Protocol, version 2.
 */
public class Htcp2Client
{
    private DataOutputStream toServer;
    private DataInputStream fromServer;

    // XXX: need a more flexible approach for determining the hash to use.
    public static final String HASH = "SHA-2";

    /**
     *  Initialise a TCP connection to the server, at the given IP address 
     *  and port number.
     *
     *  The IP address is to be provided in binary form, and must be in
     *  network byte order, which means the highest order byte is at index
     *  0 of the array (eg. 192.168.0.1 => 192 then 168 etc.)
     *
     *  @param addr IP address, in binary form (network byte order).
     *  @param port Port number, also in binary form.
     */
        public 
        Htcp2Client (byte [] addr, int port) throws Htcp2Exception
    {
        Socket s;
        String greeting;

        try
        {
            // establish a TCP socket.
            s = new Socket (InetAddress.getByAddress (addr), port);

            // These objects allow us to send commands to the server, and
            // receive replies from the server.
            toServer = new DataOutputStream (s.getOutputStream ());
            fromServer = new DataInputStream (s.getInputStream ());

            // The server will commence the session by sending an OK reply,
            // followed by a greeting message.
            greeting = getSingleLineReply ();
        }
        catch (Exception e)
        {
            System.out.println ("Could not connect to server:");
            e.printStackTrace ();
        }
    }

    /**
     *  Carry out authentication, using a given table number, and shared
     *  secret. The shared secret is established beforehand, and is
     *  verified by sending a digest (cryptographic hash with a one time
     *  nonce value).
     *
     *  @param table Table that the user is sitting at.
     *  @param secret Shared secret, as discussed above.
     */
     /*   public void
    sendTableCredentials (String table, byte [] secret) 
        throws Htcp2Exception
    {
        String line;
        MessageDigest hash;
        byte [] salt;

        // first send the TABLE command. The server will then send a reply
        // which includes a salt, or nonce value, with which we will hash
        // the shared secret. Because the salt changes each time someone
        // starts a session, replay attacks are not possible.
        sendCommand ("TABLE " + table);
        line = getSingleLineReply ();
        salt = getCryptographicSalt (line);

        try
        {
            // now hash the secret with the salt.
            hash = MessageDigest.getInstance (HASH);
            hash.update (secret);
            hash.update (salt);
        }
        catch (Exception e)
        {
            System.err.println ("Could not create password digest:");
            e.printStackTrace ();
            System.exit (0);
            return;
        }

        // send the cryptographic digest of the secret back to the server,
        // to complete the authentication.
        sendCommand ("PASSWD " + 
                DatatypeConverter.printBase64Binary (hash.digest ()) + 
                " " + HASH);
        getSingleLineReply ();
    }*/

    /**
     *  Fetch the menu from the server. The menu itself is kept as an
     *  XML file (TODO: correct?) and this method will return that file
     *  as a flat string. Although this *is* called the *HyperText* Coffee
     *  Protocol, at this early stage, we will not support a hypertext xml
     *  menu, in the interests of keeping this demonstration version
     *  simple.
     *
     *  @return The menu, as a flat string representation.
     */
        public String
    getMenu () throws Htcp2Exception
    {
        String menu = "";

        sendCommand ("GET");

        // step through each line in the multiline reply.
        for (String line : getMultilineReply ())
        {
            menu = menu + line;
        }

        return menu;
    }

    /**
     *  Stage a new order for submission. The new order will be stored
     *  on the server, but will not be released for completion until the 
     *  user finalises it by invoking the commit method. This allows users 
     *  to cancel, or modify orders while they are staged for commit.
     *
     *  @param order Items which are to be ordered.
     */
        public void
    brew (Order order) throws Htcp2Exception
    {
        // the BREW command is a multiline command, so consists of the
        // BREW keyword, a CRLF pair, then one or more items, each on
        // their own line, followed by a line which contains only a
        // period.
        sendCommand ("BREW" + Const.CRLF + order.toString () + ".");
        getSingleLineReply ();
    }

    /**
     *  Commit any pending orders. This method indicates to the server
     *  that the order(s) staged on the server are finalised, and can now
     *  be released to wait staff, who will return the product to the
     *  customer.
     *
     *  If there are no orders staged for commit, this method will have no
     *  effect.
     */
        public void
    commit () throws Htcp2Exception
    {
        sendCommand ("COMMIT");
        getSingleLineReply ();
    }

    /**
     *  Get a summary from the server of all the orders that the current
     *  client has staged for commit. This method can be used to allow the
     *  user to preview what items they have ordered. This method will
     *  return a list of objects which assign a unique id (determined by
     *  the server) for each order which is staged for commit. This allows
     *  the client to use that id when requesting to delete/cancel an
     *  order, ensuring that such operations are idempotent.
     *
     *  Note that the summary does not include orders that have been
     *  committed.
     *
     *  @return A list of pairs, each with a unique id, and the
     *      corresponding order.
     */
        public ArrayList <RemoteOrder>
    getSummary () throws Htcp2Exception
    {
        String [] orderListings;
        ArrayList <RemoteOrder> orders = new ArrayList <RemoteOrder> ();
        RemoteOrder currentOrder;
        OrderItem thisItem;

        sendCommand ("STATUS");
        orderListings = getMultilineReply ();

        if (orderListings.length >= 2)
        {
            // get the unique id of the first order.
            currentOrder = new RemoteOrder (new Order (),
                    Integer.valueOf (orderListings [1]).intValue ());
        }
        else
        {
            return orders;
        }

        // step through the list of orders. Each order consists of one or
        // more items, each on its own line, and is terminated with a
        // single empty line. The first line in the reply contains the
        // status code +OK, and is ignored.
        for (int i = 2; i < orderListings.length; i ++)
        {
            // check for an empty line. This indicates that we have read
            // the current order, and a new order follows.
            if (orderListings [i].equals (Const.CRLF))
            {
                orders.add (currentOrder);

                // check that there are more lines in the listing.
                if ((i += 1) >= orderListings.length)
                    break;

                // create a new RemoteOrder object.
                currentOrder = new RemoteOrder (new Order (),
                        Integer.valueOf (orderListings [i]).intValue ());
                continue;
            }

            // create a new item, and add it to the current order.
            thisItem = new OrderItem (orderListings [i]);
            currentOrder.order.addItem (thisItem);
        }

        return orders;
    }

    /**
     *  Remove a given order from the orders that the current client has
     *  staged for commit. This method can also be used to modify an order,
     *  by deleting the old order, and submitting a brew request with the
     *  new one. The id parameter to this method should be the same id
     *  which is provided by the getSummary method.
     *
     *  Note that orders cannot be deleted or modified once they have been
     *  committed.
     *
     *  @param id Unique id of the order to delete.
     */
        public void
    deleteOrder (int id) throws Htcp2Exception
    {
        sendCommand ("DELETE " + id);
        getSingleLineReply ();
    }

    /**
     *  Finalise the htcp2 session. This method sends the QUIT command,
     *  following which the server will close the TCP connection. Note
     *  that any brew requests that are still staged for commit will be
     *  lost; this method will not commit outstanding orders.
     *
     *  After this method has been invoked, no other method on this object
     *  may be called, as the connection to the server is closed.
     */
        public void
    close () throws Htcp2Exception
    {
        sendCommand ("QUIT");
        getSingleLineReply ();
    }

    /**
     *  Send a command to the server. The command may span multiple lines,
     *  by including CRLF pairs within the command (it is up to the caller
     *  to byte stuff '.' chars). This method will add a single CRLF pair
     *  to the end of the command.
     *
     *  @param command The command and parameters to send.
     */
        private void
    sendCommand (String command)
    {
        try
        {
            // step through the string and send each character UTF encoded, one
            // at a time.
            for (int i = 0; i < command.length (); i ++)
            {
                toServer.writeChar (command.charAt (i));
            }

            // write the CRLF pair.
            toServer.writeChars (Const.CRLF);
        }
        catch (IOException e)
        {
            System.err.println ("Error: Unable to send to the server:\n");
            e.printStackTrace ();
        }
    }

    /**
     *  Receive a single line reply from the server. This method will
     *  check the reply status and throw an exception if the status
     *  indicates that an error has occured (-ERR reply). This checking
     *  feature means that this method can be used to confirm that an HTCP2
     *  command is successful, by calling this method and ignoring the
     *  return value.
     *
     *  @return The reply, including the initial status indicator.
     *
     *  @throws Htcp2Exception If the reply indicates an error.
     */
        private String
    getSingleLineReply () throws Htcp2Exception
    {
        String line = "";
        char prev = 0;

        try
        {
            for (char current = 0; (prev != '\r') && (current != '\n');
                    prev = current)
            {
                current = fromServer.readChar ();

                if ((current != '\r') && (current != '\n'))
                    line += current;
            }

            // check if the reply is an error status.
            if (line.charAt (0) == '-')
                throw new Htcp2Exception (line);
        }
        catch (IOException e)
        {
            System.err.println ("Error: Unable to receive from the " +
                    "server.\n");
            e.printStackTrace ();
        }

        return line;
    }

    /**
     *  Receive a multi-line reply from the server. This method makes
     *  use of getSingleLineReply to detect error replies and throw an
     *  exception.
     *
     *  @return An array of lines.
     *
     *  @throws Htcp2Exception If the first line of the reply indicates an
     *      error status.
     */
        private String []
    getMultilineReply () throws Htcp2Exception
    {
        ArrayList <String> lines = new ArrayList <String> ();
        boolean isFirstLine = true;
        String line = "";

        // a multiline reply is simply multiple single line replies
        // terminated by a line containing a '.' char and nothing else.
        while ((line.length () != 1) && (line.charAt (0) != '.'))
        {
            if (isFirstLine)
            {
                // check if there was an error. If everything is ok, the
                // reply is +OK followed by the multiline reply, if not,
                // the server sends a single -ERR line.
                line = getSingleLineReply ();

                // this check is only necessary on the first line, which
                // contains the reply status.
                isFirstLine = false;
            }
            else
            {
                // a -ERR code is only reported on the first line, so any
                // exception thrown from getSingleLineReply now should not 
                // be handed any further up the stack.
                try
                {
                    line = getSingleLineReply ();
                }
                catch (Htcp2Exception e)
                {
                    ;
                }
            }

            lines.add (line);
        }

        return (String []) lines.toArray ();
    }

    /**
     *  Extract a cryptographic salt value that is expected to be present
     *  in a line of text sent from the server. The caller of this method
     *  must break up the line into an array of space delimitted words.
     *  The salt will be identified based on the angular brackets that it
     *  is enclosed within.
     *
     *  @param words An array of space delimitted tokens present in the
     *      text received from the server.
     *
     *  @return The salt, as an array of bytes.
     */
       /* private byte []
    getCryptographicSalt (String line)
    {
        Pattern saltPattern = Pattern.compile ("<([^\\s>]*)>");
        Matcher saltMatcher = saltPattern.matcher (line);
        String base64Salt = saltMatcher.group (0);
        return DatatypeConverter.parseBase64Binary (base64Salt);
    }*/
}

// vim: ts=4 sw=4 et
