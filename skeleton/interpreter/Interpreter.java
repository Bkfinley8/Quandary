package interpreter;

import java.io.*;
import java.lang.Thread.State;
import java.text.Normalizer.Form;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

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
        QVal ret = interpreter.executeRoot(astRoot, quandaryArg);
        String returnValueAsString = "";
        if(ret == null){
            returnValueAsString = "nil";
        } else {
            returnValueAsString = ret.toString();
        }
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
/* 
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
*/

    QVal executeRoot(Program astRoot, long arg){
        FuncDef mainFuncDef = astRoot.getList().lookFuncDef("main");
        HashMap<String,QVal> mainEnv = new HashMap<String,QVal>();
        mainEnv.put(mainFuncDef.getFormalDeclList().getFirst().getIdentifier(),new QInt(arg));
        return execute(mainFuncDef.getStatementList(), mainEnv);
    }
/* 
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
*/

    QVal execute(Statement stmt, HashMap<String, QVal> varMap){
        if(stmt instanceof StatementList){
            StatementList temp = (StatementList) stmt;
            QVal retVal = execute(temp.getStatement(), varMap);
            if(retVal != null){
                return retVal;
            }
            if(temp.getNextStatement() != null){
                return execute(temp.getNextStatement(), varMap);
            }
            return null;
        } else if(stmt instanceof VarDeclarationStatement){
            VarDeclarationStatement temp = (VarDeclarationStatement)stmt;
            VarDecl var = temp.getVarDecl();
            Expr expr = temp.getExpr();
            varMap.put(var.getIdentifier(), evaluate(expr, varMap));
            return null;
        } else if(stmt instanceof IfStatement){
            IfStatement temp = (IfStatement) stmt;
            Condition cond = temp.getCondition();
            Statement ifStatement = temp.getStatement();
            if(evaluate(cond,varMap)){
                return execute(ifStatement,varMap);
            } 
            return null;
        } else if(stmt instanceof IfElseStatement){
            IfElseStatement temp = (IfElseStatement) stmt;
            Condition cond = temp.getCondition();
            Statement ifStatement = temp.getIfStatement();
            Statement elseStatement = temp.getElseStatement();
            if(evaluate(cond,varMap)){
                return execute(ifStatement, varMap);
            } else {
                return execute(elseStatement, varMap);
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
        } else if(stmt instanceof UpdateStatement){
            UpdateStatement temp = (UpdateStatement) stmt;
            String ident = temp.getIdent();
            Expr expr = temp.getExpr();
            varMap.replace(ident, evaluate(expr, varMap));
            return null;
        } else if(stmt instanceof CallStatement){
            CallStatement temp = (CallStatement)stmt;
            if(temp.getIdent().equals("setLeft")){
                Expr arg1 = temp.getExprList().getFirst();
                QVal arg2 = evaluate(temp.getExprList().getRest().getFirst(),varMap);

                QObj obj = ((QRef)evaluate(arg1,varMap)).getRef();
                obj.setLeft(arg2);
                /* 
                if(arg1 instanceof BinaryExpr){
                    
                } else {
                    String ident = ((IdentExpr)arg1).getIdentifier();
                    QRef ref = (QRef)varMap.get(ident);
                    QObj obj = ref.getRef();
                    obj.setRight(arg2);
                }
                */
                return null;
            } else if(temp.getIdent().equals("setRight")){
                Expr arg1 = temp.getExprList().getFirst();
                QVal arg2 = evaluate(temp.getExprList().getRest().getFirst(),varMap);

                QObj obj = ((QRef)evaluate(arg1,varMap)).getRef();
                obj.setRight(arg2);
                /* 
                if(arg1 instanceof BinaryExpr){
                    
                } else {
                    String ident = ((IdentExpr)arg1).getIdentifier();
                    QRef ref = (QRef)varMap.get(ident);
                    QObj obj = ref.getRef();
                    obj.setRight(arg2);
                }
                */
                return null;
            }  else if(temp.getIdent().equals("acq")){
                Expr arg1 = temp.getExprList().getFirst();
                QObj obj = ((QRef)evaluate(arg1,varMap)).getRef();
                AtomicBoolean lock = obj.getLock();
                while(!(lock.compareAndSet(false, true))){
                    return null;
                }
            } else if(temp.getIdent().equals("red")){
                Expr arg1 = temp.getExprList().getFirst();
                QObj obj = ((QRef)evaluate(arg1,varMap)).getRef();
                AtomicBoolean lock = obj.getLock();
                while(!(lock.compareAndSet(true, false))){
                    return null;
                }
            }
            FuncDef callee = astRoot.getList().lookFuncDef(temp.getIdent());
            HashMap<String,QVal> calleeEnv = new HashMap<String,QVal>();
            FormalDeclList currentFormalDeclList = callee.getFormalDeclList();
            ExprList currExprList = temp.getExprList();
            while(currentFormalDeclList != null){
                calleeEnv.put(currentFormalDeclList.getFirst().getIdentifier(),evaluate(currExprList.getFirst(),varMap));
                currentFormalDeclList = currentFormalDeclList.getRest();
                currExprList = currExprList.getRest();
            }
            execute(callee.getStatementList(), calleeEnv);   
            return null;
        } else if(stmt instanceof WhileStatement) {
            WhileStatement temp = (WhileStatement) stmt;
            Condition cond = temp.getCondition();
            Statement ifStatement = temp.getStatement();
            QVal ret = null;
            while(evaluate(cond,varMap)){
                ret = execute(ifStatement,varMap);
                if(ret != null){
                    return ret;
                }
            } 
            return null;
        } else {
            throw new RuntimeException("Unhandled Statement type");
        }
}
 
/* 
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
    */
    boolean evaluate(Condition cond, HashMap<String, QVal> varMap){
        if(cond instanceof CondEval){
            CondEval temp = (CondEval)cond;
            Expr expr1 = temp.getExpr1();
            Expr expr2 = temp.getExpr2();
            switch (temp.getOperator()){
                case CondEval.DOUBLE_EQUALS: return ((QInt)evaluate(expr1, varMap)).getVal() == ((QInt)evaluate(expr2, varMap)).getVal();
                case CondEval.NOT_EQUALS: return ((QInt)evaluate(expr1, varMap)).getVal() != ((QInt)evaluate(expr2, varMap)).getVal();
                case CondEval.LESS_THAN: return ((QInt)evaluate(expr1, varMap)).getVal() < ((QInt)evaluate(expr2, varMap)).getVal();
                case CondEval.GREATER_THAN: return ((QInt)evaluate(expr1, varMap)).getVal() > ((QInt)evaluate(expr2, varMap)).getVal();
                case CondEval.LESS_THAN_OR_EQUAL_TO: return ((QInt)evaluate(expr1, varMap)).getVal() <= ((QInt)evaluate(expr2, varMap)).getVal();
                case CondEval.GREATER_THAN_OR_EQUAL_TO: return ((QInt)evaluate(expr1, varMap)).getVal() >= ((QInt)evaluate(expr2, varMap)).getVal();
            }
        } else if(cond instanceof LogicalCond){
            LogicalCond temp = (LogicalCond)cond;
            switch(temp.getOperator()){
                case LogicalCond.BOOL_AND: return evaluate(temp.getCondition1(), varMap) && evaluate(temp.getCondition2(), varMap);
                case LogicalCond.BOOL_OR: return evaluate(temp.getCondition1(), varMap) || evaluate(temp.getCondition2(), varMap);
                case LogicalCond.BOOL_NOT: return !evaluate(temp.getCondition1(), varMap);
            }
        }
        throw new RuntimeException();
    }

    QVal evaluate(Expr expr, HashMap<String, QVal> varMap){
        if (expr instanceof ConstExpr) {
            return new QInt(((ConstExpr)expr).getValue());
        } else if (expr instanceof BinaryExpr) {
            BinaryExpr binaryExpr = (BinaryExpr)expr;
            switch (binaryExpr.getOperator()) {
                case BinaryExpr.PLUS: return new QInt(((QInt)evaluate(binaryExpr.getLeftExpr(), varMap)).getVal() + ((QInt)evaluate(binaryExpr.getRightExpr(), varMap)).getVal());
                case BinaryExpr.MINUS: return new QInt(((QInt)evaluate(binaryExpr.getLeftExpr(), varMap)).getVal() - ((QInt)evaluate(binaryExpr.getRightExpr(), varMap)).getVal());
                case BinaryExpr.TIMES: return new QInt(((QInt)evaluate(binaryExpr.getLeftExpr(), varMap)).getVal() * ((QInt)evaluate(binaryExpr.getRightExpr(), varMap)).getVal());
                case BinaryExpr.DOT: return new QRef(new QObj((QVal) evaluate(binaryExpr.getLeftExpr(),varMap),(QVal) evaluate(binaryExpr.getRightExpr(),varMap)));
                default: throw new RuntimeException("Unhandled operator");
            }
        } else if(expr instanceof UnaryExpr) {
            UnaryExpr unaryExpr = (UnaryExpr)expr;
            long value = ((QInt)evaluate(unaryExpr.getExpr(), varMap)).getVal();
            return new QInt(-value);
        } else if(expr instanceof IdentExpr){
            return varMap.get(((IdentExpr)expr).getIdentifier());
        } else if(expr instanceof FuncExpr) {
            /* 

            FuncExpr temp = (FuncExpr)expr;
            String ident = temp.getIdent();
            ExprList list = temp.getExprList();
            if(this.env.containsKey(ident)){
                FuncDef func = this.env.get(ident);
                HashMap<String,Object> vars = new HashMap<String,Object>();
                ExprList exprList = null;
                FormalDeclList declList = null;
                if(list != null){
                    exprList = list;
                }
                if(func.getFormalDeclList() != null){
                    declList = func.getFormalDeclList();
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
                return execute(func.getStatementList(), vars);
            } else if(ident.equals("randomInt")){
                return ThreadLocalRandom.current().nextLong((Long)evaluate(list.getFirst(), varMap));
            } else {
                throw new RuntimeException("Invalid function call");
            }
            */
            FuncExpr temp = (FuncExpr)expr;
            if(temp.getIdent().equals("randomInt")){
                long val = ((QInt)evaluate(temp.getExprList().getFirst(), varMap)).getVal();
                return new QInt(ThreadLocalRandom.current().nextLong(val));
            } else if(temp.getIdent().equals("isNil")){
                Expr arg = temp.getExprList().getFirst();
                return evaluate(arg,varMap) == null ? new QInt (1) : new QInt(0);
            } else if(temp.getIdent().equals("isAtom")){
                Expr arg = temp.getExprList().getFirst();
                return ((evaluate(arg,varMap) == null) || (arg instanceof ConstExpr)) ? new QInt (1) : new QInt(0);
            } else if(temp.getIdent().equals("left")){
                QRef arg = (QRef)evaluate(temp.getExprList().getFirst(),varMap);
                return arg.getRef().getLeft();
            }  else if(temp.getIdent().equals("right")){
                QRef arg = (QRef)evaluate(temp.getExprList().getFirst(),varMap);
                return arg.getRef().getRight();
            } if(temp.getIdent().equals("setLeft")){
                Expr arg1 = temp.getExprList().getFirst();
                QVal arg2 = evaluate(temp.getExprList().getRest().getFirst(),varMap);

                QObj obj = ((QRef)evaluate(arg1,varMap)).getRef();
                obj.setLeft(arg2);
                /* 
                if(arg1 instanceof BinaryExpr){
                    
                } else {
                    String ident = ((IdentExpr)arg1).getIdentifier();
                    QRef ref = (QRef)varMap.get(ident);
                    QObj obj = ref.getRef();
                    obj.setRight(arg2);
                }
                */
                return new QInt(1);
            } else if(temp.getIdent().equals("setRight")){
                Expr arg1 = temp.getExprList().getFirst();
                QVal arg2 = evaluate(temp.getExprList().getRest().getFirst(),varMap);

                QObj obj = ((QRef)evaluate(arg1,varMap)).getRef();
                obj.setRight(arg2);
                /* 
                if(arg1 instanceof BinaryExpr){
                    
                } else {
                    String ident = ((IdentExpr)arg1).getIdentifier();
                    QRef ref = (QRef)varMap.get(ident);
                    QObj obj = ref.getRef();
                    obj.setRight(arg2);
                }
                */
                return new QInt(1);
            } else if(temp.getIdent().equals("acq")){
                Expr arg1 = temp.getExprList().getFirst();
                QObj obj = ((QRef)evaluate(arg1,varMap)).getRef();
                AtomicBoolean lock = obj.getLock();
                while(!(lock.compareAndSet(false, true))){
                    return new QInt(1);
                }
            } else if(temp.getIdent().equals("red")){
                Expr arg1 = temp.getExprList().getFirst();
                QObj obj = ((QRef)evaluate(arg1,varMap)).getRef();
                AtomicBoolean lock = obj.getLock();
                while(!(lock.compareAndSet(true, false))){
                    return new QInt(1);
                }
            }
            FuncDef callee = astRoot.getList().lookFuncDef(temp.getIdent());
            HashMap<String,QVal> calleeEnv = new HashMap<String,QVal>();
            FormalDeclList currentFormalDeclList = callee.getFormalDeclList();
            ExprList currExprList = temp.getExprList();
            while(currentFormalDeclList != null){
                calleeEnv.put(currentFormalDeclList.getFirst().getIdentifier(),evaluate(currExprList.getFirst(),varMap));
                currentFormalDeclList = currentFormalDeclList.getRest();
                currExprList = currExprList.getRest();
            }  
            return execute(callee.getStatementList(), calleeEnv); 
        } else if(expr instanceof NilExpr){
            return null;
        } else if(expr instanceof CastExpr){
            //Not 100% sure this is right
            CastExpr temp = (CastExpr)expr;
            int type = temp.getType();
            Expr expr2 = temp.getExpr();
            QVal ret = evaluate(expr2,varMap);
            if(type == 1){
                return (QInt) ret;
            } else if(type == 2){
                return (QVal) ret;
            } else {
                return (QRef) ret;
            }
        } else if(expr instanceof ConcurrentExpr){
            BinaryExpr temp = ((ConcurrentExpr)expr).getExpr();
            ExprThread t1 = new ExprThread(temp.getLeftExpr(),varMap);
            ExprThread t2 = new ExprThread(temp.getRightExpr(),varMap);
            t1.start();
            t2.start();
            try{
                t1.join();
                t2.join();
            } catch (InterruptedException ex) {  }

            int op = temp.getOperator();
            if(op == BinaryExpr.PLUS){
                return new QInt(((QInt)t1.getResult()).getVal() + ((QInt)t2.getResult()).getVal());
            } else if(op == BinaryExpr.MINUS){
                return new QInt(((QInt)t1.getResult()).getVal() - ((QInt)t2.getResult()).getVal()); 
            } else if(op == BinaryExpr.TIMES){
                return new QInt(((QInt)t1.getResult()).getVal() * ((QInt)t2.getResult()).getVal()); 
            } else if(op == BinaryExpr.DOT){
                return new QRef(new QObj(t1.getResult(), t2.getResult()));
            } else {
                throw new RuntimeException("Unhandled Op type");
            }
        }else {
            throw new RuntimeException("Unhandled Expr type");
        }
    }

  
	public static void fatalError(String message, int processReturnCode) {
        System.out.println(message);
        System.exit(processReturnCode);
	}
}
