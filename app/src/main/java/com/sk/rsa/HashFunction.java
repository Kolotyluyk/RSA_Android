package com.sk.rsa;

/**
 * Created by Сергій on 18.11.2016.
 */

public class HashFunction {
 private int digitalSignature;

  public int getDigitalSignature() {
    return digitalSignature;
  }

  public int compute(String text){
    int result=0;

    for (int i=0;i<text.length();i++)
      result=((int)text.charAt(i)+result)%33;

    return digitalSignature=result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    HashFunction that = (HashFunction) o;

    return digitalSignature == that.digitalSignature;

  }

  @Override
  public int hashCode() {
    return digitalSignature;
  }
}
