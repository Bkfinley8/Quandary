package ast;

public class CondEval extends Condition {
    public static final int LESS_THAN = 1;
    public static final int LESS_THAN_OR_EQUAL_TO = 2;
    public static final int GREATER_THAN = 3;
    public static final int GREATER_THAN_OR_EQUAL_TO = 4;
    public static final int DOUBLE_EQUALS = 5;
    public static final int NOT_EQUALS = 6;

    final Expr expr1;
    final int operator;
    final Expr expr2;

    public CondEval(Expr expr1, int operator, Expr expr2, Location loc) {
        super(loc);
        this.expr1 = expr1;
        this.operator = operator;
        this.expr2 = expr2;
    }
}
