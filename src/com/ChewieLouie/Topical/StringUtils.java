package com.ChewieLouie.Topical;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

	public static List<String> split( String input, String seperator ) {
		List<String> splitInput = new ArrayList<String>();
		if( input.isEmpty() == false && seperator.isEmpty() == false ) {
			int startOfToken = 0;
			int endOfToken = input.indexOf( seperator );
			while( endOfToken != -1 ) {
				splitInput.add( input.substring( startOfToken, endOfToken ) );
				startOfToken = endOfToken + seperator.length();
				endOfToken = input.indexOf( seperator, startOfToken );
			}
			splitInput.add( input.substring( startOfToken ) );
		}
		return splitInput;
	}

	public static String join( String separator, String[] strings ) {
		if( strings.length == 0 || separator.isEmpty() )
			return "";
		StringBuilder out = new StringBuilder();
		out.append( strings[0] );
		for( int x = 1; x < strings.length; ++x )
			out.append( separator ).append( strings[x] );
		return out.toString();
	}
}
