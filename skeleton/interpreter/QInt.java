package interpreter;

class QInt extends QVal{
    Long val;
    public QInt(long val){
        this.val = val;
    }

    public long getVal(){
        return this.val;
    }

    @Override
    public String toString(){
        if(this.val == null){
            return "nil";
        }
        return String.valueOf(this.val);
    }

}
