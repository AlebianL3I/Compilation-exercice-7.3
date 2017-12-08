package exercice7_3;
/**
 * Un couple (token, attribut) renvoyé par le scanner.
 *
 * @author <A HREF="mailto:etienne.payet@univ-reunion.fr">Etienne Payet</A>
 */
public class Pair {
	private final String token;
	private final Object attribut;

	public Pair(String token, Object attribut) {
		this.token = token;
		this.attribut = attribut;
	}

	public String getToken() {
		return this.token;
	}

	public Object getAttribut() {
		return this.attribut;
	}

	/**
	 * Renvoie une représentation de ce couple
	 * sous forme de chaine de caractères.
	 */
	public String toString() {
		return "(" + this.token + ", " + this.attribut + ")";
	}
}
