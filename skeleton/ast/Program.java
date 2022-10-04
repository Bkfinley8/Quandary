package ast;


public class Program extends ASTNode {

    final VarDecl functionIdentifier;
    final VarDecl args;
    final StatementList statements;

    /*
    public Program(VarDecl funcIdent, VarDecl args, List<Statement> statements, Location loc) {
        super(loc);
        this.functionIdentifier = funcIdent;
        this.args = args;
        this.statements = new StatementList(statements, loc);
    }
    */
    public Program(VarDecl funcIdent, VarDecl args, StatementList statements, Location loc) {
        super(loc);
        this.functionIdentifier = funcIdent;
        this.args = args;
        this.statements = statements;
    }

    public StatementList getStmtList(){
        return this.statements;
    }

    public VarDecl getArgs(){
        return this.args;
    }

}
