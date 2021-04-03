import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;

public class Assignment1 
{
    public static void main(String[] args) throws Exception
    {
        boolean encrypt = false,decrypt = false , brk = false;
        String inputfile="",outputfile="",keyfile="";

        for (int i=0 ; i<args.length ; i++)
        {
            if (args[i].equals("-e")) 
                encrypt=true;
            if (args[i].equals("-d")) 
                decrypt=true;
            if (args[i].equals("-b"))
                brk = true;
            if (args[i].equals("-k")) 
                keyfile = args[i+1];
            if (args[i].equals("-i")) 
                inputfile = args[i+1];
            if (args[i].equals("-o")) 
                outputfile = args[i+1];
            
        }

        if (encrypt)
            encrypt(keyfile, inputfile, outputfile);
        if (decrypt)
            decrypt(keyfile, inputfile, outputfile);
        if (brk)
            breakc(keyfile, inputfile , outputfile);
        
    }
    

    public static byte[][] AES1(byte[][] M , byte[][] K)
    {
         return XOR(SwapIndexes(M),K);    
    }

    public static byte[][] AES1_DEC(byte[][] C,byte[][] K)
    {
        return SwapIndexes(XOR(C,K));
    }

    public static byte[][] AES2 (byte[][] M, byte[][] K1,byte[][] K2)
    {
        byte[][] M1=AES1(M,K1);
        return AES1(M1,K2);
    }

    public static byte[][] AES2_DEC(byte[][] C ,byte[][] K1, byte[][] K2)
    {
        byte[][] C1 = AES1_DEC(C,K2);
        return AES1_DEC(C1,K1);
    }

    public static void breakc(String keyfile, String inputfile, String outputfile)
    {

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

    public static byte[][] breakTheCypher(byte[][] M, byte[][] C)
    {
        byte[][] SwapK1= { 
            {(byte)0xa0 ,(byte) 0x88 , (byte)0x23 , (byte)0x2a},
            {(byte)0xfa ,(byte) 0x54 ,(byte)0xa3 ,(byte)0x6c},
            {(byte)0xfe , (byte)0x2c ,(byte)0x39 , (byte)0x76},
            {(byte)0x17 , (byte)0xb1 ,(byte)0x39 , (byte)0x05}
        };
 
        byte[][]K2= XOR(XOR(XOR(SwapK1,M),C),SwapK1);
        byte[][]K1=SwapIndexes(SwapK1);
        System.out.println("K1: ");
        print_matrix(K1);
        System.out.println("K2: ");
        print_matrix(K2);
        return null;
    }
    
    public static void encrypt(String keyfile,String inputfile, String outputfile) throws Exception
    {
        byte[] k1 = new byte[16];
        byte[] k2 = new byte[16];
        read_keys(keyfile, k1, k2);
        byte[][] K1 = reshape(k1);
        byte[][] K2 = reshape(k2);
        byte[] buffer = new byte[16];
        InputStream in = new FileInputStream(inputfile);
        OutputStream out = new FileOutputStream(outputfile);
        while ( in.read(buffer, 0, 16) == 16)
        {
            byte[][] block = reshape(buffer);
            byte[] cipher = flatten(AES2(block,K1,K2));
            out.write(cipher, 0, 16);;
        }
        in.close();
        out.close();
    } 

    public static void decrypt(String keyfile,String inputfile, String outputfile) throws Exception
    {
        byte[] k1 = new byte[16];
        byte[] k2 = new byte[16];
        read_keys(keyfile, k1, k2);
        byte[][] K1 = reshape(k1);
        byte[][] K2 = reshape(k2);
        byte[] buffer = new byte[16];
        InputStream in = new FileInputStream(inputfile);
        OutputStream out = new FileOutputStream(outputfile);
        while ( in.read(buffer,0,16) ==16 )
        {
            byte[][] cipher_block = reshape(buffer);
            byte[] M = flatten(AES2_DEC(cipher_block, K1, K2));
            out.write(M,0,16);
        }
        in.close();
        out.close();
    }

    public static void read_keys(String path,byte[] k1, byte[] k2) throws Exception
    {
        InputStream input = new FileInputStream(path);
        input.read(k1,0, 16);
        input.read(k2,0,16);
        input.close();
    }

    //recieves 16 cells array and returns 4x4 matrix
    public static byte[][] reshape(byte[] arr)
    {
        byte[][] matrix = new byte[4][4];
        for (int i=0 ; i<arr.length ; i++)
        {
            matrix[i/4][i%4]=arr[i];
        }
        return matrix;
    }

    //receives 4x4 matrix and returns 16 cells array
    public static byte[] flatten(byte[][] matrix)
    {
        byte[] arr = new byte[16];
        for (int i=0 ; i<matrix.length ; i++)
        {
            for (int j=0 ; j<matrix[0].length ; j++)
            {
                arr[(i*4)+j] = matrix[i][j];
            }
        }
        return arr;
    }

    public static void print_array(byte[] M)
    {
        for (int i = 0; i < M.length ; i++)
        {
                System.out.print(String.format("%02X" , (M[i])) + ",");
        }
        System.out.println("");

    }

    public static void print_matrix(byte[][] M)
    {
        for (int i = 0; i < M.length ; i++)
        {
            for (int j= 0 ; j<M[0].length; j++)
            {
                System.out.print(String.format("%02X" , (M[i][j])) + ",");
            }
            System.out.println("");
        }
    }
}







// byte[][] M = { 
//     {(byte)0x04 ,(byte) 0xe0 , (byte)0x48 , (byte)0x28},
//     {(byte)0x66 ,(byte) 0xcb , (byte)0xf8 , (byte)0x06},
//     {(byte)0x81 , (byte)0x19 , (byte)0xd3 , (byte)0x26},
//     {(byte)0xe5 , (byte)0x9a , (byte)0x7a , (byte)0x4c}
// };
// byte[][]  K1= { 
//     {(byte)0xa0 ,(byte) 0x18 , (byte)0x23 , (byte)0x2a},
//     {(byte)0xea ,(byte) 0x54 , (byte)0xa3 , (byte)0x8c},
//     {(byte)0x3e , (byte)0x4c , (byte)0x3f , (byte)0x16},
//     {(byte)0x17 , (byte)0xb1 , (byte)0x29 , (byte)0x05}
// };

// byte[][]  K2= { 
//     {(byte)0xa9 ,(byte) 0x38 , (byte)0x23 , (byte)0x2a},
//     {(byte)0xea ,(byte) 0x54 , (byte)0x43 , (byte)0x8c},
//     {(byte)0x3e , (byte)0x42 , (byte)0x5f , (byte)0x16},
//     {(byte)0x16 , (byte)0xb2 , (byte)0x27 , (byte)0x05}
// };