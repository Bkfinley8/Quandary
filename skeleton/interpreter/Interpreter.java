package interpreter;

import java.io.*;
import java.lang.Thread.State;
import java.util.HashMap;
import java.util.Random;

import parser.ParserWrapper;
import ast.*;

public class Interpreter {

    // Process return codes
    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_PARSING_ERROR = 1;
    public static final int EXIT_STATIC_CHECKING_ERROR = 2;
    public static final int EXIT_DYNAMIC_TYPE_ERROR = 3;
    public static final int EXIT_NIL_REF_ERROR = 4;
    public static final int EXIT_QUANDARY_HEAP_OUT_OF_MEMORY_ERROR = 5;
    public static final int EXIT_DATA_RACE_ERROR = 6;
    public static final int EXIT_NONDETERMINISM_ERROR = 7;

    static private Interpreter interpreter;

    public static Interpreter getInterpreter() {
        return interpreter;
    }

    public static void main(String[] args) {
        String gcType = "NoGC"; // default for skeleton, which only supports NoGC
        long heapBytes = 1 << 14;
        int i = 0;
        String filename;
        long quandaryArg;
        try {
            for (; i < args.length; i++) {
                String arg = args[i];
                if (arg.startsWith("-")) {
                    if (arg.equals("-gc")) {
                        gcType = args[i + 1];
                        i++;
                    } else if (arg.equals("-heapsize")) {
                        heapBytes = Long.valueOf(args[i + 1]);
                        i++;
                    } else {
                        throw new RuntimeException("Unexpected option " + arg);
                    }
                } else {
                    if (i != args.length - 2) {
                        throw new RuntimeException("Unexpected number of arguments");
                    }
                    break;
                }
            }
            filename = args[i];
            quandaryArg = Long.valueOf(args[i + 1]);
        } catch (Exception ex) {
            System.out.println("Expected format: quandary [OPTIONS] QUANDARY_PROGRAM_FILE INTEGER_ARGUMENT");
            System.out.println("Options:");
            System.out.println("  -gc (MarkSweep|Explicit|NoGC)");
            System.out.println("  -heapsize BYTES");
            System.out.println("BYTES must be a multiple of the word size (8)");
            return;
        }

        Program astRoot = null;
        Reader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            astRoot = ParserWrapper.parse(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
            Interpreter.fatalError("Uncaught parsing error: " + ex, Interpreter.EXIT_PARSING_ERROR);
        }
        //astRoot.println(System.out);
        interpreter = new Interpreter(astRoot);
        interpreter.initMemoryManager(gcType, heapBytes);
        String returnValueAsString = interpreter.executeRoot(astRoot, quandaryArg).toString();
        System.out.println("Interpreter returned " + returnValueAsString);
    }

    final Program astRoot;
    final Random random;

    private Interpreter(Program astRoot) {
        this.astRoot = astRoot;
        this.random = new Random();
    }

    void initMemoryManager(String gcType, long heapBytes) {
        if (gcType.equals("Explicit")) {
            throw new RuntimeException("Explicit not implemented");            
        } else if (gcType.equals("MarkSweep")) {
            throw new RuntimeException("MarkSweep not implemented");            
        } else if (gcType.equals("RefCount")) {
            throw new RuntimeException("RefCount not implemented");            
        } else if (gcType.equals("NoGC")) {
            // Nothing to do
        }
    }

    String executeRoot(Program astRoot, long arg) {
        String lastReturn = "";
        HashMap<String, Integer> varMap = new HashMap<String, Integer>();
        StatementList statements = astRoot.getStmtList();
        lastReturn = executeStmtList(statements, lastReturn, varMap) ;
        return lastReturn;
    }

    String executeStmtList(StatementList stmtList, String ret, HashMap<String, Integer> varMap){
        for(int i = 0; i < stmtList.getSize();i++){
            Statement stmt = stmtList.at(i);
            ret = executeStmt(stmt, ret, varMap);
        }

        return ret;
    }

