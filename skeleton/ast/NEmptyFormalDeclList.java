package ast;

public class NEmptyFormalDeclList extends ASTNode {
    VarDecl first;
    NEmptyFormalDeclList rest;

    public NEmptyFormalDeclList(VarDecl first, NEmptyFormalDeclList rest, Location loc){
        super(loc);
        this.first = first;
        this.rest = rest;
    }

    public VarDecl getFirst(){
        return this.first;
    }

    public NEmptyFormalDeclList getRest(){
        return this.rest;
    }

    public boolean isEmpty(){
        return first == null;
    }
    
    public boolean hasNext(){
        return rest != null;
    }
}
