Q int main(int arg){
    if(arg == 1){
        int i = 0;
        Ref list = 0 . 0;
        Ref temp = list;
        while(i < 17){
            setRight(temp, (i . i));
            temp = (Ref)right(temp);
            i = i + 1;
        }
    } else if(arg == 2){
        Ref a;
        Ref b;
        a =  1.1;  /* 1 */
        b = 2.2;
        setRight(a,b);
        setRight(b,a);
        a =  1.1;       /* 2 */
        b = 2.2;
        setRight(a,b);
        setRight(b,a);
        a =  1.1;       /* 3 */
        b = 2.2;
        setRight(a,b);
        setRight(b,a);
        a =  1.1;           /* 4 */
        b = 2.2;
        setRight(a,b);
        setRight(b,a);
        a =  1.1;           /* 5 */
        b = 2.2;
        setRight(a,b);
        setRight(b,a);
        a =  1.1;           /* 6 */
        b = 2.2;
        setRight(a,b);
        setRight(b,a);
        a =  1.1;           /* 7 */
        b = 2.2;
        setRight(a,b);
        setRight(b,a);
        a =  1.1;           /* 8 */
        b = 2.2;
        setRight(a,b);
        setRight(b,a);
        a =  1.1;           /* 9 */
        b = 2.2;
        setRight(a,b);
        setRight(b,a);
    }  else if(arg == 3){
        int i = 0;
        Ref list = 0 . 0;
        Ref temp = list;
        while(i < 16){
            setRight(temp, (i . i));
            temp = (Ref)right(temp);
            i = i + 1;
        }
        Ref x = nil . 2;
        free(x);   
    }  else if(arg == 4){
        
    }
    return 0;
}