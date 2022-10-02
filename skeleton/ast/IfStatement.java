package ast;

public class IfStatement extends Statement{
    final Condition condition;
    final Statement statement;

    public IfStatement(Condition cond, Statement statement, Location loc) {
        super(loc);
        this.condition = cond;
        this.statement = statement;
    }
}
