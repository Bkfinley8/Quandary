package interpreter;

import java.io.*;
import java.lang.Thread.State;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import parser.ParserWrapper;
import ast.*;
import java.util.Random;

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
    boolean hasReturned;
    HashMap<String,FuncDef> env;

    private Interpreter(Program astRoot) {
        this.astRoot = astRoot;
        this.hasReturned = false;
        this.random = new Random();
        this.env = new HashMap<String,FuncDef>();
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

    /* 
    String executeRoot(Program astRoot, long arg) {
        
            //Long lastReturn = 0L;
            //HashMap<String, Long> varMap = new HashMap<String, Long>();
            //VarDecl argument = astRoot.getArgs();
            //varMap.put(argument.getIdentifier(),arg);
            //StatementList statements = astRoot.getStmtList();
            //return String.valueOf(executeStmtList(statements, lastReturn, varMap));
        Long lastReturn = 0L;
        FuncDefList list = astRoot.getList();
        populateFuncDef(list);
        if(this.env.containsKey("main")){
            FuncDef main = this.env.get("main");
            HashMap<String,Long> argMap = new HashMap<String,Long>();
            argMap.put(main.getFormalDeclList().getList().getFirst().getIdentifier(),arg);
            lastReturn = executeStmtList(main.getStatementList(),lastReturn,argMap);
        } else {
            throw new RuntimeException("No Main function");
        }

        return String.valueOf(lastReturn);      
    }
*/
    Object executeRoot(Program astRoot, long arg) {
        FuncDefList list = astRoot.getList();
        populateFuncDef(list);
        if(this.env.containsKey("main")){
            FuncDef main = this.env.get("main");
            HashMap<String,Object> argMap = new HashMap<String,Object>();
            argMap.put(main.getFormalDeclList().getList().getFirst().getIdentifier(),arg);
            return executeStmtList(main.getStatementList(),argMap);
        } else {
        throw new RuntimeException("No Main function");
        }      
    }

    void populateFuncDef(FuncDefList list){
        FuncDefList currentList = list;
        while(!currentList.isEmpty()){
            FuncDef temp = currentList.getFirst();
            this.env.put(temp.getReturnDecl().getIdentifier(),temp);
            if(currentList.hasNext()){
                currentList = currentList.getRest();
            } else {
                break;
            }
        }
    }

    Object executeStmtList(StatementList stmtList, HashMap<String,Object> varMap){
        Object retVal = executeStmt(stmtList.getStatement(),varMap);
        if(retVal != null){
            return retVal;
        }
        if(stmtList.hasNext()){
            return executeStmtList(stmtList.getNextStatement(), varMap);
        }
        return null;
    }

    Object executeStmt(Statement stmt, HashMap<String, Object> varMap){
        if(stmt instanceof VarDeclarationStatement){
            VarDeclarationStatement temp = (VarDeclarationStatement)stmt;
            VarDecl var = temp.getVarDecl();
            Expr expr = temp.getExpr();
            varMap.put(var.getIdentifier(), evaluate(expr, varMap));
            //Ref a = 3 . 2;
            return null;
        } else if(stmt instanceof IfStatement){
            IfStatement temp = (IfStatement) stmt;
            Condition cond = temp.getCondition();
            Statement ifStatement = temp.getStatement();
            if(evalCondition(cond,varMap)){
                return executeStmt(ifStatement,varMap);
            } 
            return null;
        } else if(stmt instanceof IfElseStatement){
            IfElseStatement temp = (IfElseStatement) stmt;
            Condition cond = temp.getCondition();
            Statement ifStatement = temp.getIfStatement();
            Statement elseStatement = temp.getElseStatement();
            if(evalCondition(cond,varMap)){
                return executeStmt(ifStatement, varMap);
            } else {
                return executeStmt(elseStatement, varMap);
            }
        } else if(stmt instanceof ReturnStatement){
            ReturnStatement temp = (ReturnStatement) stmt;
            Expr expr = temp.getExpr();
            return evaluate(expr, varMap);
        } else if(stmt instanceof PrintStatement){
            PrintStatement temp = (PrintStatement) stmt; 
            Expr expr = temp.getExpr();
            System.out.println(evaluate(expr, varMap));
            return null;
        } else if(stmt instanceof StatementList){
            StatementList temp = (StatementList) stmt;
            return executeStmtList(temp, varMap);
        } else {
            throw new RuntimeException("Unhandled Statement type");
        }
    }

    boolean evalCondition(Condition cond, HashMap<String, Object> varMap){
        if(cond instanceof NotOP){
            NotOP temp = (NotOP) cond;
            return !evalCondition(temp.getCondition(), varMap);
        } else if(cond instanceof EvalAndOr){
            EvalAndOr temp = (EvalAndOr) cond;
            Condition cond1 = temp.getCondition1();
            Condition cond2 = temp.getCondition2();
            int op = temp.getOperator();
            if(op == 1){
                return evalCondition(cond1, varMap) && evalCondition(cond2, varMap);
            } else {
                return evalCondition(cond1, varMap) || evalCondition(cond2, varMap);
            }
        } else if(cond instanceof CondEval){
            CondEval temp = (CondEval) cond;
            Expr expr1 = temp.getExpr1();
            Expr expr2 = temp.getExpr2();
            int op = temp.getOperator();
            if(op == 1){
                return (Long)evaluate(expr1, varMap) < (Long)evaluate(expr2, varMap);
            } else if (op == 2){
                return (Long)evaluate(expr1, varMap) <= (Long)evaluate(expr2, varMap);
            } else if (op == 3){
                return (Long)evaluate(expr1, varMap) > (Long)evaluate(expr2, varMap);
            } else if (op == 4){
                return (Long)evaluate(expr1, varMap) >= (Long)evaluate(expr2, varMap);
            } else if (op == 5){
                return (Long)evaluate(expr1, varMap) == (Long)evaluate(expr2, varMap);
            } else {
                return (Long)evaluate(expr1, varMap) != (Long)evaluate(expr2, varMap);
            }
        }  else {
            throw new RuntimeException("Unhandled Condition type");
        }
    }

    Object evaluate(Expr expr, HashMap<String, Object> varMap){
        if (expr instanceof ConstExpr) {
            return ((ConstExpr)expr).getValue();
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr)expr;
            switch (binaryExpr.getOperator()) {
                case BinaryExpr.PLUS: return (Long)evaluate(binaryExpr.getLeftExpr(), varMap) + (Long)evaluate(binaryExpr.getRightExpr(), varMap);
                case BinaryExpr.MINUS: return (Long)evaluate(binaryExpr.getLeftExpr(), varMap) - (Long)evaluate(binaryExpr.getRightExpr(), varMap);
                case BinaryExpr.TIMES: return (Long)evaluate(binaryExpr.getLeftExpr(), varMap) * (Long)evaluate(binaryExpr.getRightExpr(), varMap);
                case BinaryExpr.DOT: return new QRef(new QObj((QVal) evaluate(binaryExpr.getLeftExpr(), varMap),(QVal) evaluate(binaryExpr.getRightExpr(), varMap)));
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if(expr instanceof UnaryExpr) {
            UnaryExpr unaryExpr = (UnaryExpr)expr;
            return -1 * (Long)evaluate(unaryExpr.getExpr(), varMap);
        } else if(expr instanceof IdentExpr){
            IdentExpr temp = (IdentExpr) expr;
            String varName = temp.getIdentifier();
            return varMap.get(varName);
        } else if(expr instanceof FuncExpr) {
            FuncExpr temp = (FuncExpr)expr;
            String ident = temp.getIdent();
            ExprList list = temp.getExprList();
            if(this.env.containsKey(ident)){
                FuncDef func = this.env.get(ident);
                HashMap<String,Object> vars = new HashMap<String,Object>();
                NEmptyExprList exprList = null;
                NEmptyFormalDeclList declList = null;
                if(list != null){
                    exprList = list.getList();
                }
                if(func.getFormalDeclList() != null){
                    declList = func.getFormalDeclList().getList();
                }
                while(exprList != null){
                    Expr temp2 = exprList.getFirst();
                    VarDecl temp3 = declList.getFirst();
                    vars.put(temp3.getIdentifier(),evaluate(temp2,varMap));
                    if(exprList.hasNext()){
                        exprList = exprList.getRest();
                        declList = declList.getRest();
                    } else {
                        break;
                    }
                }
                return executeStmtList(func.getStatementList(), vars);
            } else if(ident.equals("randomInt")){
                return ThreadLocalRandom.current().nextLong((Long)evaluate(list.getList().getFirst(), varMap));
            } else {
                throw new RuntimeException("Invalid function call");
            }
        } else {
            throw new RuntimeException("Unhandled Expr type");
        }
    }

    boolean sameLength(NEmptyFormalDeclList declList, NEmptyExprList exprList){
        return true;
    }
    
	public static void fatalError(String message, int processReturnCode) {
        System.out.println(message);
        System.exit(processReturnCode);
	}
}
