package ast;


public class Program extends ASTNode {

    FuncDefList funcDefList;


    public Program(FuncDefList funcDefList, Location loc) {
        super(loc);
        this.funcDefList = funcDefList;

    }

    public FuncDefList getList(){
        return this.funcDefList;
    }


}
