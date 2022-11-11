package interpreter;

class QInt extends QVal{
    long val;
    public QInt(long val){
        this.val = val;
    }

    public long getVal(){
        return this.val;
    }
}
