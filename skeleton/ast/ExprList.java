package ast;

public class ExprList extends Expr{
    Expr first;
    ExprList rest;

    public ExprList(Expr first, ExprList rest, Location loc){
        super(loc);
        this.first = first;
        this.rest = rest;
    }

    public Expr getFirst(){
        return this.first;
    }

    public ExprList getRest(){
        return this.rest;
    }

    public boolean isEmpty(){
        return first == null;
    }
    
    public boolean hasNext(){
        return rest != null;
    }
}
