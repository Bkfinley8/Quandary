package ast;

public class NilExpr extends Expr {
    final Object value;

    public NilExpr() {
        super(null);
        this.value = null;
    }

    public Object getValue() {
        return this.value;
    }
}
