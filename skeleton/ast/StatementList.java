package ast;

import java.util.List;

public class StatementList extends Statement{
    final List<Statement> statements;

    public StatementList(List<Statement> statements, Location loc) {
        super(loc);
        this.statements = statements;
    }

    public int getSize(){
        return this.statements.size();
    }

    public Statement at(int idx){
        return this.statements.get(idx);
    }
    
}
