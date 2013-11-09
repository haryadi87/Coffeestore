// Authors: H. Herdian, N. Fatima and M. Signorini

package com.coffeestore.server;

import java.net.*;
import java.io.*;


/**
 *  A program to receive orders using the Hyper Text Coffee Protocol.
 */
public class ServerMain
{
    // These arrays of strings define the command line options that may
    // be provided by the server's operator.
    private static final String [] shortOptions = {"-p", "-l", "-h"};
    private static final String [] longOptions = 
        {"--port", "--log", "--help"};

    // the number of options in the above lists.
    private static final int NR_OPTIONS = 3;

    // indices of options into the above option lists.
    private static final int PORT_FLAG = 0;
    private static final int LOGFILE_FLAG = 1;
    private static final int USAGE_FLAG = 2;

    private ServerSocket listenSocket;
    private int portNumber;
    private String passwdPath, menuPath;

    /**
     *  Initialise the server, based on the command line parameters
     *  provided by the operator.
     */
        public
    ServerMain (String [] args)
    {
        portNumber = 0;

        passwdPath = ".";
        menuPath = ".";

        // parse the command line options.
        parseOptions (args);

        // make sure we have a valid port to bind to.
        if (portNumber == 0)
        {
            System.err.println ("I need a port number!");
            printUsage ();
            System.exit (1);
        }
        else
        {
            initListenSocket ();
        }
    }

    /**
     *  Entry point of the server program. This method will simply create
     *  an instance of this class, passing the argument list to the 
     *  constructor, which will parse command line options.
     */
        public static void
    main (String [] args)
    {
        try
        {
            ServerMain server = new ServerMain (args);
            server.startServer ();
        }
        catch (Exception e)
        {
            System.err.println ("Unrecoverable error:");
            e.printStackTrace ();
        }
    }

	    private void
    startServer () throws IOException
    {
        Thread worker;
        Socket client;

        while (true)
        {
            // wait for a new client to connect.
            client = listenSocket.accept ();

            // start a new thread to service the client.
            worker = new Thread (
                new SessionThread (client, passwdPath, menuPath));
            worker.start ();
        }
    }

    /**
     *	Set up a TCP socket which we will listen for connections on. The
     *	port number is obtained from the command line params, and is stored
     *	in the portNumber member variable.
     */
	    private void
    initListenSocket ()
    {
        try
        {
            listenSocket = new ServerSocket (portNumber);
        }
        catch (IOException e)
        {
            System.err.println ("Could not establish listening port:");
            e.printStackTrace ();
            System.exit (1);
        }
    }

    /**
     *  Search through the list of command line parameters for any options
     *  defined in the list of accepted options.
     *
     *  @param args The list of options provided on the command line.
     */
        private void
    parseOptions (String [] args)
    {
        boolean found;
        int option;

        // step through each item in the list of options.
        for (int i = 0; i < args.length; i ++)
        {
            found = false;
            option = 0;

            // search for the option in the lists of known short and long
            // options.
            for (int j = 0; j < NR_OPTIONS; j ++)
            {
                if ((args [i].equals (shortOptions [j])) ||
                        (args [i].equals (longOptions [j])))
                {
                    found = true;
                    option = j;
                    break;
                }
            }

            if (found == true)
            {
                // we found an option. Check which one it was.
                switch (option)
                {
                case PORT_FLAG:
                    // The port number option has an argument, being the
                    // port number. We will advance i, because the next
                    // proper option must come after the port number
                    // argument.
                    portNumber = Integer.valueOf (args [i + 1]).intValue ();
                    i += 1;
                    break;

                case LOGFILE_FLAG:
                    // This is unused at the moment. It will probably take
                    // a file name as an argument, though.
                    break;

                case USAGE_FLAG:
                    printUsage ();
                    System.exit (0);
                }
            }
            else
            {
                System.err.println ("Unrecognised option \"" + args [i] +
                        "\".");
                printUsage ();
                System.exit (1);
            }
        }
    }

    /**
     *  Print usage information on stderr.
     */
        private void
    printUsage ()
    {
        System.err.println (
                "htcp2d: Hyper Text Coffee Protocol server.\n\n" +
                "OPTIONS:\n\n" +
                "\t-p --port    The TCP port to listen for requests on\n" +
                "\t-l --log     Log file to write to\n" +
                "\t-h --help    Print this information and exit\n");
    }
}

// vim: set ts=4 sw=4 et
