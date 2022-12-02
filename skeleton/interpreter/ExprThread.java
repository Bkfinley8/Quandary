package interpreter;
import ast.*;
import java.util.*;

public class ExprThread extends Thread {
    Expr expr;
    QVal result;

    public ExprThread(Expr expr){
        this.expr = expr;
    }

    public void run(HashMap<String, QVal> varMap){
        result = Interpreter.getInterpreter().evaluate(expr, varMap);
    }

    public QVal getResult(){
        return this.result;
    }
}
