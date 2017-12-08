package exercice7_3;

public class Quadruple
{
	private String op;
	private Integer arg1, arg2, result;
	
	public Quadruple(String op, Integer arg1, Integer arg2, Integer result)
	{
		this.op = op;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.result = result;
	}
	
	// Accesseurs : 
	
	public String getOp() {return this.op; }
	public void setOp(String op) {this.op = op;}
	
	public Integer getArg1() { return this.arg1; }
	public void setArg1(Integer arg1) { this.arg1 = arg1; }
	
	public Integer getArg2() { return this.arg2; }
	public void setArg2(Integer arg2) { this.arg2 = arg2; }
	
	public Integer getResult() { return this.result; }
	public void setResult(Integer result) { this.result = result; }
	
	public String toString()
	{
		String res = null;
		
		if(Token.ASSIGNOP.equals(this.op)) // (:=, arg1, _, result)
		{
			res = "@" + this.result + " := @" + this.arg1;
			
		}
		
		else if ("+".equals(this.op) || "-".equals(this.op) || "*".equals(this.op) || "/".equals(this.op) || Token.MOD.equals(this.op)) 
		{
			res = "@" + this.result + " := @" + this.arg1 + " " + this.op + " @" + this.arg2; 
		}
		
		else if (Token.GOTO.equals(this.op))
		{
			res = "goto " + this.op;
		}
		
		else if (Token.GT.equals(this.op))
		{
			res = "if @" + this.arg1 + this.op + " @" + this.arg2 + " goto " + this.result;
		}
		
		else if (Token.PRINT.equals(this.op))
		{
			res = "print @" + this.result;
		}
			
		else res = "Erreur, quadruplet inconnu";
		
		return res;
	}
}