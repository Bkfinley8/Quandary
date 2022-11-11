package ast;

public class NilExpr extends Expr {
    final Object value;

    public NilExpr(Location loc) {
        super(loc);
        this.value = null;
    }

    public Object getValue() {
        return this.value;
    }
}