    String executeStmt(Statement stmt, String lastReturn, HashMap<String, Integer> varMap){
        if(stmt instanceof VarDeclarationStatement){
            VarDeclarationStatement temp = (VarDeclarationStatement)stmt;
            VarDecl var = temp.getVarDecl();
            Expr expr = temp.getExpr();

            varMap.put(var.getIdentifier(),(Integer) evaluate(expr));
        } else if(stmt instanceof PrintStatement){
            PrintStatement temp = (PrintStatement) stmt;
            Expr expr = temp.getExpr();
            System.out.println(evaluate(expr));
        } else if(stmt instanceof IfStatement){
            IfStatement temp = (IfStatement) stmt;
            Condition cond = temp.getCondition();
            Statement ifStatement = temp.getStatement();
            if(evalCondition(cond)){
                executeStmt(ifStatement, lastReturn, varMap);
            } 
        } else if( stmt instanceof IfElseStatement){
            IfElseStatement temp = (IfElseStatement) stmt;
            Condition cond = temp.getCondition();
            Statement ifStatement = temp.getIfStatement();
            Statement elseStatement = temp.getElseStatement();
            if(evalCondition(cond)){
                executeStmt(ifStatement, lastReturn, varMap);
            } else {
                executeStmt(elseStatement, lastReturn, varMap);
            }
        } else if(stmt instanceof ReturnStatement){
            ReturnStatement temp = (ReturnStatement) stmt;
            Expr expr = temp.getExpr();
            lastReturn = (String) evaluate(expr);
        } else if(stmt instanceof StatementList){
            StatementList temp = (StatementList) stmt;
            lastReturn = executeStmtList(temp, lastReturn, varMap);
        } else {
            System.out.println("ERROR: SOMETHING WENT WWRONG");
        }
        return lastReturn;
    }

    boolean evalCondition(Condition cond){
        if(cond instanceof NotOP){
            NotOP temp = (NotOP) cond;
            return !evalCondition(temp.getCondition());
        } else if(cond instanceof AndOr){
            AndOr temp = (AndOr) cond;
            Condition cond1 = temp.getCondition1();
            Condition cond2 = temp.getCondition2();
            int op = temp.getOperator();
            if(op == 1){
                return evalCondition(cond1) && evalCondition(cond2);
            } else {
                return evalCondition(cond1) || evalCondition(cond2);
            }
        } else if(cond instanceof CondEval){
            CondEval temp = (CondEval) cond;
            Expr expr1 = temp.getExpr1();
            Expr expr2 = temp.getExpr2();
            int op = temp.getOperator();
            if(op == 1){
                return (Integer)evaluate(expr1) < (Integer)evaluate(expr2);
            } else if (op == 2){
                return (Integer)evaluate(expr1) <= (Integer)evaluate(expr2);
            } else if (op == 3){
                return (Integer)evaluate(expr1) > (Integer)evaluate(expr2);
            } else if (op == 4){
                return (Integer)evaluate(expr1) >= (Integer)evaluate(expr2);
            } else if (op == 5){
                return (Integer)evaluate(expr1) == (Integer)evaluate(expr2);
            } else {
                return (Integer)evaluate(expr1) != (Integer)evaluate(expr2);
            }
        }  else {
            throw new RuntimeException("Unhandled Condition type");
        }
    }

    Object evaluate(Expr expr) {
        if (expr instanceof ConstExpr) {
            return ((ConstExpr)expr).getValue();
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr)expr;
            switch (binaryExpr.getOperator()) {
                case BinaryExpr.PLUS: return (Long)evaluate(binaryExpr.getLeftExpr()) + (Long)evaluate(binaryExpr.getRightExpr());
                case BinaryExpr.MINUS: return (Long)evaluate(binaryExpr.getLeftExpr()) - (Long)evaluate(binaryExpr.getRightExpr());
                case BinaryExpr.TIMES: return (Long)evaluate(binaryExpr.getLeftExpr()) * (Long)evaluate(binaryExpr.getRightExpr());
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if(expr instanceof UnaryExpr) {
            UnaryExpr unaryExpr = (UnaryExpr)expr;
            return -1 * (Long)evaluate(unaryExpr.getExpr());
        } else {
            throw new RuntimeException("Unhandled Expr type");
        }
    }

	public static void fatalError(String message, int processReturnCode) {
        System.out.println(message);
        System.exit(processReturnCode);
	}
}
