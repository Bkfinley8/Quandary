package ast;

public class StatementList extends Statement{
    //final List<Statement> statements;

    Statement statement;
    StatementList nextList;
/*
    public StatementList(List<Statement> statements, Location loc) {
        super(loc);
        this.statements = statements;
    }
*/

    public StatementList(Statement stmt, StatementList stmtList, Location loc){
        super(loc);
        this.nextList = stmtList;
        this.statement = stmt;
    }

    /*
    public int getSize(){
        return this.statements.size();
    }
    */

    /* 
    public Statement at(int idx){
        return this.statements.get(idx);
    }
    */

    public Statement getStatement(){
        return this.statement;
    }

    public StatementList getNextStatement(){
        return this.nextList;
    }
    
}
