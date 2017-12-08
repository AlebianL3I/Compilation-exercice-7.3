package exercice7_3;
import java.io.IOException;

import exercice7_3.Token;

/**
 * Un parser pour l'analyse syntaxique.
 *
 * P -> program id D begin L end . eof
 * D -> var D' | epsilon 
 * D'-> id : Y ; D'| epsilon 
 * Y -> int | float
 * L -> I ; L | epsilon
 * I -> id := E | print(E)
 * E -> T + E | T - E | T
 * T -> F * T | F / T | F mod T | F
 * F -> (E) | id | num
 *
 * @author <A HREF="mailto:etienne.payet@univ-reunion.fr">Etienne Payet</A>
 */
public class Parser {

	/**
	 * Le scanner servant à lire les tokens.
	 */
	private final Scanner scanner;

	/**
	 * La table des symboles dans laquelle stocker/lire
	 * des informations.
	 */
	private final Symbtab symbtab;
	
	/**
	 * Le dernier couple (toke, attribut) renvoyé
	 * par le scanner.
	 */
	private Pair lookahead;

	private Icode icode;

	public Parser(Scanner scanner, Symbtab symbtab) {
		this.scanner = scanner;
		this.symbtab = symbtab;
	}
	
	/**
	 * Lance l'analyse syntaxique.
	 * 
	 * @throws IOException si une erreur d'entrée/sortie
	 * se produit
	 */
	public void parse() throws IOException {
		this.lookahead = this.scanner.nexttoken();
		this.P();
	}

	private void match(String token) throws IOException {
		if (token.equals(this.lookahead.getToken()))
			this.lookahead = this.scanner.nexttoken();
		else 
			throw new RuntimeException(
					"erreur de syntaxe ligne " + this.scanner.getLineno() +
					", " + token + " attendu");
	}

	private void P() throws IOException{
		// P -> program id D begin L end . eof
		if(Token.PROGRAM.equals(this.lookahead.getToken())) {
			this.match(Token.PROGRAM);
			this.match(Token.ID);
			this.D();
			this.match(Token.BEGIN);
			this.L();
			this.match(Token.END);
			this.match(".");
			this.match(Token.EOF);
			this.icode.emit(new Quadruple(Token.HALT,null,null,null));
		}
	}
	
	private void D() throws IOException{
		// D -> var D' | epsilon 
		if(Token.VAR.equals(this.lookahead.getToken())) {
			this.match(Token.VAR);
			this.D0();
		}
		else {
			this.icode.emit(new Quadruple(Token.HALT,null,null,null));
		}
	}
	
	private void D0() throws IOException{
		//D'-> id : Y ; D'| epsilon
		if(Token.ID.equals(this.lookahead.getToken())) {
			this.match(Token.ID);
			this.match(":");
			this.Y();
			this.match(";");
			this.D0();
		}
		else {
			this.icode.emit(new Quadruple(Token.HALT,null,null,null));
		}
	}
	
	private void Y() throws IOException{
		//Y -> int | float
		if(Token.INT.equals(this.lookahead.getToken())) {
			this.match(Token.INT);
		}
		else {
			this.match(Token.FLOAT);
		}
	}
	
	private void  L() throws IOException {
		// L -> I ; L | eof
		if (Token.ID.equals(this.lookahead.getToken())) {
			this.I();
			this.match(";");
			this.L();
		}
		else
			this.icode.emit(new Quadruple(Token.HALT,null,null,null));
	}
	
	private void  I() throws IOException {
		//  I -> id := E | print(E)
		String token=this.lookahead.getToken();
		if(Token.ID.equals(token)==false) {
			int id_adr = (Integer) this.lookahead.getAttribut();
			String id_type= this.symbtab.getType(id_adr);
			if(id_type==null) 
				throw new RuntimeException(
						"erreur ligne " + this.scanner.getLineno() +
						", variable" + this.symbtab.getLexeme(id_adr) + " non déclarée");
			this.match(Token.ID);
			this.match(Token.ASSIGNOP);
			int E_val = this.E();
			if(id_type.equals(this.symbtab.getType(E_val)))
				this.icode.emit(new Quadruple(Token.ASSIGNOP,this.symbtab.getAdresse(E_val) ,null,this.symbtab.getAdresse(id_adr)));
			else
				throw new RuntimeException("erreur de typage ligne " + this.scanner.getLineno());
		}
		else if(Token.PRINT.equals(token)) {
			this.match(Token.PRINT);
			this.match("(");
			int E_val=this.E();
			this.match(")");
			icode.emit(new Quadruple(Token.PRINT,null,null,E_val));
		}
	}

	private int E() throws IOException {
		// E -> T + E1  | T - E1 | T 
		int T_val = this.T();
		int E_val = T_val;
		String token = this.lookahead.getToken();
		if ("+".equals(token)) {
			this.match("+");
			E_val = this.E();
			//int id_adr = (Integer) this.lookahead.getAttribut();
		}
		else if ("-".equals(token)) {
			this.match("-");
			E_val = this.E();
			//int id_adr = (Integer) this.lookahead.getAttribut();
		}
		icode.emit(new Quadruple(token,T_val,E_val,E_val));
		return E_val;
	}
	
	
	private int T() throws IOException {
		// T -> F * T | F / T | F mod T | F 
		int F_val = this.F();
		int T_val = F_val;
		String token = this.lookahead.getToken();
		if ("*".equals(token)) {
			this.match("*");
			T_val = this.T();
		}
		else if ("/".equals(token)) {
			this.match("/");
			T_val /= this.T();
		}
		else if (Token.MOD.equals(token)) {
			this.match(Token.MOD);
		}
		icode.emit(new Quadruple(token,T_val,F_val,T_val));
		return T_val;
	}

	private int F() throws IOException {
		// F -> (E) | num | id }
		int F_val = 0;
		String token = this.lookahead.getToken();
		if ("(".equals(token)) {
			this.match("(");
			F_val = this.E();
			this.match(")");
		}
		else if (Token.NUM.equals(token)) {
			String lexeme = (String) this.lookahead.getAttribut();
			F_val = (int) Float.valueOf(lexeme).floatValue();
			this.match(Token.NUM);
		}
		else if (Token.ID.equals(token)) {
			int id_adr = (Integer) this.lookahead.getAttribut();
			int val = (int) this.symbtab.getValue(id_adr);
			if (val == -1)
				throw new RuntimeException(
						"erreur ligne " + this.scanner.getLineno() +
						", la variable " + this.symbtab.getLexeme(id_adr) + " n'a pas de valeur");
			this.match(Token.ID);
			F_val = val; 
		}
		else
			throw new RuntimeException(
					"erreur de syntaxe ligne " + this.scanner.getLineno() +
					", '(', NUM ou ID attendu");
		
		return F_val;
	}
	
}
