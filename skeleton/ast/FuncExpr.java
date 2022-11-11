package ast;

public class FuncExpr extends Expr {
    
    ExprList expr;
    String ident;
    public FuncExpr(String ident,ExprList expr, Location loc){
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
