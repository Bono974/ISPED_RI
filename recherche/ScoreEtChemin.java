package projet.recherche;

@SuppressWarnings("rawtypes")
public class ScoreEtChemin implements java.lang.Comparable {

	/**
	 * Chemin du document
	 * @see ScoreEtChemin#getChemin()
	 * @see ScoreEtChemin#setChemin(String)
	 */
	private String chemin ;
	
	/**
	 * Score du document
	 * @see ScoreEtChemin#getScore()
	 * @see ScoreEtChemin#setScore(int)
	 */
	private float score ;

	/**
	 * Constructeur de la classe. Ce constructeur ne prend que le chemin en entrée.
	 * @param s Chemin du document
	 * @see ScoreEtChemin#chemin
	 * @see ScoreEtChemin#score
	 */
	public ScoreEtChemin(String s)
	{
	     this.chemin = s ;
	     this.score = 0 ;
	}
	
	/**
	 * Constructeur de la classe. Ce constructeur ne prend que le score en entrée.
	 * @param i Score du document
	 * @see ScoreEtChemin#chemin
	 * @see ScoreEtChemin#score
	 */
	public ScoreEtChemin(float i)
	{
	     this.chemin = null ;
	     this.score = i ;
	}
	
	/**
	 * Constructeur de la classe. Ce constructeur prend le score et le chemin en entrée.
	 * @param s Chemin du document
	 * @param i Score du document
	 * @see ScoreEtChemin#chemin
	 * @see ScoreEtChemin#score
	 */
	public ScoreEtChemin(float i, String s)
	{
	     this.chemin = s ;
	     this.score = i ;
	}
	
	/**
	 * Getter de l'attribut score
	 * @see ScoreEtChemin#score
	 * @return score
	 */
	public float getScore()
	{
	    return score ;
	}
	
	/**
	 * Setter de l'attribut Score
	 * @see ScoreEtChemin#score
	 * @param i le score
	 */
	public void setScore(float i)
	{
	    this.score = i ;
	    this.chemin = null ;
	}
	
	/**
	 * Getter de l'attribut chemin
	 * @see ScoreEtChemin#chemin
	 * @return chemin
	 */
	public String getChemin()
	{
	    return chemin ;
	}
	
	/**
	 * Settter de l'attribut chemin
	 * @see ScoreEtChemin#chemin
	 * @param s le chemin
	 */
	public void setChemin(String s)
	{
	    this.chemin = s ;
	    this.score = 0 ;
	}
	
	/**
	 * Cette méthode est une surchage de la méthode compareTo de Component
	 * Elle permet de comparer les objets ScoreEtChemin afin de les trier
	 * La comparaison s'effectue sur le score
	 * @see Componant
	 */
	public int compareTo(Object autre) {
		float scoreAutre = ((ScoreEtChemin) autre).getScore();
		float monScore = this.getScore();
		if (scoreAutre > monScore) return -1; 
	    else 
	    	if(scoreAutre == monScore) return 0; 
	    	else return 1; 
	}

	/**
	 * Cette méthode est une réécriture de la méthode equals(). Le test s'effectue sur le chemin
	 * 
	 */
	public boolean equals(ScoreEtChemin sc) {
		if (this.getChemin().equals(sc.getChemin())) {
			return true;
		}
		else
			return false;
		
		
	}
	
}

