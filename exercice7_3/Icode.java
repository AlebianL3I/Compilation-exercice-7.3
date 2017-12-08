package exercice7_3;

public class Icode
{
	private String op;
	private Integer arg1,arg2,result;
	
	public void emit(Quadruple q)
	{
		switch (q.getOp())
		{
			// Erreur (si pas d'opérateur)
			case "null":
				System.out.println("Erreur");
				break;
			
			// Operator : 
			case Token.PLUS:
			case Token.MOINS:
			case Token.MULT:
			case Token.DIV:
				System.out.println(q.getResult()+" "+q.getArg1()+" "+q.getOp()+" "+q.getArg2());
				break;
			
			case Token.ASSIGNOP:
				System.out.println(q.getResult()+" "+q.getOp() +" "+q.getArg1());
				break;
				
			// RELOP : 
			case Token.GT:	
			case Token.GE:			
			case Token.LT:
			case Token.LE:
				System.out.println("if "+q.getArg1()+" "+q.getOp()+" "+q.getArg2()+" "+Token.GOTO+" "+q.getResult());
				break;
				
			default : 
				System.out.println(q.getOp()+" "+q.getResult());
				break;

		}
			
		
	}

	public void run()
	{
		Symbtab symbtab = new Symbtab();
		int index = 0;
		Number val;
		Quadruple q = new Quadruple(op,arg1,arg2,result);
		
		while(true)
		{
			if (q.getOp() == Token.PROGRAM)
			{
				System.out.println("program " + symbtab.getValue(q.getResult()) );
				index += 1;
			}
			
			else if (q.getOp() == Token.BEGIN)
			{
				System.out.println("begin");
				index += 1;
			}
			
			else if (q.getOp() == Token.END)
			{
				System.out.println("end");
				index += 1;
			}
			
			else if (q.getOp() == Token.PRINT)
			{
				System.out.println(symbtab.getValue(q.getResult()));
				index += 1;
			}
			
			else if (q.getOp() == Token.ASSIGNOP) 
			{
				val = symbtab.getAdresse(q.getArg1());
				
				if (val == null) { throw new RuntimeException("Expression sans valeur !"); }
			}
						
			else if ("<".equals(q.getOp())  ||
					 "<=".equals(q.getOp()) ||
					 ">".equals(q.getOp())  ||
					 ">=".equals(q.getOp())
					)
			{
				this.arg1 = (Integer)symbtab.getAdresse(q.getArg1());
				this.arg2 = (Integer)symbtab.getAdresse(q.getArg2());
				
				if (arg1 == null || arg2 == null) {throw new RuntimeException("Comparaison san valeur ! "); }
				
				else if (q.getOp() == "<" ) if (arg1 < arg2) { break; }  
				else if (q.getOp() == "<=")  if (arg1 <= arg2) { break; }
				else if (q.getOp() == ">") if(arg1 > arg2) { break; }
				else if (q.getOp() == ">=") if(arg1 >= arg2) { break; }
				else index += 1;
			}
		
			else if ("+".equals(q.getOp()) || 
					 "-".equals(q.getOp()) || 
					 "*".equals(q.getOp()) || 
					 "/".equals(q.getOp()) ||
					 "mod".equals(q.getOp())
					)
			{
				this.arg1 = (Integer)symbtab.getAdresse(q.getArg1());
				this.arg2 = (Integer)symbtab.getAdresse(q.getArg2());
				
				if (arg1 == null || arg2 == null) { throw new RuntimeException("Expression sans valeur ! "); }
				
				else if (q.getOp() == "+") { symbtab.setAdresse(q.getResult(), arg1+arg2); }
				else if (q.getOp() == "-") { symbtab.setAdresse(q.getResult(), arg1-arg2); }
				else if (q.getOp() == "*") { symbtab.setAdresse(q.getResult(), arg1*arg2); }
				else if (q.getOp() == "/") { symbtab.setAdresse(q.getResult(), arg1/arg2); }
				else if (q.getOp() == "mod") {symbtab.setAdresse(q.getResult(), arg1%arg2); }
				else index += 1;	
				
			}
			
			else if ("halt".equals(q.getOp())) break;
			
		}
		
	}
}