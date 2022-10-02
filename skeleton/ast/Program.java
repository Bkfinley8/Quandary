package ast;

import java.util.List;

public class Program extends ASTNode {

    final VarDecl functionIdentifier;
    final List<VarDecl> formalParameters;
    final StatementList statements;

    public Program(VarDecl funcIdent, List<VarDecl> formalParams, List<Statement> statements, Location loc) {
        super(loc);
        this.functionIdentifier = funcIdent;
        this.formalParameters = formalParams;
        this.statements = new StatementList(statements, loc);
    }

}
