import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException; 
import java.util.Arrays; 

public class SHA1{
    static final int BUFSIZE = 256;
    
    private static final int[] K = {0x5a827999, 0x6ed9eba1,
        0x8f1bbcdc, 0xca62c1d6};
    
    private static final int[] H = {0x67452301, 0xefcdab89,
        0x98badcfe, 0x10325476, 0xc3d2e1f0};
    
    private int f(final int t, int b, final int c, final int d){
        if(t < 0 || 80 < t){
            throw new IllegalArgumentException();
        }else if(t < 20){
            return b & c | ~b & d;
        }else if(40 <= t && t < 60){
            return b & c | b & d | c & d;
        }else {
            return b ^ c ^ d;
        }
    }

    private int rotl(final int r, final int x){
        final int rot = r % 32;
        return (x >>> (32 - rot)) | (x << rot);
    }

    private void hashBlock(final int h[], final byte[] data, final int offset){
        final int[] w = new int[80];
        for(int i = 0; i < 16; i++){
            w[i] = (data[offset + i * 4] & 0xff) << 24
                | (data[offset + i * 4 + 1] & 0xff) << 16
                | (data[offset + i * 4 + 2] & 0xff) << 8
                | data[offset + i * 4 + 3] & 0xff;
        }
        for(int t = 16; t < 80; t++){
            w[t] = this.rotl(1, w[t - 3] ^ w[t - 8] ^ w[t - 14] ^ w[t - 16]);
        }
        int a = h[0];
        int b = h[1];
        int c = h[2];
        int d = h[3];
        int e = h[4];
        for(int t = 0; t < 80; t++){
            final int temp = this.rotl(5, a) + this.f(t, b, c, d) + e + w[t]
                + SHA1.K[t / 20];
            e = d;
            d = c;
            c = this.rotl(30, b);
            b = a;
            a = temp;
        }
        h[0] += a; 
        h[1] += b; 
        h[2] += c; 
        h[3] += d; 
        h[4] += e; 
    }

    public int[] hash(final byte[] data){
        if(data == null){
            throw new IllegalArgumentException();
        }        
        final int[] h = SHA1.H.clone();
        int i;
        for(i = 0; i < data.length - 64; i += 64){
            this.hashBlock(h, data, i);
        }
        final byte[] tmp = new byte[64];
        final int remains = data.length - i;
        System.arraycopy(data, i, tmp, 0, remains);
        tmp[remains] = (byte) 0x80;

        if(remains > 64 - 8 - 1){
            Arrays.fill(tmp, remains + 1, 64, (byte) 0);
            this.hashBlock(h, tmp, 0);
            Arrays.fill(tmp, 0, 64 - 8, (byte) 0);
        }else{
            Arrays.fill(tmp, remains + 1, 64 - 8, (byte) 0);
        }

        final long len = (long) data.length * 8;
        tmp[56] = (byte) (len >> 56);
        tmp[57] = (byte) (len >> 48);
        tmp[58] = (byte) (len >> 40);
        tmp[59] = (byte) (len >> 32);  
        tmp[60] = (byte) (len >> 24);   
        tmp[61] = (byte) (len >> 16);
        tmp[62] = (byte) (len >> 8);
        tmp[63] = (byte) len; 
        this.hashBlock(h, tmp, 0);
        
        return h; 
    }

    public static void main(final String[] argv) throws Exception{
    	byte[] buf = new byte[SHA1.BUFSIZE];
        int top = 0;
        int limit = SHA1.BUFSIZE;
        int len;
        while ((len = System.in.read(buf, top, limit)) != -1) {
            final byte[] tmp = new byte[top + len + SHA1.BUFSIZE];
            System.arraycopy(buf, 0, tmp, 0, top + len);
            buf = tmp;
            top = top + len;
            limit = SHA1.BUFSIZE;
        }
        final byte[] tmp = new byte[top];
        System.arraycopy(buf, 0, tmp, 0, top);

        final int[] sha1hash = new SHA1().hash(tmp);

        boolean firstElementP = true;
        for (final int v : sha1hash) {
            if (firstElementP) {
                firstElementP = false;
            } else {
                System.out.print(" ");
            }
            System.out.print(Integer.toHexString(v));
        }
        System.out.println();
    }
}
