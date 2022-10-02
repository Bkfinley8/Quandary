package ast;

public class AndOr extends Condition{
    public static final int BOOL_AND = 7;
    public static final int BOOL_OR = 8;

    final Condition expr1;
    final int operator;
    final Condition expr2;

    public AndOr(Condition expr1, int operator, Condition expr2, Location loc) {
        super(loc);
        this.expr1 = expr1;
        this.operator = operator;
        this.expr2 = expr2;
    }
}
