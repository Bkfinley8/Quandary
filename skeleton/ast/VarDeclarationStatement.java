package ast;

public class VarDeclarationStatement extends Statement {
    final VarDecl varDecl;
    final Expr val;

    public VarDeclarationStatement(VarDecl varDecl, Expr expr, Location loc) {
        super(loc);
        this.varDecl = varDecl;
        this.val = expr;
    }
}
