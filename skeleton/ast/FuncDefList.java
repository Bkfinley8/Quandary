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

    public FuncDef lookFuncDef(String name){
        if(first.getReturnDecl().getIdentifier().equals(name)){
            return first;
        }
        return rest.lookFuncDef(name);
    }
}
