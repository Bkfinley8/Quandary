package ast;

public class ConcurrentExpr extends Expr {
    BinaryExpr binaryExpr;

    public ConcurrentExpr(BinaryExpr binaryExpr, Location loc){
        super(loc);
        this.binaryExpr = binaryExpr;
    }

    public BinaryExpr getExpr(){
        return this.binaryExpr;
    }
    
}
