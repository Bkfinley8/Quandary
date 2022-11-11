package ast;

public class FuncDefList extends ASTNode {

    FuncDef first;
    FuncDefList rest;

    public FuncDefList(FuncDef first, FuncDefList rest, Location loc){
        super(loc);
        this.first = first;
        this.rest = rest;
    }

    public FuncDef getFirst(){
        return this.first;
    }

    public FuncDefList getRest(){
        return this.rest;
    }

    public boolean isEmpty(){
        return this.first == null;
    }
    
    public boolean hasNext(){
        return this.rest != null;
    }
}
