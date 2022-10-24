package ast;

public class FormalDeclList extends VarDecl {
    VarDecl first;
    FormalDeclList rest;

    public FormalDeclList(VarDecl first, FormalDeclList rest, Location loc){
        super(loc);
        this.first = first;
        this.rest = rest;
    }
}
