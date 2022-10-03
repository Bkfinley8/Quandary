package ast;

public class PrintStatement extends Statement{
    final Expr expr;
    
    public PrintStatement(Expr expr, Location loc) {
        super(loc);
        this.expr = expr;
    }

    public Expr getExpr(){
        return this.expr;
    }
}
