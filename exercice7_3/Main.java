package exercice7_3;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Programme principal. Lance le parser sur le fichier passé en paramètre.
 *
 * @author <A HREF="mailto:etienne.payet@univ-reunion.fr">Etienne Payet</A>
 */
public class Main {
	public static void main(String args[]) throws IOException {
		if (args.length != 1)
			System.out.println("usage : java Main <file>\n");
		else {
			BufferedReader fic = null;
			try {
				fic = new BufferedReader(new FileReader(args[0]));
				Symbtab symbtab = new Symbtab();
				Scanner scanner = new Scanner(symbtab, fic);
				Parser parser = new Parser(scanner, symbtab);
				parser.parse();
			} finally {
				if (fic != null) fic.close();
			}
		}
	}
}
