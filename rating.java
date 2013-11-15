/*
* ---------------------- CloudFlix: Class for rating provided by a user to a movie ------------------------
*        Author: Saurav Majumder
*        Date: Nov 14, 2013
*        Description: This is the class containing all the feature of a movie.
*        @param Input files: rating.dat.
* ---------------------------------------------------------------------------------------------------------
*/
public class rating {
	protected int userId;
	protected int mId;
	protected float rating;
	
	public rating(int userId, int mId, float rating) {
		this.userId = userId;
		this.mId = mId;
		this.rating = rating;
	}
}
