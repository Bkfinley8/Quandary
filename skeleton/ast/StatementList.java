package ast;

public class StatementList extends Statement{

    Statement statement;
    StatementList nextList;

    public StatementList(Statement stmt, StatementList stmtList, Location loc){
        super(loc);
        this.nextList = stmtList;
        this.statement = stmt;
    }

    public Statement getStatement(){
        return this.statement;
    }

    public StatementList getNextStatement(){
        return this.nextList;
    }

    public boolean isEmpty(){
        return this.statement == null;
    }
    
    public boolean hasNext(){
        return this.nextList != null;
    }
}
