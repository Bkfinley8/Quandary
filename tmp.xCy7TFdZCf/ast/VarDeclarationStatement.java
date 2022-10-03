package ast;

public class VarDeclarationStatement extends Statement {
    final VarDecl varDecl;
    final Expr expr;

    public VarDeclarationStatement(VarDecl varDecl, Expr expr, Location loc) {
        super(loc);
        this.varDecl = varDecl;
        this.expr = expr;
    }

    public VarDecl getVarDecl() {
        return this.varDecl;
    }

    public Expr getExpr(){
        return this.expr;
    }
}
