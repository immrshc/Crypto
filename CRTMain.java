import java.math.BigInteger; 

public class CRTMain{
    public static void main(String[] argv){
        SignWithCRT signFunc = new SignWithCRT();
        if(argv.length != 4){return;}
        BigInteger c = new BigInteger(argv[0]);
        BigInteger p = new BigInteger(argv[1]);
        BigInteger q = new BigInteger(argv[2]);
        BigInteger d = new BigInteger(argv[3]);
        if(c.signum() == -1 || p.signum() != 1
           || q.signum() != 1 || d.signum() != 1){return;}
        BigInteger m = signFunc.sign(c, p, q, d);
        System.out.println(m);
    }
}

class SignWithCRT{
    BigInteger sign(BigInteger c, BigInteger p,
            BigInteger q,BigInteger d){
        ModPowBinary modpow = new ModPowBinary();
        ExtendedEuclidGCD gcdFunc = new ExtendedEuclidGCD();
        //フェルマーの小定理で利用する
        BigInteger dp = d.mod(p.subtract(BigInteger.ONE));
        BigInteger dq = d.mod(q.subtract(BigInteger.ONE));
        //cを出来るだけ小さくしておく
        BigInteger cp = c.mod(p);
        BigInteger cq = c.mod(q);
        // m mod p, m mod q を求める
        BigInteger mp = modpow.modPow(cp, dp, p);
        BigInteger mq = modpow.modPow(cq, dq, q);
        //ap + bq = 1 となるA, Bを求める
        gcdFunc.gcd(p, q);
        //pとqは互いに素でなければならない
        if(!gcdFunc.getGCD().equals(BigInteger.ONE)){
            throw new IllegalArgumentException();
        }
        BigInteger a = gcdFunc.getX();
        BigInteger b = gcdFunc.getY();
        //中国人剰余定理より m mod p*q を求める
        BigInteger m = (mp.multiply(b.multiply(q)).add(
                    mq.multiply(a.multiply(p)))).mod(p.multiply(q));
        return m;
    }
}

class ModPowBinary{
    public BigInteger modPow(BigInteger a, BigInteger m, BigInteger n) {
        if (m.signum() < 0 || n.signum() != 1)
            throw new IllegalArgumentException();

        //[STEP 1]
        BigInteger s = BigInteger.ONE;
        //[STEP 2]
        for (int j = m.bitLength() - 1; j >= 0; j--) {
            //[STEP 2-1]
            s = s.multiply(s).mod(n);
            //[STEP 2-2]
            if (m.testBit(j)) s = s.multiply(a).mod(n);
        }
        //[STEP 3]
        return s;
    }
}

class ExtendedEuclidGCD{
    private BigInteger x_, y_, gcd_;

    void gcd(BigInteger a, BigInteger b){
        //引数が正であるか確認
        if(a.signum() != 1 || b.signum() != 1){
            throw new IllegalArgumentException();
        }
        //初期値の設定
        BigInteger xPrev = BigInteger.ONE, yPrev = BigInteger.ZERO;
        BigInteger x = BigInteger.ZERO, y = BigInteger.ONE;
        BigInteger rPrev = a, r = b;
        //剰余が0になるまで繰り返す
        while(!r.equals(BigInteger.ZERO)){
            BigInteger qNext = rPrev.divide(r);
            BigInteger rNext = rPrev.remainder(r);
            BigInteger xNext = xPrev.subtract(x.multiply(qNext));
            BigInteger yNext = yPrev.subtract(y.multiply(qNext));
            xPrev = x; yPrev = y;
            x = xNext; y = yNext;
            rPrev = r; r = rNext;
        }
        this.gcd_ = rPrev;
        this.x_ = xPrev;
        this.y_ = yPrev;
    }
    
    BigInteger getX(){ return this.x_;}
    BigInteger getY(){ return this.y_;}
    BigInteger getGCD(){ return this.gcd_;}
}


