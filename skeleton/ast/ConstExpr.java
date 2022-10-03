package ast;

public class ConstExpr extends Expr {

    final Object value;

    public ConstExpr(long value, Location loc) {
        super(loc);
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
