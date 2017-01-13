package com.sk.rsa;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by Сергій on 17.11.2016.
 */

public class RSA {
    private  BigInteger p;

    public BigInteger getQ() {
        return q;
    }

    private  BigInteger q;
    private  BigInteger d;//private
    private   BigInteger e;//public

    public BigInteger getN() {
        return n;
    }

    private  BigInteger n;
    private  BigInteger phi;
    private int lengthPQ;

    public RSA(int lengthPQ) {
        this.lengthPQ=lengthPQ;
        Random r=new Random(600);
        int a=r.nextInt();
        p=BigInteger.probablePrime(lengthPQ,r);
        q=BigInteger.probablePrime(lengthPQ,r);
        this.n=p.multiply(q);
        e=BigInteger.probablePrime(lengthPQ,r);
        phi=p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        d=e.modInverse(phi);

    }

    public BigInteger getD() {
        return d;
    }

    public BigInteger getE() {
        return e;
    }


    public BigInteger getP() {

        return p;
    }

    private int encrypt(int index,int n,int publicKey){

        BigInteger indexBigInteger=BigInteger.valueOf(index);
        BigInteger c=indexBigInteger.modPow(BigInteger.valueOf(publicKey),BigInteger.valueOf(n));
        return c.intValue();
    }

    private int decrypt(int index,int n,int privateKey){
        BigInteger indexBigInteger=BigInteger.valueOf(index);
        BigInteger x=indexBigInteger.modPow(BigInteger.valueOf(privateKey),BigInteger.valueOf(n));
        return x.intValue();
    }



    public String encrypt(String text,int n,int publicKey){
        String resultText="";
        for (int i=0;i<text.length();i++){
            resultText+=(char) encrypt((int)text.charAt(i),n,publicKey);
        }
        return resultText;
    }
    public String decrypt(String text,int n,int privateKey){
        String resultText="";
        for (int i=0;i<text.length();i++){
            resultText+=(char) decrypt((int)text.charAt(i),n,privateKey);
        }
        return resultText;
    }
}
