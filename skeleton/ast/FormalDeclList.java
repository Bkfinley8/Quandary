package ast;

public class FormalDeclList extends ASTNode {
    VarDecl first;
    FormalDeclList rest;

    public FormalDeclList(VarDecl first, FormalDeclList rest, Location loc){
        super(loc);
        this.first = first;
        this.rest = rest;
    }

    public VarDecl getFirst(){
        return this.first;
    }

    public FormalDeclList getRest(){
        return this.rest;
    }

    public boolean isEmpty(){
        return first == null;
    }
    
    public boolean hasNext(){
        return rest != null;
    }
}
