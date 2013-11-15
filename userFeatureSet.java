/*
* ---------------------- CloudFlix: Movie feature-set generation code ------------------------
*        Author: Saurav Majumder
*        Date: Nov 14, 2013
*        Description: This is the main class to create the final set of features for the 
*        movie classification.
*        @param Input files: movie.dat, rating.dat and genre.dat.
* --------------------------------------------------------------------
*/

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class userFeatureSet {
	/* HashMap to keep feature vector of every movie. <movie-id,feature-set> */
	HashMap<String, movieFeature>movieMap = new HashMap<>();
	
	/* Function read the feature-set for each movie and stores it into the map mentioned above */
	public void readMovieFeatures(String featureFile) throws IOException{
		InputStream    fis;
		BufferedReader br;
		String         line;
		movieFeature   movie; /* Variable of type movieFeature class which is in this package */
		String[] 	   features;
		
		fis = new FileInputStream(featureFile);
		br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		
		/* Read each line in the file and extracts the required features and puts it into the map */
		while ((line = br.readLine()) != null) {
			features = line.split("\t"); /* Split the input file with tab delimiter, <Change if required> */
			/* Create object of the movie class containing all the features of the movie */
		    movie = new movieFeature(features[0], features[1], features[2], features[5], features[6], features[7], features[8], features[9],
		    		 features[10], features[11], features[12], features[13], features[14], features[15], features[16], features[17], features[18],
		    		 features[19], features[21], features[22]);
		    movieMap.put(movie.mId, movie); /* Put into the map */
		}
		
		/*Dereference all pointers */
		br.close();
		br = null;
		fis = null;
	}
	
	/* Function combines the feature values stored in the previous function to create final feature vector for each user */
	public void exportAllMoviesFeaturePerUser(String ratingFile) throws IOException {
		InputStream    	fis;
		BufferedReader 	br;
		String         	line;
		Iterator<String>iterator;
		String 			vector = null;
		
		int i = 0;
		String currentUserId = "-1"; /* Set current user to -1 for starting */
		String[] values;
		String mvclass = "negative";
		PrintWriter writer = null;
		movieFeature mf;
		
		/* Check if the movie features have been extracted or not */
		if(movieMap.size() < 1) return;
		
		fis = new FileInputStream(ratingFile);
		br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
		
		/* Retrive the feature names from the movie map to include in the beginning of the final feature set */
		mf = movieMap.get("id"); /* Search with 'id' since it his the value of the id of that row */
		String attribute = "recId" + ", " + "userId" + ", " + mf.mId + ", " + mf.rtAllCriticsRating + ", " + mf.rtAllCriticsNumReviews + ", " + mf.rtAllCriticsNumFresh + ", " + mf.rtAllCriticsNumRotten
					 + ", " + mf.rtAllCriticsScore + ", " + mf.rtTopCriticsRating + ", " + mf.rtTopCriticsNumReviews + ", " + mf.rtTopCriticsNumFresh
					 + ", " + mf.rtTopCriticsNumRotten + ", " + mf.rtTopCriticsScore + ", " + mf.rtAudienceRating + ", " + mf.rtAudienceNumRatings + ", " + mf.rtAudienceScore
					 + ", ";
		
		/* Extract genre for all the movies and store in the LinkedHashSet */
		newDataset nd = new newDataset("moviesraw.dat");
		
		/* Retrive list of all types of genre included in that file */
		Set<String> gen = nd.allGenre();
		
		/* Iterate through the set to print the name of the genres in the first row of the final list. */
		iterator = gen.iterator(); 
		while (iterator.hasNext()){
			attribute += iterator.next() + ", ";  
		}
		
		/* Add the feature name 'Class' in the end of the first row */
		attribute += "Class";
		
		/* Read each line in the user rating file to retrive user's liked and disliked movies */
		while ((line = br.readLine()) != null) {
			values = line.split(",");
			
			/* If the current user is not same as the previous then start a new file */
			if(!currentUserId.equals(values[0])) { 
				if(!currentUserId.equals("-1")) {
					writer.close();
					System.out.println("Feature set prepared for user:"+currentUserId);
				}
				currentUserId = values[0]; /* Create new file with the user id */
				writer = new PrintWriter("featureVectors/"+currentUserId+".csv", "UTF-8"); /* Output file saved in featureVectors folder */
				writer.println(attribute); /* Write the names of the features */
				i = 1;
			}
			
			/* Adding Movie feature vector to the final list */
			if(movieMap.containsKey(values[1])){
				mf = movieMap.get(values[1]); /* Retrieving feature vector of the specified movie */
				/* Creating the feature vector */
				vector = i++ + ", " + currentUserId + ", " + mf.mId + ", " + mf.rtAllCriticsRating + ", " + mf.rtAllCriticsNumReviews + ", " + mf.rtAllCriticsNumFresh + ", " + mf.rtAllCriticsNumRotten
						 + ", " + mf.rtAllCriticsScore + ", " + mf.rtTopCriticsRating + ", " + mf.rtTopCriticsNumReviews + ", " + mf.rtTopCriticsNumFresh
						 + ", " + mf.rtTopCriticsNumRotten + ", " + mf.rtTopCriticsScore + ", " + mf.rtAudienceRating + ", " + mf.rtAudienceNumRatings + ", " + mf.rtAudienceScore;
			}
			
			/* Adding Movie genre in bag of word format in the final list */
			iterator = gen.iterator(); 
			/* Check for genres which belong to the specified movie */
			while (iterator.hasNext()){
				int checkGenre = nd.isGenre(values[1], iterator.next()) ? 1 : 0; /* Set 1 if the movie belong to that genre or set 0 */
				vector += ", "+ checkGenre;  
			}
			
			/* Adding movie class in the final list */
			float rating = Float.parseFloat(values[2]);
			
			/* If rating is greater than 3 then consider as positive, or else negative */
			if(rating > 3) {
				mvclass = "positive";
			} else {
				mvclass = "negative";
			}
			vector += ", " + mvclass;
			
			writer.println(vector); /* Print feature vector into file. */
		}
		
		writer.close();
		br.close();
		br = null;
		fis = null;
	}
	public static void main(String[] args) {
		userFeatureSet user = new userFeatureSet();
		try {
			user.readMovieFeatures("movie.dat");
			user.exportAllMoviesFeaturePerUser("ratingsForMahout.csv");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
}
