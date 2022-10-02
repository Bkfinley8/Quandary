package ast;

public class IfElseStatement extends Statement {

    final Condition condition;
    final Statement statement1;
    final Statement elseStatement;

    public IfElseStatement(Condition cond, Statement statement1, Statement elseStatement, Location loc) {
        super(loc);
        this.condition = cond;
        this.statement1 = statement1;
        this.elseStatement = elseStatement;
    }
}
