package interpreter;
import ast.*;
import java.util.*;

public class ExprThread extends Thread {
    Expr expr;
    QVal result;
    HashMap<String, QVal> varMap;
    public ExprThread(Expr expr, HashMap<String,QVal> varMap){
        this.expr = expr;
        this.varMap = varMap;
    }

    public void run(){
        result = Interpreter.getInterpreter().evaluate(expr, varMap);
    }

    public QVal getResult(){
        return this.result;
    }
}
