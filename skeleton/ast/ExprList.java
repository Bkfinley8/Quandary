package ast;

public class ExprList extends ASTNode {
    NEmptyExprList list;

    public ExprList(NEmptyExprList lists, Location loc){
        super(loc);
        this.list = lists;
    }

    public NEmptyExprList getList(){
        return this.list;
    }

    public boolean isEmpty(){
        return list == null;
    }
}
