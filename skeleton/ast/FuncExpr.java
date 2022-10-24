package ast;

public class FuncExpr extends Expr {
    
    Expr expr;
    String ident;
    public FuncExpr(String ident,Expr expr, Location loc){
        super(loc);
        this.ident = ident;
        this.expr = expr;
    }
}
