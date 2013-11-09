/**
 *  Authors: Haryadi Herdian, Nouras Fatima, Matthew Signorini
 */

package com.coffeestore.utils;

import java.util.HashMap;
import java.util.Set;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.io.*;


/**
 *  A class to store data about a single item that a customer is
 *  ordering.
 */
public class OrderItem
{
    private HashMap <String, String> attributes;
    private Set <String> keys;

    /**
     *  Initialise a new, empty order item.
     */
        public
    OrderItem ()
    {
        initMapping ();
    }

    /**
     *  Initialise a new order item, given a list of parameters stored in
     *  a URL encoded string.
     */
        public
    OrderItem (String paramList)
    {
        String [] pair; 
        String name, value;

        // initialise the hash mapping.
        initMapping ();

        // the URL encoded parameter list is a sequence of name=value
        // pairs, separated by & chars.
        for (String param : paramList.split ("&"))
        {
            pair = param.split ("=");

            if (pair.length != 2)
                continue;

            try
            {
                // decode the URL encoded name and value.
                name = URLDecoder.decode (pair [0], "UTF-8");
                value = URLDecoder.decode (pair [1], "UTF-8");

                // add the param to the mapping.
                attributes.put (name, value);
            }
            catch (UnsupportedEncodingException e)
            {
                System.err.println ("Error: I need UTF-8!");
                e.printStackTrace ();
            }
        }
    }

    /**
     *  Initialise the hash map.
     */
        private void
    initMapping ()
    {
        attributes = new HashMap <String, String> ();
        keys = attributes.keySet ();
    }

    /**
     *  Return a list of all the attribute names, as an array of strings.
     */
        public String []
    getAttributeNames ()
    {
        return (String []) keys.toArray ();
    }

    /**
     *  Get the value for a given attribute of an item. If there is no
     *  attribute that matches the name, this method will return an empty
     *  string.
     */
        public String
    getValue (String paramName)
    {
        String value = attributes.get (paramName);

        if (value == null)
            value = "";

        return value;
    }

    /**
     *  Convert the item to a URL encoded string. This method returns the
     *  URL encoded string, using the UTF-8 charset.
     */
        public String
    toUrlEncoded ()
    {
        String encoded = "", value;

        // step through all the valid names in the hash map.
        for (String name : (String []) keys.toArray ())
        {
            value = attributes.get (name);

            try
            {
                name = URLEncoder.encode (name, "UTF-8");
                value = URLEncoder.encode (value, "UTF-8");

                // if there are other pairs before this one, add an
                // ampersand to separate the pairs.
                if (encoded.equals ("") != true)
                    encoded += "&";

                encoded += name + "=" + value;
            }
            catch (UnsupportedEncodingException e)
            {
                System.err.println ("Error: I need UTF-8!");
                e.printStackTrace ();
            }
        }

        return encoded;
    }
}

// vim: ts=4 sw=4 et
