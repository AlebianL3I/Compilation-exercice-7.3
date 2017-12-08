package exercice7_3;
/**
 * Une ligne de la table des symboles.
 *
 * @author <A HREF="mailto:etienne.payet@univ-reunion.fr">Etienne Payet</A>
 */
public class Entry {
	public String lexeme;
	public String token;
	public String type;
	public Number value;
	public int adresse;

	public Entry (String lexeme, String token) {
		this.lexeme = lexeme;
		this.token  = token;
	}

	/**
	 * Renvoie une représentation de cette ligne
	 * sous forme de chaîne de caractères.
	 */
	public String toString() {
		return this.lexeme + "\t" + this.token;
	}
}
