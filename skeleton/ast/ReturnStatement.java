package ast;

public class ReturnStatement extends Statement{
    final Expr expr;
    
    public ReturnStatement(Expr expr, Location loc) {
        super(loc);
        this.expr = expr;
    }
}
