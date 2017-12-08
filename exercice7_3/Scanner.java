package exercice7_3;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Un scanner pour l'analyse lexicale.
 *
 * @author <A HREF="mailto:etienne.payet@univ-reunion.fr">Etienne Payet</A>
 */
public class Scanner {
	/**
	 * La table des symboles dans laquelle stocker/lire
	 * des informations.
	 */
	private final Symbtab symbtab;

	/**
	 * Le fichier dans lequel se trouve le code source.
	 */
	private final BufferedReader fic;

	/**
	 * Le numéro de la ligne que ce scanner est en train
	 * de lire dans le code source.
	 */
	private int lineno;

	/**
	 * L'état initial de l'automate que ce scanner
	 * est en train de simuler.
	 */
	private int start;

	public Scanner(Symbtab symbtab, BufferedReader fic) {
		this.symbtab = symbtab;
		this.fic = fic;
		this.lineno = 1;
	}

	public int getLineno() {
		return this.lineno;
	}
	
	/**
	 * Branchement sur l'état initial du prochain automate.
	 */
	private int fail() {
		if (this.start == 0) this.start = 3;
		else if (this.start == 3) this.start = 6;
		else if (this.start == 6) this.start = 11;
		else if (this.start == 11) this.start = 13;
		else if (this.start == 13)
			throw new RuntimeException("erreur lexicale ligne " + this.lineno);
		else
			throw new RuntimeException("erreur du scanner ligne " + this.lineno);
		return this.start;
	}

	/**
	 * Simule les mouvements des automates.
	 * 
	 * @throws IOException si une erreur de lecture dans le
	 * fichier source survient
	 */
	public Pair nexttoken() throws IOException {
		this.start = 0;
		int state = 0;
		StringBuilder lexbuf = new StringBuilder();
		int c = -1;

		while(true) {
			/* Espaces */
			if (state == 0) {
				// lecture d'un caractère dans fic
				c = this.fic.read();
				if (c == -1)
					// on est arrivé à la fin de fic
					return new Pair(Token.EOF, null);
				else if (c == ' ' || c == '\t') {
					// on ignore les espaces et tabulations
				} 
				else if (c == '\n') this.lineno++;
				else if (c == '#')
					// lecture d'un commentaire
					this.fic.readLine();
				else state = this.fail();
			}
			/* id */
			else if (state == 3) {
				if (Character.isLetter(c)) {
					lexbuf.append((char) c);
					state = 4;
				}
				else state = this.fail();
			}
			else if (state == 4) {
				this.fic.mark(1);
				c = this.fic.read();
				if (Character.isLetterOrDigit(c)) lexbuf.append((char) c);
				else state = 5;
			}
			else if (state == 5) {
				// recule la tete de lecture
				this.fic.reset();
				int p = this.symbtab.insert(lexbuf.toString(), Token.ID);
				return new Pair(this.symbtab.getToken(p), new Integer(p));
			}
			/* num */
			else if (state == 6) {
				if (Character.isDigit(c)) {
					lexbuf.append((char) c);
					state = 7;
				}
				else state = this.fail();
			}
			else if (state == 7) {
				this.fic.mark(1);
				c = this.fic.read();
				if (Character.isDigit(c)) lexbuf.append((char) c);
				else if (c == '.') state = 8;
				else state = 10;
			}
			else if (state == 8) {
				c = this.fic.read();
				if (Character.isDigit(c)) {
					lexbuf.append('.');
					lexbuf.append((char) c);
					state = 9;
				}
				else
					throw new RuntimeException("erreur lexicale ligne " + this.lineno);
			}
			else if (state == 9) {
				this.fic.mark(1);
				c = this.fic.read();
				if (Character.isDigit(c)) lexbuf.append((char) c);
				else state = 10;
			}
			else if (state == 10) {
				this.fic.reset();
				return new Pair(Token.NUM, lexbuf.toString());
			}
			/* opérateur := */
			else if (state == 11) {
				if (c == ':') state = 12;
				else state = this.fail();
			}
			else if (state == 12) {
				c = this.fic.read();
				if (c == '=') return new Pair(Token.ASSIGNOP, null);
				else if (c == '<') {
					int d=this.fic.read();
					if (d == '=') return new Pair(Token.LE, null);
					return new Pair(Token.LT, null);
				}
				else if (c == '>'){
					int d=this.fic.read();
					if (d == '=') return new Pair(Token.GE, null);
					return new Pair(Token.GT, null);
				} 
				if (c == '=') return new Pair(Token.ASSIGNOP, null);
				else
					throw new RuntimeException("erreur lexicale ligne " + this.lineno);
			}
			/* autre */
			else if (state == 13) {
				if (c == ';' || c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')')
					return new Pair(String.valueOf((char) c), null);
				else state = this.fail();
			}
			/* erreur */
			else
				throw new RuntimeException("erreur du scanner ligne " + this.lineno);
		}
	}
}
