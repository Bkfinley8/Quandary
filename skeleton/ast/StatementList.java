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
        return statement;
    }

    public StatementList getNextStatement(){
        return nextList;
    }

    public boolean isEmpty(){
        return statement == null;
    }
    
    public boolean hasNext(){
        return nextList != null;
    }
}
