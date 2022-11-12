package ast;

public class CallStatement extends Statement {
    ExprList expr;
    String ident;
    public CallStatement(String ident,ExprList expr, Location loc){
        super(loc);
        this.ident = ident;
        this.expr = expr;
    }

    public String getIdent(){
        return this.ident;
    }

    public ExprList getExprList(){
        return this.expr;
    }
}
