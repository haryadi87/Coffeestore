// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server.auth;

import java.io.*;
//import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.security.MessageDigest;
import java.util.Arrays;

import com.coffeestore.server.*;
import com.coffeestore.utils.*;


/**
 *  A class to handle the authentication state of an HTCP2 session.
 *  The entry point for the authentication state is the doLogin method,
 *  which will receive credentials (login or table id and digest of the
 *  shared secret) from the client. The hasValidCredentials method of
 *  the SessionManager object will be called if and only if the client
 *  provides valid credentials. Because the method calls involved stay
 *  on the stack for the duration of the HTCP2 session, this class uses
 *  static methods only, to avoid having non garbage collectable objects
 *  stuck in the call stack.
 */
public class AuthState
{
    private static final int SALT_LEN = 128;

    /**
     *  Entry point to the HTCP2 authentication state. This method
     *  receives and processes the authentication state commands defined
     *  by the HTCP2 protocol, by which the client submits a username or
     *  table id, and a cryptographic digest of a shared secret.
     *
     *  @param client Object by which we can communicate with the client.
     *  @param session Object to notify when the client has authenticated.
     *
     *  @throws AbortSession If the connection to the client is broken.
     */
        public static void
    doLogin (ClientSocket client, SessionManager session)
        throws AbortSession
    {
        // this method does not return. When a client authenticates, we
        // will invoke the hasValidCredentials of the session parameter.
        // Here, we enter an infinite loop of receiving credentials,
        // checking them, and continuing if they are invalid.
        while (true)
        {
            try
            {
                parseCommand (client, session, client.readLine ());
            }
            catch (LoginFailed lf)
            {
                try
                {
                    client.writeLine (Const.REPLY_ERR +
                            "Authentication failed: wrong password.");
                }
                catch (Exception e)
                {
                    ;
                }
            }
            catch (LoginSucceeded ls)
            {
                session.hasValidCredentials ();
                return;
            }
            catch (IOException e)
            {
                // IOException is only thrown if there was an error talking
                // to the client over the socket. This is not a recoverable
                // situation.
                System.err.println ("Unrecoverable error during socket " +
                        "operation:");
                e.printStackTrace ();
                throw new AbortSession ();
            }
        }
    }

    /**
     *  Carry out the command specified by the line of text received from
     *  the client.
     *
     *  @param client Needed because we are using static methods.
     */
        private static void
    parseCommand (ClientSocket client, SessionManager session, String line)
        throws LoginSucceeded, LoginFailed, IOException
    {
        String [] words = line.split (" ");
        words [0] = words [0].toUpperCase ();

        // check what the command is.
        if (words [0].equals ("TABLE"))
        {
           // doTable (client, session, words);
        }
        else if (words [0].equals ("USER"))
        {
            doUser (client);
        }
        else if (words [0].equals ("QUIT"))
        {
            doQuit (client, session);
        }
        else if (words [0].equals ("XYZZY"))
        {
            client.writeLine (Const.REPLY_OK + "Nothing happens");
        }
        else
        {
            // unrecognised command.
            client.writeLine (Const.REPLY_ERR + words [0] +
                    ": I don't know what to do with that command!");
        }
    }

    /**
     *  Handle the table command. This involves sending a cryptographic
     *  salt back to the client in the reply, and then receiving and
     *  verifying a digest of the password and the salt.
     *
     *  @param client Endpoint to talk to the client with.
     *  @param words Array of space delimited words from the command.
     */
        /*private static void
    doTable (ClientSocket client, SessionManager session, String [] words) 
        throws LoginSucceeded, LoginFailed, IOException
    {
        byte [] salt = new byte [SALT_LEN];
        SecureRandom rand = new SecureRandom ();
        String [] replyWords;

        // generate the cryptographic salt.
        rand.nextBytes (salt);

        // send salt to client, then await digest of password.
        client.writeLine (Const.REPLY_OK + "Please hash password for " +
                words [1] + " with salt <" +
                DatatypeConverter.printBase64Binary (salt) + ">");

        // get the PASSWD command. If the client sends a different command,
        // we will reply with an error status until we get the PASSWD.
        while (true)
        {
            replyWords = client.readLine ().split (" ");
            replyWords [0] = replyWords [0].toUpperCase ();

            if (replyWords [0].equals ("PASSWD") != true)
            {
                client.writeLine (Const.REPLY_ERR + "Invalid command. " +
                        "I need PASSWD.");
                continue;
            }

            // verify the digest of the password using the copy of the
            // password we have on file.
            if (verifyPassword (session, words [1], 
              DatatypeConverter.parseBase64Binary (replyWords [1]), salt,
              replyWords [2]) != true)
            {
                // invalid password.
                throw new LoginFailed ();
            }
            else
            {
                throw new LoginSucceeded ();
            }
        }
    }*/

    /**
     *  Verify a password given the username, password digest, salt and
     *  hashing algorithm.
     *
     *  @return true if the digest matches, false if it does not.
     */
        private static boolean
    verifyPassword (SessionManager session, String user, byte [] digest, 
        byte [] salt, String algorithm) throws LoginFailed
    {
        boolean result = false;
        MessageDigest hash;
        byte [] actualPassword = session.getPassword (user);

        try
        {
            hash = MessageDigest.getInstance (algorithm);

            // hash the password then the salt.
            hash.update (actualPassword);
            hash.update (salt);

            // compare the digest with what the client sent.
            if (Arrays.equals (hash.digest (), digest) != false)
                result = true;
        }
        catch (Exception e)
        {
            System.err.println ("Error trying to verify password:");
            e.printStackTrace ();
        }

        return result;
    }

    /**
     *  Handle the user command, which is not currently supported by our
     *  simple server. This command is anticipated to be used to allow an
     *  operator to log in to do administrative tasks, such as changing
     *  the password for one or more tables. At this stage, we just return
     *  an unsupported error.
     */
        private static void
    doUser (ClientSocket client) throws IOException
    {
        client.writeLine (Const.REPLY_ERR + "Sorry, the USER command " +
                "is not yet supported here");
    }

    /**
     *  Terminate the session from the authentication state. The QUIT
     *  command is valid in this state according to the protocol, and the
     *  server shall send a success status reply, then close down the
     *  connection.
     *
     *  @param client Communications endpoint to send reply with.
     *  @param session Session to quit.
     */
        private static void
    doQuit (ClientSocket client, SessionManager session) throws IOException
    {
        client.writeLine (Const.REPLY_OK + "That was brief.");
        session.quitSession ();
    }
}

// vim: ts=4 sw=4 et
