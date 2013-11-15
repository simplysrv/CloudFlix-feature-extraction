/*
* ---------------------- CloudFlix: Class for handling movie genre ------------------------
*        Author: Saurav Majumder
*        Date: Nov 14, 2013
*        Description: This is the class containing all the genre information related to 
*        the movies.
*        @param Input files: movie genre file.
* -----------------------------------------------------------------------------------
*/
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class movieGenreMain {
	protected HashMap<String,movieGenre>movie_map;
	Set<String>		allGenre;
	
	public newDataset(String genrefile) {
		InputStream    	fis;
		BufferedReader 	br;
		String         	line;
		String[]		features;
		String[]		genre;
		movieGenre		mg;
		
		movie_map = new HashMap<String,movieGenre>(); /* Map to contain all the movies and its genres */
		allGenre = new HashSet<String>(); /* Set to contain all the genres */
		
		try {
			fis = new FileInputStream(genrefile);
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			
			while ((line = br.readLine()) != null) {
				//System.out.println("Line: "+line);
				features = line.split("::");
				if(features.length > 1) {
					mg = new movieGenre(features[0],features[1]);
					genre = features[2].split("\\|");
					for(String s : genre) {
						mg.addGenre(s);
						allGenre.add(s);
				    }
					//System.out.println(mg.movie_id+"::"+mg.movie_name);
					movie_map.put(features[0], mg);
				}
			}
			br.close();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		fis = null;
	}
	
	/* Check if a specified movie belongs to a certain genre */
	public boolean isGenre(String movie_id, String Genre) {
		if(movie_map.containsKey(movie_id)) {
			Set<String> genreList = movie_map.get(movie_id).getAllGenre();
			if(genreList.contains(Genre)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	/* Return set of all genres present in the input file */
	public Set<String> allGenre() {
		return allGenre;
	}
	
	public static void main(String[] args) {
		newDataset nd = new newDataset("moviesraw.dat");
		System.out.println(nd.isGenre("16", "Crime"));
		System.out.println(nd.isGenre("24", "Crime"));
		
		Set<String> gen = nd.allGenre();
		Iterator iterator = gen.iterator(); 
		while (iterator.hasNext()){
	         System.out.println(iterator.next());  
		}
	}
}
