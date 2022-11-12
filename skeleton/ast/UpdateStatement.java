package ast;

public class UpdateStatement extends Statement {
    Expr expr;
    String ident;
    public UpdateStatement(String ident,Expr expr, Location loc){
        super(loc);
        this.ident = ident;
        this.expr = expr;
    }

    public String getIdent(){
        return this.ident;
    }

    public Expr getExpr(){
        return this.expr;
    }
}
