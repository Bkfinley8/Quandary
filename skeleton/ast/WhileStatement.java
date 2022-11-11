package ast;

public class WhileStatement extends Statement{
    final Condition condition;
    final Statement statement;

    public WhileStatement(Condition cond, Statement statement, Location loc) {
        super(loc);
        this.condition = cond;
        this.statement = statement;
    }

    public Condition getCondition(){
        return this.condition;
    }

    public Statement getStatement(){
        return this.statement;
    }
}
