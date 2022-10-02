package ast;

public class NotOP extends Condition {
    public static final int BOOL_NOT = 1;

    final int operator;
    final Condition cond;

    public NotOP(int operator, Condition cond, Location loc) {
        super(loc);
        this.operator = operator;
        this.cond = cond;
    }

}
