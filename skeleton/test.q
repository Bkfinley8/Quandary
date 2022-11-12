mutable Q main(int arg){
    mutable Ref list = nil;
    return isAtom(2.3);
    list = add(list, 4);


    return arg;
}

mutable Ref add(Ref list, Q elem) {
  if (isNil(list) != 0) {
    return elem . nil;
  }
  mutable Ref curr = list;
  while (isNil(right(curr)) == 0) {
    curr = (Ref)right(curr);
  }
  setRight(curr, elem . nil);
  return list;
}