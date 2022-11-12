package ast;

public class CastExpr extends Expr{
    Expr expr;
    int type;
    /*
     * 1: INT
     * 2: Q
     * 3: Ref
     */
    public CastExpr(int type, Expr expr, Location loc){
        super(loc);
        this.type = type;
        this.expr = expr;
    }

    public int getType(){
        return this.type;
    }

    public Expr getExpr(){
        return this.expr;
    }
}
