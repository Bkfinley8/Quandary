package ast;

public class NEmptyExprList extends Expr{
    Expr first;
    NEmptyExprList rest;

    public NEmptyExprList(Expr first, NEmptyExprList rest, Location loc){
        super(loc);
        this.first = first;
        this.rest = rest;
    }

    public Expr getFirst(){
        return this.first;
    }

    public NEmptyExprList getRest(){
        return this.rest;
    }

    public boolean isEmpty(){
        return first == null;
    }
    
    public boolean hasNext(){
        return rest != null;
    }
}
