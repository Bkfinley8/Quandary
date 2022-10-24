package ast;

public class FuncDefList extends FuncDef {

    FuncDef first;
    FuncDefList rest;

    public FuncDefList(FuncDef first, FuncDefList rest, Location loc){
        super(loc);
        this.first = first;
        this.rest = rest;
    }
}
