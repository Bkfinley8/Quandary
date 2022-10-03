package ast;

public class AndOr extends Condition{
    public static final int BOOL_AND = 1;
    public static final int BOOL_OR = 2;

    final Condition cond1;
    final int operator;
    final Condition cond2;

    public AndOr(Condition cond1, int operator, Condition cond2, Location loc) {
        super(loc);
        this.cond1 = cond1;
        this.operator = operator;
        this.cond2 = cond2;
    }

    public Condition getCondition1(){
        return this.cond1;
    }

    public Condition getCondition2(){
        return this.cond2;
    }

    public int getOperator(){
        return this.operator;
    }
}
