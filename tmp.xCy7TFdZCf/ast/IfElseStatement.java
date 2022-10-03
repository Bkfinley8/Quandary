package ast;

public class IfElseStatement extends Statement {

    final Condition condition;
    final Statement ifstatement;
    final Statement elseStatement;

    public IfElseStatement(Condition cond, Statement ifstatement, Statement elseStatement, Location loc) {
        super(loc);
        this.condition = cond;
        this.ifstatement = ifstatement;
        this.elseStatement = elseStatement;
    }

    public Condition getCondition(){
        return this.condition;
    }

    public Statement getIfStatement(){
        return this.ifstatement;
    }

    public Statement getElseStatement(){
        return this.elseStatement;
    }
}
