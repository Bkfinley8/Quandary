void fib(int n, int* result){
    int temp;
    if(n<2){
        *result = n;
    } else {
        fib(n-2, result);
        fib(n-1, &temp);
        *result += temp;
    }
}