/*
* ---------------------- CloudFlix: Movie collaborative feature-set generation code ------------------------
*        Author: Saurav Majumder
*        Date: Nov 14, 2013
*        Description: This is the main class to create the final set of features for the 
*        collaborative movie classification.
*        @param Input files: rating.dat.
* -----------------------------------------------------------------------------------------------------------
*/

package dataPreprocessing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class collaborativeFeatureSet {
	public collaborativeFeatureSet(String ratingFile, float threshold) throws IOException {
		InputStream    	fis;
		BufferedReader 	br;
		String         	line;
		String[] 		values;
		PrintWriter 	writer = null;
		
		fis = new FileInputStream(ratingFile);
		writer = new PrintWriter("collaborativeFeatureSet.csv", "UTF-8");
		br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		
		while ((line = br.readLine()) != null) {
			values = line.split(",");
			System.out.println(values[0]);
			
			/* If the rating is greater than the specified threshold then consider as liked */
			if(Float.parseFloat(values[2]) >= threshold) {
				writer.println(values[0]+", "+values[1]);
			}
		}
		writer.close();
		br.close();
	}
	
	public static void main(String[] args) {
		try {
			collaborativeFeatureSet test = new collaborativeFeatureSet("ratingsForMahout.csv", 3);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
