package ast;


public class Program extends ASTNode {

    FuncDefList funcDefList;

    /*
    public Program(VarDecl funcIdent, VarDecl args, List<Statement> statements, Location loc) {
        super(loc);
        this.functionIdentifier = funcIdent;
        this.args = args;
        this.statements = new StatementList(statements, loc);
    }
    */
    public Program(FuncDefList funcDefList, Location loc) {
        super(loc);
        this.funcDefList = funcDefList;

    }

    public FuncDefList getStmtList(){
        return this.funcDefList;
    }


}
