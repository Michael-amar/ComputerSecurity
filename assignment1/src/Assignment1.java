import java.lang.String;

public class Assignment1 
{
    public static void main(String[] args)
    {
        byte[][] M = { 
            {(byte)0x04 ,(byte) 0xe0 , (byte)0x48 , (byte)0x28},
            {(byte)0x66 ,(byte) 0xcb , (byte)0xf8 , (byte)0x06},
            {(byte)0x81 , (byte)0x19 , (byte)0xd3 , (byte)0x26},
            {(byte)0xe5 , (byte)0x9a , (byte)0x7a , (byte)0x4c}
        };
        byte[][]  K1= { 
            {(byte)0xa0 ,(byte) 0x18 , (byte)0x23 , (byte)0x2a},
            {(byte)0xea ,(byte) 0x54 , (byte)0xa3 , (byte)0x8c},
            {(byte)0x3e , (byte)0x4c , (byte)0x3f , (byte)0x16},
            {(byte)0x17 , (byte)0xb1 , (byte)0x29 , (byte)0x05}
        };

        byte[][]  K2= { 
            {(byte)0xa9 ,(byte) 0x38 , (byte)0x23 , (byte)0x2a},
            {(byte)0xea ,(byte) 0x54 , (byte)0x43 , (byte)0x8c},
            {(byte)0x3e , (byte)0x42 , (byte)0x5f , (byte)0x16},
            {(byte)0x16 , (byte)0xb2 , (byte)0x27 , (byte)0x05}
        };
        // print_array(AddRoundKey(M, K));
        //print_array(AES1(M,K1));
        breakTheCypher(M,AES2(M, K1,K2));
        
    }
    public static void print_array(byte[][] M)
    {
        for (int i = 0; i < 4 ; i++)
        {
            for (int j= 0 ; j<4 ; j++)
            {
                System.out.print(String.format("%02X" , (M[i][j])) + ",");
            }
            System.out.println("");
        }
    }
    public static byte[][] AES1(byte[][] M , byte[][] K)
    {
         return XOR(SwapIndexes(M),K);
         
    }

    public static byte[][] XOR(byte[][] M,byte[][] K)
    {
        byte[][] newArr= new byte[4][4];
        for (int i =0 ; i<M.length ; i++)
        {
            for (int j =0 ; j<M[0].length ; j++)
            {
                newArr[i][j] =(byte) (M[i][j] ^ K[i][j]);
            }
        }
        return newArr;
    }

    public static byte[][] SwapIndexes(byte[][] M)
    {
        byte[][] newArr = new byte[4][4];
        for (int i=0 ; i< M.length ; i++)
        {
            for (int j=i ; j<M[0].length ; j++)
            {
                byte temp = M[i][j];
                newArr[i][j] = M[j][i];
                newArr[j][i] = temp;
            }
        }
        return newArr;
    }


    public static byte[][] AES2 (byte[][]M, byte[][]K1,byte[][]K2)
    {
        byte[][] M1=AES1(M,K1);
        return AES1(M1,K2);
    }



    public static byte[][] breakTheCypher(byte[][] M, byte[][] C)
    {
        byte[][] SwapK1= { 
            {(byte)0xa0 ,(byte) 0x88 , (byte)0x23 , (byte)0x2a},
            {(byte)0xfa ,(byte) 0x54 ,(byte)0xa3 ,(byte)0x6c},
            {(byte)0xfe , (byte)0x2c ,(byte)0x39 , (byte)0x76},
            {(byte)0x17 , (byte)0xb1 ,(byte)0x39 , (byte)0x05}
        };
        byte[][]K2= XOR(XOR(SwapK1,M),C);
        byte[][]K1=SwapIndexes(SwapK1);
        System.out.println("K1: ");
        print_array(K1);
        System.out.println("K2: ");
        print_array(K2);
        return null;
    }

    public static byte[][] breakTheCypherAES1(byte[][] M, byte[][] C)
    {
        
        byte[][] SwapM=SwapIndexes(M);
        byte[][] K= XOR(SwapM,C);
        System.out.println("M: ");
        print_array(M);
        System.out.println("Swap M: ");
        print_array(SwapM);
        System.out.println("C: ");
        print_array(C);
        System.out.println("K: ");
        print_array(K);
        return null;
    }
    
    
}
