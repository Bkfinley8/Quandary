package ast;

public class UnaryExpr extends Expr {
    final Expr expr1;

    public UnaryExpr(Expr expr1, Location loc) {
        super(loc);
        this.expr1 = expr1;
    }

    public Expr getExpr() {
        return this.expr1;
    }
    
    @Override
    public String toString() {
        return "-" + expr1;
    }
}
