package exercice7_3;
import java.util.ArrayList;

/**
 * Table des symboles.
 *
 * @author <A HREF="mailto:etienne.payet@univ-reunion.fr">Etienne Payet</A>
 */
public class Symbtab {
	/**
	 * Les lignes de cette table.
	 */
	private final ArrayList<Entry> entries = new ArrayList<Entry>();
	
	
	// Constructeur dans lequel on ajoute tout les mots-clés : 
	
	public Symbtab() 
	{
		this.entries.add(new Entry("program",Token.PROGRAM));
		this.entries.add(new Entry("begin", Token.BEGIN));
		this.entries.add(new Entry("end",Token.END));
		this.entries.add(new Entry("print",Token.PRINT));
		this.entries.add(new Entry("var",Token.VAR));
		
		this.entries.add(new Entry("mod",Token.MOD));
		this.entries.add(new Entry("eof",Token.EOF));
		this.entries.add(new Entry("num",Token.NUM));
		this.entries.add(new Entry("id",Token.ID));
	}


	/**
	 * Recherche le lex\`eme donn\'e dans cette table.
	 *
	 * @return le num\'ero de ligne o\`u est stock\'e le lex\`eme
	 * si celui-ci est dans cette table et -1 sinon
	 */
	private int lookup(String lexeme) {
		int p = 0;
		for(Entry e : this.entries)
			if (e.lexeme.equals(lexeme)) return p;
			else p++;
		return -1;
	}

	/**
	 * Ins\`ere les \'el\'ements donn\'es dans cette table si le
	 * lex\`eme donn\'e ne s'y trouve pas d\'ej\`a.
	 *
	 * @return le num\'ero de ligne o\`u est stock\'e le lex\`eme
	 * donn\'e si celui-ci est d\'ej\`a dans cette table, ou le
	 * num\'ero de ligne o\`u les \'el\'ements ont Ã©tÃ© ins\'er\'es
	 * si le lex\`eme n'est pas d\'ej\`a dans cette table
	 */
	public int insert(String lexeme, String token) {
		int p = this.lookup(lexeme);
		if (p >= 0) return p; // le lex\`eme est d\'ej\`a dans la table

		// ici, le lex\`eme n'est pas d\'ej\`a dans la table : on l'ins\`ere
		this.entries.add(new Entry(lexeme, token));
		return this.entries.size() - 1;
	}
	
	public int newId()
	{
		this.entries.add(new Entry(null,Token.ID)); 
		return this.entries.size() - 1;
	}


	/**
	 * Renvoie le lex\`eme stock\'e \`a la ligne donn\'ee.
	 */
	public String getLexeme(int p) {
		return this.entries.get(p).lexeme;
	}

	/**
	 * Renvoie le token stock\'e \`a la ligne donn\'ee.
	 */
	public String getToken(int p) {
		return this.entries.get(p).token;
	}
	

	/**
	 * Renvoie la valeur stockée à la ligne donnée.
	 */
	public Number getValue(int p) {
		return this.entries.get(p).value;
	}
	
	/**
	 * Stocke la valeur donnée à la ligne donnée.
	 */
	public void setValue(int p, int val) {
		this.entries.get(p).value = val;
	}
	
	public String getType(int p) {
		  return this.entries.get(p).type;
	}
		 
	public void setType(int p, String type) {
		  this.entries.get(p).type = type;
	}
	
	
	public int getAdresse(int p) {
		  return this.entries.get(p).adresse;
	}
	
		 
	 public void setAdresse(int p, int adresse) {
		 this.entries.get(p).adresse = adresse;
	}

	/**
	 * Renvoie une représentation de cette table sous
	 * forme de chaîne de caractères.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder("@\tlexème\ttoken\tvaleur\n");
		sb.append("--------------------------\n");
		int p = 0;
		for(Entry e : this.entries) {
			sb.append(p);
			sb.append('\t');
			sb.append(e.toString());
			sb.append('\n');
			p++;
		}
		return sb.toString();
	}




}


