package ast;

public class EvalAndOr extends Condition{
    public static final int BOOL_AND = 1;
    public static final int BOOL_OR = 2;

    final Condition condition1;
    final int operator;
    final Condition condition2;

    public EvalAndOr(Condition cond1, int operator, Condition cond2, Location loc) {
        super(loc);
        this.condition1 = cond1;
        this.operator = operator;
        this.condition2 = cond2;
    }

    public Condition getCondition1(){
        return this.condition1;
    }

    public Condition getCondition2(){
        return this.condition2;
    }

    public int getOperator(){
        return this.operator;
    }
}
