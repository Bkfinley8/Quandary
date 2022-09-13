Q main(int arg) {
    Ref list = (8980 . (3692 . (8513 . (1301 . (2978 . (8724 . (5612 . (8882 . (317 . (5278 . (655 . (6535 . (603 . (2766 . (907 . (1974 . (899 . (2318 . (8129 . (8865 . (7059 . (8364 . (6569 . (6208 . (1211 . (7817 . (1788 . (463 . (2000 . (2427 . (9642 . (6044 . (2922 . (5147 . (8581 . (6029 . (1877 . (5160 . (6932 . (2408 . (8020 . (1475 . (8993 . (6282 . (713 . (8865 . (6716 . (8033 . (4479 . (2300 . (2469 . (7570 . (1361 . (3586 . (645 . (1731 . (5951 . (4372 . (6918 . (6610 . (5744 . (3833 . (5671 . (9436 . (9403 . (2552 . (6105 . (8199 . (5901 . (3041 . (1268 . (3913 . (4678 . (9829 . (9299 . (2686 . (8987 . (4185 . (9603 . (4038 . (6822 . (5848 . (7385 . (5092 . (9969 . (2670 . (2361 . (9504 . (9807 . (8157 . (6047 . (4022 . (4036 . (892 . (1018 . (2741 . (5847 . (9459 . (5164 . (9025 . nil))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))));
    return sort(list);
}

Ref sort(Ref list) {
    if (isNil(list) != 0 || isNil(right(list)) != 0) return list;
    int pivot = (int)left(list);
    Ref lists = [ sort(subList(list, pivot, -1)) . sort(subList(list, pivot, 1)) ];
    return concat((Ref)left(lists), concat(subList(list, pivot, 0), (Ref)right(lists)));
}

Ref subList(Ref list, int pivot, int comparator) {
    if (isNil(list) != 0)
        return nil;
    int first = (int)left(list);
    Ref rest = (Ref)right(list);
    if ((comparator <  0 && first <  pivot) ||
        (comparator == 0 && first == pivot) ||
        (comparator >  0 && first >  pivot))
        return first . subList(rest, pivot, comparator);
    return subList(rest, pivot, comparator);
}

Ref concat(Ref first, Ref second) {
    if (isNil(first) != 0)
        return second;
    if (isNil(right(first)) != 0)
        return left(first) . second;
    return left(first) . concat((Ref)right(first), second);
}

int isSorted(Ref list) {
    if (isNil(list) != 0 || isNil(right(list)) != 0)
        return 1;
    if ((int)left(list) > (int)left((Ref)right(list)))
        return 0;
    return isSorted((Ref)right(list));
}

int length(Ref list) {
    if (isNil(list) != 0) return 0;
    return 1 + length((Ref)right(list));
}

Ref randomList(int n) {
    if (n == 0) return nil;
    return randomInt(10000) . randomList(n - 1);
}
