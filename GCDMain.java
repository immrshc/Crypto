import java.math.BigInteger;

public class GCDMain{
    public static void main(String[] argv){
        //GCDTrialDivision gcdFunc = new GCDTrialDivision();
        //GCDEuclid gcdFunc = new GCDEuclid();
        ExtendedEuclidGCD gcdFunc = new ExtendedEuclidGCD();
        if(argv.length != 2){return;}
        BigInteger a = new BigInteger(argv[0]);
        BigInteger b = new BigInteger(argv[1]);
        //BigInteger gcd = gcdFunc.gcd(a, b);
        gcdFunc.gcd(a, b);
        //System.out.println(gcd);
        System.out.println("gcd = " + gcdFunc.getGCD()); 
        //System.out.println(gcdFunc.getGCD().equals(BigInteger.ONE));
        System.out.println("x = " + gcdFunc.getX());
        System.out.println("y = " + gcdFunc.getY());
    }
}

class GCDTrialDivision{
    //目的：試行除算を行う
    //gcd: BigInteger -> BigInteger -> BigInteger
    BigInteger gcd(BigInteger a, BigInteger b){
        //引数が正であるか確認
        if(a.signum() != 1 || b.signum() != 1){
            throw new IllegalArgumentException();
        }
        //引数の最小値より割る数が小さい限り割る
        BigInteger g = BigInteger.ONE;
        if(a.compareTo(b) < 0){
            BigInteger t = a;
            a = b;
            b = t;
        }
        //2で割り、割り切れなくなったら増分する
        for(BigInteger n = BigInteger.valueOf(2);
            n.compareTo(b) <= 0; n = n.add(BigInteger.ONE)){
            //剰余がゼロになるまで、割る
            while(a.remainder(n).equals(BigInteger.ZERO) &&
                    b.remainder(n).equals(BigInteger.ZERO)){
                g = n.multiply(g);
                a = a.divide(n);
                b = b.divide(n);
            }
        }
        //最大公約数gを返す
        return g;
    }
}

class GCDEuclid{
    BigInteger gcd(BigInteger a, BigInteger b){
        //引数が正であるか確認
        if(a.signum() != 1 || b.signum() != 1){
            throw new IllegalArgumentException();
        }
        //剰余が0になるまで繰り返す
        for(BigInteger r = a.remainder(b);
            !r.equals(BigInteger.ZERO);
            r = a.remainder(b)){
            a = b; b = r;
        }
        //最大公約数を返す
        return b;
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

